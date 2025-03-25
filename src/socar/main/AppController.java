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

    // 선택한 메뉴에 따라 시스템을 정해주는 기능
    public void chooseSystem(int selectNumber) {
        switch (selectNumber) {
            case 1:
                if (userService.isLoggedIn()) {
                    System.out.println("이미 로그인된 상태입니다.");
                    return;
                }
                service = new UserService(this);  // UserService에 AppController 전달
                break;
            case 2:
                service = new ReservationService();
                break;
            case 3:
                if ("admin".equals(loggedInUserId)) {
                    service = new CarService();
                } else {
                    System.out.println("관리자만 실행 가능합니다.");
                    return;
                }
                break;
            case 4:
                System.out.println("# 프로그램을 종료합니다.");
                System.exit(0);
            default:
                System.out.println("# 존재하지 않는 메뉴입니다.");
        }

        try {
            service.start();
        } catch (Exception e) {
            System.out.println("# 메뉴를 다시 입력하세요!");
        }
    }

    // 로그인된 사용자 아이디를 설정
    public void setLoggedInUserId(String userId) {
        this.loggedInUserId = userId;
    }

}



