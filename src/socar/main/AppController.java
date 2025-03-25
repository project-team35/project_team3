package socar.main;

import socar.car.service.CarService;
import socar.common.AppService;
import socar.reservation.service.ReservationService;
import socar.user.service.UserService;

public class AppController {

    private AppService service;
    private UserService userService = new UserService();  // UserService 인스턴스

    // 선택한 메뉴에 따라 시스템을 정해주는 기능
    public void chooseSystem(int selectNumber) {
        switch (selectNumber) {
            case 1:
                service = new UserService();
                break;
            case 2:
                service = new ReservationService();
                break;
            case 3:
                String loggedInUserId = getLoggedInUserId();
                if("admin".equals(loggedInUserId)) {
                    service = new CarService();
                }
                else {
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
    // 로그인된 사용자 아이디를 반환하는 메서드
    public String getLoggedInUserId() {
        return ((UserService)service).getLoggedInUserId();  // 로그인된 사용자 아이디 반환
    }

}


