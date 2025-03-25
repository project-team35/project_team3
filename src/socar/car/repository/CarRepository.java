package socar.car.repository;

import socar.car.domain.Car;
import socar.jdbc.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarRepository {

    public static void addCar(Car Car) throws SQLException {
        String sql = "INSERT INTO cars VALUES (car_seq.NEXTVAL, ?, ?, ?)";

        try(Connection conn = DBConnectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, Car.getCarType());
            pstmt.setInt(2, Car.getDailyFee());
            pstmt.setString(3, Car.getIs_active());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //자동차 비활성화
    public static void inactiveCar(int carId){
        String sql = "UPDATE cars SET \"is_active\" = 'N' WHERE \"car_id\" = " + carId;

        try(Connection conn = DBConnectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
}
    // 자동차 목록 출력
    public static void showCarList(){
        String sql = "SELECT * FROM cars WHERE \"is_active\" = 'Y' " ;

        try(Connection conn = DBConnectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {


            while (rs.next()) {
                System.out.print("차량 번호: " + rs.getString(1));
                System.out.print(", 차량 종류: " + rs.getString(2) + "\n");
        }
    }   catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
