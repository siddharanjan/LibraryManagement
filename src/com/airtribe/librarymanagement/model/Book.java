package com.airtribe.librarymanagement.model;

import java.io.Serializable;
import java.util.Objects;

public class Book implements Serializable {
    private String isbn;
    private String title;
    private String author;
    private int publicationYear;
    private String genre;
    private boolean isAvailable;
    private int quantity;

    public Book(String isbn, String title, String author, int publicationYear, String genre, int quantity) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.quantity = quantity;
        this.isAvailable = quantity > 0;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.isAvailable = quantity > 0;
    }

    public void decrementQuantity() {
        if (quantity > 0) {
            quantity--;
            this.isAvailable = quantity > 0;
        }
    }

    public void incrementQuantity() {
        quantity++;
        this.isAvailable = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationYear=" + publicationYear +
                ", genre='" + genre + '\'' +
                ", quantity=" + quantity +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
