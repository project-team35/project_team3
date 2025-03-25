package socar.reservation.repository;

import socar.reservation.domain.ReservationObject;
import socar.jdbc.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {

    public void save(ReservationObject r) {
        String sql = "INSERT INTO RESERVATIONS (reservation_id, user_id, car_id, start_date, end_date, total_fee, is_cancelled, is_returned, mileage) " +
                "VALUES (RESERVATION_SEQ.NEXTVAL, ?, ?, ?, ?, ?, 'N', 'N', 0)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, r.getUserId());
            pstmt.setInt(2, r.getCarId());
            pstmt.setDate(3, Date.valueOf(r.getStartDate()));
            pstmt.setDate(4, Date.valueOf(r.getEndDate()));
            pstmt.setLong(5, r.getTotalFee());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean cancelReservation(int reservationId, String userId) {
        String sql = "UPDATE RESERVATIONS SET is_cancelled = 'Y' WHERE reservation_id = ? AND user_id = ? AND is_cancelled = 'N'";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.setString(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasActiveReservation(String userId) {
        String sql = "SELECT COUNT(*) FROM RESERVATIONS WHERE user_id = ? AND is_cancelled = 'N' AND end_date >= SYSDATE";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ReservationObject> findByUser(String userId) {
        List<ReservationObject> list = new ArrayList<>();
        String sql = "SELECT * FROM RESERVATIONS WHERE user_id = ? ORDER BY start_date DESC";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ReservationObject r = new ReservationObject(
                        rs.getString("user_id"),
                        rs.getInt("car_id"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getLong("total_fee")
                );
                r.setReservationId(rs.getInt("reservation_id"));
                if ("Y".equals(rs.getString("is_cancelled"))) r.cancel();
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
