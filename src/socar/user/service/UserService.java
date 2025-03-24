package socar.user.service;

import socar.common.Appservice;
import socar.user.domain.User;
import socar.ui.AppUi;
import socar.user.repository.UserRepository;

public class UserService implements Appservice {

    private final UserRepository userRepository = new UserRepository();

    @Override
    public void start() {
        while (true) {
            AppUi.userManagementScreen();
            int selection = AppUi.inputInteger(">>> ");

            switch (selection) {
                case 1:
                    join();  // 회원가입
                    break;
                case 2:
                    login();  // 로그인
                    break;
                case 3:
                    deleteUser();  // 회원 탈퇴
                    break;
                case 4:
                    return;  // 종료
                default:
                    System.out.println("# 메뉴를 다시 입력하세요!");
            }
        }
    }

    // 회원 가입
    private void join() {
        System.out.println("\n====== 회원 가입을 진행합니다. ======");
        String userId = AppUi.inputString("# 사용자 아이디: ");
        String userName = AppUi.inputString("# 사용자 이름: ");
        String password = AppUi.inputString("# 비밀번호: ");
        int licenseNum = AppUi.inputInteger("# 면허번호: ");

        // 관리자 여부를 입력받지 않음, 기본값은 'N'으로 설정
        char isAdmin = 'N';  // 기본값은 관리자가 아님

        // User 객체 생성
        User newUser = new User(userId, userName, password, licenseNum, isAdmin);

        // 사용자 정보 저장
        userRepository.addUser(newUser);

        // 회원 가입 완료 메시지 출력
        System.out.printf("\n### [%s]님의 회원 가입이 완료되었습니다.\n", newUser.getUserName());
    }

    private void login() {
        System.out.println("\n====== 로그인 ======");
        String userId = AppUi.inputString("# 사용자 아이디: ");
        String password = AppUi.inputString("# 비밀번호: ");

        // 아이디와 비밀번호로 사용자 정보 조회
        User user = userRepository.findUserByIdAndPassword(userId, password);

        if (user != null) {  // 사용자 정보가 존재하면
            System.out.printf("\n### [%s]님, 로그인에 성공했습니다.\n", user.getUserName());
        } else {
            System.out.println("\n### 아이디나 비밀번호가 잘못되었습니다.");
        }
    }

    // 회원 탈퇴
    private void deleteUser() {
        System.out.println("\n====== 회원 탈퇴 ======");
        String userId = AppUi.inputString("# 탈퇴할 사용자 아이디: ");
        String password = AppUi.inputString("# 비밀번호: ");

        // 아이디와 비밀번호를 기반으로 사용자 조회
        User user = userRepository.findUserByIdAndPassword(userId, password);

        if (user != null) {
            // 비밀번호가 일치하는 경우
            userRepository.deactivateUser(userId);  // 회원 비활성화 처리
            System.out.printf("\n###[%s]님의 회원 탈퇴가 완료되었습니다." , user.getUserName());
        } else {
            // 비밀번호나 아이디가 일치하지 않으면
            System.out.println("\n### 아이디나 비밀번호가 잘못되었습니다.");
        }
    }

}
