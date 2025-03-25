package socar.car.service;
import socar.car.domain.Car;
import socar.car.domain.CarType;
import socar.car.repository.CarRepository;
import socar.common.AppService;

import java.sql.SQLException;

import static socar.ui.AppUi.*;

public class CarService implements AppService {

    private final CarRepository carRepository = new CarRepository();

    @Override
    public void start() throws SQLException {
        while (true) {
            carManagementScreen(); // AppUI 에서 이름 통일해야 함
            int selection = inputInteger(">>> ");

            switch (selection) {
                case 1:
                    insertCarData();
                    break;
                case 2:
                    inactiveCarData();
                    break;

                default:
                    System.out.println("### 메뉴를 다시 입력하세요.");

            }

        }
    }


    //  차 추가  CarAdd
    private void insertCarData() throws SQLException {
        System.out.println("\n ====== 자동차 정보를 추가합니다. ======");

        String carType  =  inputString("# 차량종류: ");
        int dailyFee =  inputInteger("# 일일대여료: ");
        String is_active =  inputString("# 활성화여부: ");

        Car car = new Car(carType, dailyFee);
        car.setIs_active(is_active);

        CarRepository.addCar(car);

        System.out.printf("\n### [%s] 정보가 정상적으로 추가되었습니다.");
    }

    //  차 비활성화 CarInactive
    private void inactiveCarData() {
        System.out.println("\n ====== 자동차 상태를 비활성화합니다. ======");
        int carId =  inputInteger("# 차량 번호: ");
        carRepository.inactiveCar(carId);
    }



}
