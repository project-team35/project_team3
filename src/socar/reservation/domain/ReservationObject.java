package socar.reservation.domain;

import java.time.LocalDate;

public class ReservationObject {
    private int reservationId;
    private String userId;
    private int carId;
    private LocalDate startDate;
    private LocalDate endDate;
    private long totalFee;
    private boolean isCancelled;
    private boolean isReturned;

    public ReservationObject(String userId, int carId, LocalDate startDate, LocalDate endDate, long totalFee) {
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalFee = totalFee;
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

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setTotalFee(long totalFee) {
        this.totalFee = totalFee;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

}
