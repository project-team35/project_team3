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
        String sql = "INSERT INTO users VALUES(?,?,?,?,?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getUserName());
            pstmt.setString(3, user.getPassword());
            pstmt.setInt(4, user.getLicenseNum());
            pstmt.setString(5, String.valueOf(user.getIsAdmin()));  // isAdmin 값을 'Y' 또는 'N'으로 저장

            // SQL 실행
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("저장된 비밀번호: " + user.getPassword());

    }

    public User findUserByIdAndPassword(String userId, String password) {
        User user = null;
        String sql = "SELECT * FROM users WHERE \"user_id\" = ? AND \"password\" = ?";
        // 백슬래시를 사용한 이유는 SQL 쿼리에서 컬럼명이나 문자열을 정확하게 구분하기 위해 따옴표로 감싸야 할 때 발생하는 문제를 해결하기 위해서 사용함 (안할시 쿼리문 실패)

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, password);  // 비밀번호도 함께 비교

            // 쿼리 실행
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("DB에서 가져온 비밀번호: " + rs.getString("password"));
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

    // 회원 삭제 (비활성화 처리)
    public void deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE \"user_id\" = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            // 쿼리 실행
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
