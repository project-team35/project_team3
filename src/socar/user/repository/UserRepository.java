package socar.user.repository;

import socar.jdbc.DBConnectionManager;
import socar.user.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    // 회원 추가 기능
    public void addUser(User user) {
        String sql = "INSERT INTO users VALUES(?,?,?,?,?,?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getUserName());
            pstmt.setString(3, user.getPassword());
            pstmt.setInt(4, user.getLicenseNum());
            pstmt.setString(5, String.valueOf(user.getIsAdmin()));  // isAdmin 값을 'Y' 또는 'N'으로 저장
            pstmt.setString(6, "Y");  // 기본적으로 active는 'Y'로 설정

            // SQL 실행
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 아이디 중복 체크 (활성/비활성 둘다 검사)
    public boolean isUserIdExists(String userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE \"user_id\" = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // 존재하면 true 반환
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // 존재하지 않으면 false 반환
    }

    // 특정 사용자가 활성화 상태인지 확인
    public boolean isUserActive(String userId) {
        String sql = "SELECT \"active\" FROM users WHERE \"user_id\" = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return "Y".equals(rs.getString("active")); // active가 'Y'인 경우 true 반환
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // active가 'N'이거나 존재하지 않는 경우 false 반환
    }


    // 회원 탈퇴 (비활성화 처리)
    public void deactivateUser(String userId) {
        String sql = "UPDATE users SET \"active\" = 'N' WHERE \"user_id\" = ?";  // active 컬럼을 'N'으로 설정

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            // 쿼리 실행
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // 사용자 정보 조회 ( 회원탈퇴, 로그인)
    public User findUserByIdAndPassword(String userId, String password) {
        User user = null;
        String sql = "SELECT * FROM users WHERE \"user_id\" = ? AND \"password\" = ? AND \"active\" = 'Y'";  // \" 소문자 

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, password);

            // 쿼리 실행
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getString("user_id"),
                        rs.getString("user_name"),
                        rs.getString("password"),
                        rs.getInt("license_num"),
                        rs.getString("is_admin").charAt(0)  // 'Y' 또는 'N'을 char로 변환
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
