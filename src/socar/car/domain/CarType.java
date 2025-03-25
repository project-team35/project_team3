package socar.car.domain;

public enum CarType {

    AVANTE(1, "아반떼", 1500), SONATA(2, "쏘나타", 1600), GRANDEUR(3, "그랜져", 1700), PORTER(4, "포터", 2000), G90(5, "G90", 2000), MODEL3(6, "모델3", 0), CYBERTRUCK(7, "사이버트럭", 0), XC90(8, "XC90", 2000);

    private final int EnumId;
    private final int Displacement;
    private final String CarNameKor;

    CarType(int enumId, String carNameKor, int displacement) {
        EnumId = enumId;
        CarNameKor = carNameKor;
        Displacement = displacement;
    }

    public int getEnumId() {return EnumId; }
    public String getCarNameKor() {
        return CarNameKor;
    }
    public int getDisplacement() {
        return Displacement;
    }

    public static String getName(int input){
        for (CarType car : CarType.values()) {
           if(input == car.getEnumId()){
               return car.getCarNameKor();
           }
        }
        return null;
    }
}

