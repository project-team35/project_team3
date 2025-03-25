package socar.main;

//import socar.movie.service.MovieService;
//import video.order.service.OrderService;
import socar.common.AppService;
import socar.user.service.UserService;

public class AppController {

    private AppService service;

    // 선택한 메뉴에 따라 시스템을 정해주는 기능
    public void chooseSystem(int selectNumber) {
        switch (selectNumber) {
            case 1:
                service = new UserService();
                break;
//            case 2:
//                service = new ReserationsService();
//                break;
//            case 3:
//                service = new CarService();
//                break;
            case 2:
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


