package com.airtribe.librarymanagement.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Reservation implements Serializable, Comparable<Reservation> {
    private String reservationId;
    private String patronId;
    private String isbn;
    private LocalDate reservationDate;
    private ReservationStatus status;
    private int priority;

    public enum ReservationStatus {
        PENDING, FULFILLED, CANCELLED
    }

    public Reservation(String reservationId, String patronId, String isbn, int priority) {
        this.reservationId = reservationId;
        this.patronId = patronId;
        this.isbn = isbn;
        this.reservationDate = LocalDate.now();
        this.status = ReservationStatus.PENDING;
        this.priority = priority;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getPatronId() {
        return patronId;
    }

    public String getIsbn() {
        return isbn;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Reservation other) {
        if (this.priority != other.priority) {
            return Integer.compare(this.priority, other.priority);
        }
        return this.reservationDate.compareTo(other.reservationDate);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", patronId='" + patronId + '\'' +
                ", isbn='" + isbn + '\'' +
                ", reservationDate=" + reservationDate +
                ", status=" + status +
                ", priority=" + priority +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(reservationId, that.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }
}
