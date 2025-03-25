package socar.reservation.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationPolicy {

    // 예약 일수 계산
    public static long calculateDays(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    // 총 결제 금액 계산
    public static long calculateTotalFee(long dailyFee, long days) {
        return dailyFee * days;
    }

    // 반납 날짜
    public static boolean isReturned(LocalDate endDate) {
        return LocalDate.now().isAfter(endDate);
    }
}

