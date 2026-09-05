package com.airtribe.librarymanagement.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Patron implements Serializable {
    private String patronId;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate membershipDate;
    private List<Loan> borrowingHistory;
    private Set<String> preferences;

    public Patron(String patronId, String name, String email, String phoneNumber) {
        this.patronId = patronId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.membershipDate = LocalDate.now();
        this.borrowingHistory = new ArrayList<>();
        this.preferences = new HashSet<>();
    }

    public String getPatronId() {
        return patronId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public List<Loan> getBorrowingHistory() {
        return new ArrayList<>(borrowingHistory);
    }

    public void addBorrowingHistory(Loan loan) {
        borrowingHistory.add(loan);
    }

    public Set<String> getPreferences() {
        return new HashSet<>(preferences);
    }

    public void addPreference(String genre) {
        preferences.add(genre);
    }

    public void removePreference(String genre) {
        preferences.remove(genre);
    }

    @Override
    public String toString() {
        return "Patron{" +
                "patronId='" + patronId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", membershipDate=" + membershipDate +
                ", borrowingHistorySize=" + borrowingHistory.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patron patron = (Patron) o;
        return Objects.equals(patronId, patron.patronId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patronId);
    }
}
