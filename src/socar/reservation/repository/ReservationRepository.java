package socar.reservation.repository;

import socar.reservation.domain.ReservationObject;
import socar.jdbc.DBConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationRepository {

    public void save(ReservationObject r) {
        String sql = "INSERT INTO RESERVATIONS (\"reservation_id\", \"user_id\", \"car_id\", \"start_date\", \"end_date\", \"total_fee\", \"is_cancelled\", \"is_returned\") " +
                "VALUES (RESERVATION_SEQ.NEXTVAL, ?, ?, ?, ?, ?, 'N', 'N')";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, r.getUserId());
            pstmt.setInt(2, r.getCarId());
            pstmt.setDate(3, Date.valueOf(r.getStartDate()));
            pstmt.setDate(4, Date.valueOf(r.getEndDate()));
            pstmt.setLong(5, r.getTotalFee());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                conn.commit();  // 데이터 저장을 확정
            } else {
                conn.rollback();  // 실패 시 롤백
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> findAvailableCars(LocalDate start, LocalDate end) {
        List<Map<String, Object>> list = new ArrayList<>();

        String sql = """
        SELECT "car_id", "car_type", "daily_fee"
        FROM CARS
        WHERE "is_active" = 'Y'
        AND "car_id" NOT IN (
            SELECT "car_id"
            FROM RESERVATIONS
            WHERE "is_cancelled" = 'N'
              AND (
                ("start_date" <= ? AND "end_date" >= ?) OR
                ("start_date" <= ? AND "end_date" >= ?) OR
                ("start_date" >= ? AND "end_date" <= ?)
              )
        )
        ORDER BY "car_id"
        """;

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(start));
            pstmt.setDate(2, Date.valueOf(start));
            pstmt.setDate(3, Date.valueOf(end));
            pstmt.setDate(4, Date.valueOf(end));
            pstmt.setDate(5, Date.valueOf(start));
            pstmt.setDate(6, Date.valueOf(end));

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("carId", rs.getInt("car_id"));
                map.put("carType", rs.getString("car_type"));
                map.put("dailyFee", rs.getLong("daily_fee"));
                list.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public boolean cancelReservation(int reservationId, String userId) {
        String sql = "UPDATE RESERVATIONS SET \"is_cancelled\" = 'Y', \"is_returned\" = 'Y' WHERE \"reservation_id\" = ? AND \"user_id\" = ? AND \"is_cancelled\" = 'N' AND \"is_returned\" = 'N'";

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

    public List<ReservationObject> findByUser(String userId) {
        List<ReservationObject> list = new ArrayList<>();
        String sql = "SELECT \"reservation_id\", \"user_id\",\"car_id\", \"start_date\", \"end_date\", \"total_fee\", \"is_cancelled\", \"is_returned\" " +
                "FROM RESERVATIONS WHERE \"user_id\" = ? ORDER BY \"start_date\" DESC";

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
                r.setCancelled("Y".equals(rs.getString("is_cancelled")));
                r.setReturned("Y".equals(rs.getString("is_returned"))); // ✅ DB에서 가져온 반납 여부 설정
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 예약 ID로 예약을 조회하는 메서드 추가
    public ReservationObject findById(int reservationId) {
        String sql = "SELECT \"reservation_id\", \"user_id\", \"car_id\", \"start_date\", \"end_date\", \"total_fee\", \"is_cancelled\", \"is_returned\" " +
                "FROM RESERVATIONS WHERE \"reservation_id\" = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                ReservationObject r = new ReservationObject(
                        rs.getString("user_id"),
                        rs.getInt("car_id"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getLong("total_fee")
                );
                r.setReservationId(rs.getInt("reservation_id"));
                r.setCancelled("Y".equals(rs.getString("is_cancelled")));
                r.setReturned("Y".equals(rs.getString("is_returned")));
                return r;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;  // 예약이 없으면 null 반환
    }

    public List<ReservationObject> findAll() {
        List<ReservationObject> list = new ArrayList<>();
        String sql = "SELECT \"reservation_id\", \"user_id\", \"car_id\", \"start_date\", \"end_date\", \"total_fee\", \"is_cancelled\", \"is_returned\" " +
                "FROM RESERVATIONS ORDER BY \"start_date\" DESC";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ReservationObject r = new ReservationObject(
                        rs.getString("user_id"),
                        rs.getInt("car_id"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getLong("total_fee")
                );
                r.setReservationId(rs.getInt("reservation_id"));
                r.setCancelled("Y".equals(rs.getString("is_cancelled")));
                r.setReturned("Y".equals(rs.getString("is_returned")));
                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
