package socar.main;

import static socar.ui.AppUi.inputInteger;
import static socar.ui.AppUi.startScreen;

public class Main {

    public static void main(String[] args) {

        AppController controller = new AppController();

        while (true) {
            startScreen();
            int selectNumber = inputInteger(">>> ");
            controller.chooseSystem(selectNumber);
        }

    }

}