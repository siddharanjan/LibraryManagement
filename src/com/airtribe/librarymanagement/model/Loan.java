package com.airtribe.librarymanagement.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Loan implements Serializable {
    private String loanId;
    private String patronId;
    private String isbn;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus status;

    public enum LoanStatus {
        ACTIVE, RETURNED, OVERDUE
    }

    public Loan(String loanId, String patronId, String isbn, int borrowDays) {
        this.loanId = loanId;
        this.patronId = patronId;
        this.isbn = isbn;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(borrowDays);
        this.status = LoanStatus.ACTIVE;
    }

    public String getLoanId() {
        return loanId;
    }

    public String getPatronId() {
        return patronId;
    }

    public String getIsbn() {
        return isbn;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
        this.status = LoanStatus.RETURNED;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public boolean isOverdue() {
        return status == LoanStatus.ACTIVE && LocalDate.now().isAfter(dueDate);
    }

    public long getOverdueDays() {
        if (isOverdue()) {
            return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "loanId='" + loanId + '\'' +
                ", patronId='" + patronId + '\'' +
                ", isbn='" + isbn + '\'' +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return Objects.equals(loanId, loan.loanId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId);
    }
}
