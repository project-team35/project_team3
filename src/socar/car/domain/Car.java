package socar.car.domain;

public class Car {


    private int carId;
    private String carType;
    private int dailyFee;
    private String is_active;

    public Car(String carType, int dailyFee) {
        this.carId = carId;
        this.carType = carType;
        this.dailyFee = dailyFee;
        this.is_active = "Y";
    }
    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public int getDailyFee() {
        return dailyFee;
    }

    public void setDailyFee(int dailyFee) {
        this.dailyFee = dailyFee;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    @Override
    public String toString() {
        return "Car{" +
                "차량번호=" + carId +
                ", 차량이름='" + carType + '\'' +
                ", 일일요금=" + dailyFee + "원"+
                ", 활성화 여부=" + is_active +
                '}';
    }
}
