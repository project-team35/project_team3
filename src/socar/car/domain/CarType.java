package socar.car.domain;

public enum CarType {

    AVANTE("아반떼", 1500),
    SONATA("쏘나타", 1600),
    GRANDEUR("그랜져", 1700),
    PORTER("포터" , 2000),
    G90("G90" , 2000),
    MODEL3("모델3" , 0),
    CYBERTRUCK("사이버트럭" , 0),
    XC90("XC90" , 2000);

    private final int Displacement;
    private final String CarNameKor;

    CarType( String carNameKor, int displacement) {

        CarNameKor = carNameKor;
        Displacement = displacement;
    }


    public String getCarNameKor() {
        return CarNameKor;
    }

    public int getDisplacement() {
        return Displacement;
    }
}

