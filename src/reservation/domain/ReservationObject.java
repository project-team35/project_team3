package reservation.domain;

import java.time.LocalDate;

public class ReservationObject {
    private int reservationId;
    private String userId;
    private int carId;
    private LocalDate startDate;
    private LocalDate endDate;
    private long totalFee;
    private boolean isCancelled;

    public ReservationObject(String userId, int carId, LocalDate startDate, LocalDate endDate, long totalFee) {
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalFee = totalFee;
        this.isCancelled = false;
    }


    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getUserId() {
        return userId;
    }

    public int getCarId() {
        return carId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public long getTotalFee() {
        return totalFee;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }
}
