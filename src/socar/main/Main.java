package socar.main;

import static socar.ui.AppUi.inputInteger;
import static socar.ui.AppUi.startScreen;

public class Main {

    public static void main(String[] args) {

        AppController controller = new AppController();

        while (true) {
            // 로그인 여부에 따른 startScreen 호출
            startScreen(controller.isLoggedIn());  // 로그인 상태를 boolean 값으로 전달
            int selectNumber = inputInteger(">>> ");
            controller.chooseSystem(selectNumber);
        }
    }
}
