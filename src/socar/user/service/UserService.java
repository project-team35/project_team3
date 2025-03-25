package socar.user.service;

import socar.common.AppService;
import socar.user.domain.User;
import socar.ui.AppUi;
import socar.user.repository.UserRepository;

public class UserService implements AppService {

    private final UserRepository userRepository = new UserRepository();
    private User loggedInUser;  // 로그인된 사용자 정보

    @Override
    public void start() {
        // 관리자 계정이 없는 경우 자동으로 생성
        createAdminAccountIfNotExists();

        while (true) {
            AppUi.userManagementScreen();
            System.out.println(getLoggedInUserId());
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

    // 관리자 계정이 없으면 자동으로 생성
    private void createAdminAccountIfNotExists() {
        String adminUserId = "admin";
        if (!userRepository.isUserIdExists(adminUserId)) {
            System.out.println("\n### 관리자 계정이 존재하지 않습니다. 자동으로 생성됩니다.");

            // 관리자 계정 생성
            User adminUser = new User(adminUserId, "admin", "admin", 123456, 'Y');
            userRepository.addUser(adminUser);
            System.out.println("\n### 관리자 계정이 생성되었습니다. 아이디: admin, 비밀번호: admin");
        }
    }

    // 회원 가입
    private void join() {
        System.out.println("\n====== 회원 가입을 진행합니다. ======");
        String userId = AppUi.inputString("# 사용자 아이디: ");

        // 아이디 중복 검사 추가
        if (userRepository.isUserIdExists(userId)) {
            System.out.println("\n### 이미 사용 중인 아이디입니다. 회원 가입을 진행할 수 없습니다.");
            return;
        }

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

    public void login() {
        System.out.println("\n====== 로그인 ======");
        String userId = AppUi.inputString("# 사용자 아이디: ");
        String password = AppUi.inputString("# 비밀번호: ");

        // 사용자 상태 확인
        if (!userRepository.isUserActive(userId)) {
            System.out.println("\n### 탈퇴한 회원입니다.");
            return;
        }

        // 아이디와 비밀번호로 사용자 정보 조회
        User user = userRepository.findUserByIdAndPassword(userId, password);

        if (user != null) {  // 사용자 정보가 존재하면
            loggedInUser = user;  // 로그인된 사용자 정보 저장
            System.out.printf("\n### [%s]님, 로그인에 성공했습니다.\n", user.getUserName());
        } else {
            System.out.println("\n### 아이디나 비밀번호가 잘못되었습니다.");
        }

    }

    // 로그인된 사용자 아이디를 반환
    public String getLoggedInUserId() {
        if (loggedInUser != null) {
            System.out.println(loggedInUser.getUserId());
            return loggedInUser.getUserId();  // 로그인된 사용자의 userId 반환
        } else {
            return null;  // 로그인되지 않은 경우 null 반환
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
            System.out.printf("\n### [%s]님의 회원 탈퇴가 완료되었습니다.\n", user.getUserName());
        } else {
            // 비밀번호나 아이디가 일치하지 않으면
            System.out.println("\n### 아이디나 비밀번호가 잘못되었습니다.");
        }
    }
}
