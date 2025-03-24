package socar.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// 데이터베이스 연결 및 관리를 위한 공통 로직을 모아놓은 클래스
public class DBConnectionManager {

    // 오라클 JDBC 연결 정보
    private static final String DRIVER = "oracle.socar.jdbc.driver.OracleDriver";
    private static final String URL = "socar.jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "socar";
    private static final String PASSWORD = "socar";

    // 정적 초기화자를 사용하여 드라이버 로드
    static {
        try {
            Class.forName(DRIVER);
            System.out.println("JDBC 드라이버 강제 구동 완료!");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC 드라이버 로드 실패!");
            e.printStackTrace();
        }
    }

    // 데이터베이스 접속 객체를 리턴해주는 메서드
    // 데이터베이스 접속 객체 Connection을 리턴, 예외를 던짐.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
