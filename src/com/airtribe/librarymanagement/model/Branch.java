package com.airtribe.librarymanagement.model;

import java.io.Serializable;
import java.util.*;

public class Branch implements Serializable {
    private String branchId;
    private String branchName;
    private String location;
    private String contactNumber;
    private Map<String, Book> books;

    public Branch(String branchId, String branchName, String location, String contactNumber) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.location = location;
        this.contactNumber = contactNumber;
        this.books = new HashMap<>();
    }

    public String getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Map<String, Book> getBooks() {
        return new HashMap<>(books);
    }

    public void addBook(Book book) {
        if (books.containsKey(book.getIsbn())) {
            books.get(book.getIsbn()).incrementQuantity();
        } else {
            books.put(book.getIsbn(), book);
        }
    }

    public void removeBook(String isbn) {
        Book book = books.get(isbn);
        if (book != null) {
            book.decrementQuantity();
            if (book.getQuantity() <= 0) {
                books.remove(isbn);
            }
        }
    }

    public Book getBook(String isbn) {
        return books.get(isbn);
    }

    public int getTotalBooks() {
        return books.values().stream().mapToInt(Book::getQuantity).sum();
    }

    public int getAvailableBooks() {
        return (int) books.values().stream()
                .filter(Book::isAvailable)
                .mapToInt(Book::getQuantity)
                .sum();
    }

    @Override
    public String toString() {
        return "Branch{" +
                "branchId='" + branchId + '\'' +
                ", branchName='" + branchName + '\'' +
                ", location='" + location + '\'' +
                ", totalBooks=" + getTotalBooks() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return Objects.equals(branchId, branch.branchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(branchId);
    }
}
