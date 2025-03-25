package reservation.service;

import reservation.domain.ReservationObject;
import reservation.domain.ReservationPolicy;
import reservation.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ReservationService {
    private final ReservationRepository repo = new ReservationRepository();
    private final Scanner sc = new Scanner(System.in);

    public void start(String userId) {
        while (true) {
            System.out.println("\n--- 예약 시스템 ---");
            System.out.println("1. 예약하기");
            System.out.println("2. 예약취소");
            System.out.println("3. 예약조회");
            System.out.println("4. 종료");
            System.out.print(">>> ");
            int sel = Integer.parseInt(sc.nextLine());

            switch (sel) {
                case 1 -> makeReservation(userId);
                case 2 -> cancel(userId);
                case 3 -> view(userId);
                case 4 -> { System.out.println("종료합니다."); return; }
                default -> System.out.println("잘못된 입력");
            }
        }
    }

    private void makeReservation(String userId) {
        if (repo.hasActiveReservation(userId)) {
            System.out.println("이미 예약 중인 차량이 있습니다.");
            return;
        }

        System.out.print("시작일 (yyyy-mm-dd): ");
        LocalDate start = LocalDate.parse(sc.nextLine());

        System.out.print("종료일 (yyyy-mm-dd): ");
        LocalDate end = LocalDate.parse(sc.nextLine());

        long days = ReservationPolicy.calculateDays(start, end);
        long fee = ReservationPolicy.calculateTotalFee(50000, days); // 일일요금 임시

        System.out.printf("결제 금액: %d원\n", fee);

        ReservationObject r = new ReservationObject(userId, 1, start, end, fee);
        repo.save(r);

        System.out.printf("예약이 완료되었습니다. (%s ~ %s)\n", start, end);
    }

    private void cancel(String userId) {
        List<ReservationObject> list = repo.findByUser(userId);
        for (ReservationObject r : list) {
            if (!r.isCancelled() && r.getEndDate().isAfter(LocalDate.now())) {
                System.out.printf("예약 ID: %d | 기간: %s ~ %s | 금액: %d원\n",
                        r.getReservationId(), r.getStartDate(), r.getEndDate(), r.getTotalFee());
            }
        }

        System.out.print("취소할 예약 ID 입력: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.println("입력하신 예약이 맞습니까? (1. 맞다 / 2. 아니다)");
        int c = Integer.parseInt(sc.nextLine());

        if (c == 1) {
            boolean success = repo.cancelReservation(id, userId);
            System.out.println(success ? "예약이 취소되었습니다." : "취소 실패");
        }
    }

    private void view(String userId) {
        List<ReservationObject> list = repo.findByUser(userId);
        for (ReservationObject r : list) {
            String status = r.isCancelled() ? "취소됨" : (ReservationPolicy.isReturned(r.getEndDate()) ? "반납됨" : "예약 중");
            System.out.printf("예약ID: %d | 기간: %s ~ %s | 금액: %d원 | 상태: %s\n",
                    r.getReservationId(), r.getStartDate(), r.getEndDate(), r.getTotalFee(), status);
        }
    }
}

