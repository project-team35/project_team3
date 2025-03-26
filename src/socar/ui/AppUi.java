package socar.ui;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AppUi {

    private static Scanner sc = new Scanner(System.in);

    // 사용자로부터 문자열 입력 받기
    public static String inputString(String message) {
        System.out.print(message);
        return sc.nextLine();
    }

    // 사용자로부터 정수 입력 받기
    public static int inputInteger(String message) {
        System.out.print(message);
        int num = 0;
        try {
            num = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("# 올바른 정수 입력값이 아닙니다!");
        } finally {
            sc.nextLine(); // try에서도(엔터 입력값), catch에서도(쓰레기 문자열 수거) nextLine()이 동작해야 함.
        }
        return num;
    }

    // 구분선 출력
    public static void makeLine() {
        System.out.println("===============================================");
    }

    // 시작 화면 출력 (로그인 상태에 따라 다른 메뉴)
    public static void startScreen(boolean isLoggedIn) {
        System.out.println("\n================ PlayCar 시스템 ================");

        if (isLoggedIn) {
            System.out.println("| 2. 차량 예약");
            System.out.println("| 3. 서비스 관리");
            System.out.println("| 4. 로그아웃");
        } else {
            System.out.println("| 1. 회원 가입 / 로그인");
        }
        System.out.println("| 5. 프로그램 종료");
        makeLine();
    }

    // 회원 관리 시스템 화면 출력
    public static void userManagementScreen() {
        System.out.println("\n========= 회원 관리 시스템 =========");
        System.out.println("### 1. 회원 가입");
        System.out.println("### 2. 로그인");
        System.out.println("### 3. 회원 탈퇴");
        System.out.println("### 4. 첫 화면으로 가기");
        makeLine();
    }

    // 차량 관리 시스템 화면 출력
    public static void carManagementScreen() {
        System.out.println("\n========= 차량 관리 =========");
        System.out.println("### 1. 차량 추가 ");
        System.out.println("### 2. 차량 비활성화");
        System.out.println("### 3. 첫 화면으로 가기");
        makeLine();
    }
}
