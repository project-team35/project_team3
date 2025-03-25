package reservation.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationPolicy {

    public static long calculateDays(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    public static long calculateTotalFee(long dailyFee, long days) {
        return dailyFee * days;
    }

    public static boolean isReturned(LocalDate endDate) {
        return LocalDate.now().isAfter(endDate);
    }
}

