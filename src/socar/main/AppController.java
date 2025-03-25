package socar.main;

import socar.reservation.service.ReservationService;
import socar.car.service.CarService;
import socar.common.AppService;
import socar.user.service.UserService;

public class AppController {

    private AppService service;
    private static UserService userService = new UserService();

    public static String getLoggedInUserId() {
        return userService.getLoggedInUserId();
    }


    // 선택한 메뉴에 따라 시스템을 정해주는 기능
    public void chooseSystem(int selectNumber) {
        String loggedInUserId_print = getLoggedInUserId();
        System.out.println("loggedInUserId = " + loggedInUserId_print);
        switch (selectNumber) {
            case 1:
                service = new UserService();
                break;
            case 2:
                service = new ReservationService();
                break;
            case 3:
                String loggedInUserId = getLoggedInUserId();
                System.out.println("loggedInUserId = " + loggedInUserId);

                if("admin".equals(loggedInUserId)) {
                    service = new CarService();
                    System.out.println("관리자이지롱~ ");
                }
                else {
                    System.out.println("관리자만 실행 가능합니다.");
                }
                break;

//                service = new CarService();  // 관리자인 경우만 3. 입력 가능
//                break;
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

}


