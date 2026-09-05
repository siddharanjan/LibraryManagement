package com.airtribe.librarymanagement.pattern;

import com.airtribe.librarymanagement.model.Book;

public class BookFactory {
    public static Book createBook(String isbn, String title, String author,
                                   int publicationYear, String genre, int quantity) {
        if (isbn == null || isbn.isEmpty() || title == null || title.isEmpty()) {
            throw new IllegalArgumentException("ISBN and Title cannot be null or empty");
        }
        return new Book(isbn, title, author, publicationYear, genre, quantity);
    }

    public static Book createBookWithDefaults(String isbn, String title, String author) {
        return createBook(isbn, title, author, java.time.Year.now().getValue(), "Unknown", 1);
    }
}
