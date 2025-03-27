package socar.reservation.service;

import socar.car.domain.CarType;
import socar.reservation.domain.ReservationObject;
import socar.reservation.domain.ReservationPolicy;
import socar.reservation.repository.ReservationRepository;
import socar.common.AppService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ReservationService implements AppService {
    private final ReservationRepository repo = new ReservationRepository();
    private final Scanner sc = new Scanner(System.in);
    private final String userId;

    public ReservationService(String userId) {
        this.userId = userId;
    }

    @Override
    public void start() {
        while (true) {
            System.out.println("\n--- 예약 시스템 ---");
            System.out.println("1. 예약하기");
            System.out.println("2. 예약취소");
            System.out.println("3. 예약조회");
            System.out.println("4. 종료");
            System.out.print(">>> ");
            int sel = Integer.parseInt(sc.nextLine());
            switch (sel) {
                case 1 : makeReservation(userId); break;
                case 2 : cancel(userId); break;
                case 3 : showReservationList(userId); break;
                case 4 : {
                    System.out.println("종료합니다.");
                    return;
                }
                default : System.out.println("잘못된 입력");
            }
        }
    }


    // 차량 예약하기
    private void makeReservation(String userId) {

        System.out.print("시작일 (yyyy-mm-dd): ");
        LocalDate start = LocalDate.parse(sc.nextLine());

        System.out.print("종료일 (yyyy-mm-dd): ");
        LocalDate end = LocalDate.parse(sc.nextLine());

        // 시작일이 과거일 경우
        if (start.isBefore(LocalDate.now())) {
            System.out.println("이미 지난 날짜로는 예약할 수 없습니다.");
            return;
        }

        // 종료일이 시작일보다 이전일 경우
        if (end.isBefore(start)) {
            System.out.println("종료일은 시작일보다 이후여야 합니다.");
            return;
        }


        // 1일 이상 예약이 들어오지 않았을 시 구동
        long day = ReservationPolicy.calculateDays(start, end);
        if (day < 1) {
            System.out.println("최소 1일 이상 예약해야 합니다.");
            return;
        }

        // car_id 받아와서 차 목록 보여주기
        List<Map<String, Object>> availableCars = repo.findAvailableCars(start, end);

        if (availableCars.isEmpty()) {
            System.out.println("예약 가능한 차량이 없습니다.");
            return;
        }

        System.out.println("\n[ 예약 가능 차량 목록 ]");
        for (Map<String, Object> car : availableCars) {

            String carTypeName = (String) car.get("carType");

            // CarType enum에서 배기량 가져오기
            int displacement = -1;
            for (CarType ct : CarType.values()) {
                if (ct.getCarNameKor().equals(carTypeName)) {
                    displacement = ct.getDisplacement();
                    break;
                }
            }

            System.out.printf("차량번호: %s | 차종: %s | 배기량: %dcc | 일일요금: %s원\n",
                    car.get("carId"), carTypeName, displacement, car.get("dailyFee"));
        }

        System.out.print("예약할 차량 번호(car_id) 입력: ");
        int carId = Integer.parseInt(sc.nextLine());

        // car_id 유효성 검사
        Map<String, Object> selectedCar = availableCars.stream()
                .filter(c -> (int) c.get("carId") == carId)
                .findFirst()
                .orElse(null);

        if (selectedCar == null) {
            System.out.println("선택한 차량은 예약 불가합니다.");
            return;
        }

        // 일일 요금 추출 (Object -> long 변환)
        long dailyFee = ((Number) selectedCar.get("dailyFee")).longValue();

        // 예약일 수 계산
        long days = ReservationPolicy.calculateDays(start, end);

        //  calculateTotalFee 메서드 활용하여 최종 결제 금액 계산
        long fee = ReservationPolicy.calculateTotalFee(dailyFee, days);

        System.out.printf("결제 금액: %d원\n", fee);

        ReservationObject r = new ReservationObject(userId, carId, start, end, fee);
        repo.save(r); // 예약 저장

        System.out.printf("[%s]님 예약이 완료되었습니다. (%s ~ %s)\n",userId, start, end);
    }

    // 예약 취소
    private void cancel(String userId) {
        List<ReservationObject> list;

        if ("admin".equals(userId)) {
            // 관리자(admin)는 모든 예약을 조회
            list = repo.findAll();  // 모든 예약 조회
        } else {
            // 일반 사용자는 본인의 예약만 조회
            list = repo.findByUser(userId);
        }

        boolean hasCancelable = false;

        for (ReservationObject r : list) {
            if (!r.isCancelled() && r.getEndDate().isAfter(LocalDate.now())) {
                System.out.printf("예약 ID: %d | 사용자: %s | 기간: %s ~ %s | 금액: %d원\n",
                        r.getReservationId(), r.getUserId(), r.getStartDate(), r.getEndDate(), r.getTotalFee());
                hasCancelable = true;
            }
        }

        if (!hasCancelable) {
            System.out.println("예약 내역이 없습니다.");
            return;
        }

        // 취소할 예약 ID 입력 받기
        System.out.print("취소할 예약 ID 입력: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.println("입력하신 예약이 맞습니까?");
        System.out.println("1. 예 | 2. 아니오");
        int c = Integer.parseInt(sc.nextLine());

        if (c == 1) {
            // 예약 ID로 예약 객체 찾기
            ReservationObject reservation = repo.findById(id);

            if (reservation == null) {
                System.out.println("해당 예약을 찾을 수 없습니다.");
                return;
            }

            // 관리자가 취소할 경우, 해당 예약의 실제 사용자 ID로 취소를 진행
            boolean success = false;
            if ("admin".equals(userId)) {
                // 관리자라면, 해당 예약의 실제 사용자 ID로 취소 작업 진행
                success = repo.cancelReservation(id, reservation.getUserId());  // 관리자일 때는 예약의 사용자 ID로 취소
            } else {
                // 일반 사용자는 자신의 예약만 취소
                success = repo.cancelReservation(id, userId);
            }

            System.out.println(success ? "예약이 취소되었습니다." : "취소 실패 : 이전 화면으로 돌아갑니다.");
        }
    }



    // 예약 조회
    private void showReservationList(String userId) {
        List<ReservationObject> list;

        if ("admin".equals(userId)) {
            // 관리자(admin)는 모든 예약을 조회
            list = repo.findAll();  // null을 전달하여 모든 예약을 반환하게 처리
        } else {
            // 일반 사용자는 본인의 예약만 조회
            list = repo.findByUser(userId);
        }

        if (list.isEmpty()) {
            System.out.println("예약 내역이 없습니다.");
            return;
        }

        for (ReservationObject r : list) {
            String status;
            if (r.isCancelled()) {
                status = "취소됨";
            } else if (r.isReturned()) {
                status = "반납됨";
            } else {
                status = "예약 중";
            }

            System.out.printf("예약ID: %d | 예약자 이름: %s | 기간: %s ~ %s | 금액: %d원 | 상태: %s\n",
                    r.getReservationId(), r.getUserId(), r.getStartDate(), r.getEndDate(), r.getTotalFee(), status);
        }
    }
}



