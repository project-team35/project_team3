package socar.main;

import socar.car.service.CarService;
import socar.common.AppService;
import socar.reservation.service.ReservationService;
import socar.user.service.UserService;

public class AppController {

    private AppService service;
    private UserService userService;
    private String loggedInUserId;  // 로그인된 사용자 아이디

    public AppController() {
        // AppController 생성 시 UserService 인스턴스를 넘겨줍니다.
        this.userService = new UserService(this); // AppController 전달
    }

    // 시스템 메뉴 선택 처리
    public void chooseSystem(int selectNumber) {
        switch (selectNumber) {
            case 1: // 회원 가입 / 로그인
                if (isLoggedIn()) {
                    System.out.println("이미 로그인된 상태입니다.");
                    return;
                }
                service = new UserService(this);  // UserService에 AppController 전달
                break;
            case 2: // 차량 예약
                if (!isLoggedIn()) {
                    System.out.println("로그인이 필요합니다.");
                    return;
                }
                service = new ReservationService(loggedInUserId); // 로그인된 사용자 ID 전달
                break;

            case 3: // 서비스 관리 (관리자만 접근 가능)
                if (!isLoggedIn()) {
                    System.out.println("로그인이 필요합니다.");
                    return;
                }
                if ("admin".equals(loggedInUserId)) {
                    service = new CarService();
                } else {
                    System.out.println("관리자만 실행 가능합니다.");
                    return;
                }
                break;
            case 4: // 로그아웃
                if (isLoggedIn()) {
                    userService.logout(); // 로그아웃 메소드 호출
                    return;// 로그아웃 후 메인 화면으로 돌아가기
                }
                break;
            case 5: // 프로그램 종료
                System.out.println("| 프로그램을 종료합니다.");
                System.exit(0);
            default:
                System.out.println("| 존재하지 않는 메뉴입니다.");
                return;
        }

        // 메뉴 선택에 따른 서비스 실행
        try {
            service.start();
        } catch (Exception e) {
            System.out.println("| 메뉴를 다시 입력하세요!");
        }
    }

    // 로그인된 사용자 아이디를 설정
    public void setLoggedInUserId(String userId) {
        this.loggedInUserId = userId;
    }

    // 로그인된 사용자 아이디를 반환
    public String getLoggedInUserId() {
        return loggedInUserId;
    }

    // 로그인 여부 확인
    public boolean isLoggedIn() {
        return loggedInUserId != null;  // 로그인된 사용자가 있으면 true 반환
    }
}
