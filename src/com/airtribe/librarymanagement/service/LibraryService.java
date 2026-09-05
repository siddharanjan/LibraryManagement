package com.airtribe.librarymanagement.service;

import com.airtribe.librarymanagement.exception.LibraryException;
import com.airtribe.librarymanagement.model.Book;
import com.airtribe.librarymanagement.model.Patron;
import com.airtribe.librarymanagement.pattern.BookFactory;
import com.airtribe.librarymanagement.pattern.PatronFactory;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LibraryService {
    private static final Logger logger = Logger.getLogger(LibraryService.class.getName());
    private Map<String, Book> bookInventory;
    private Map<String, Patron> patrons;

    public LibraryService() {
        this.bookInventory = new HashMap<>();
        this.patrons = new HashMap<>();
    }

    public void addBook(String isbn, String title, String author, int publicationYear, String genre, int quantity) throws LibraryException {
        try {
            Book book = BookFactory.createBook(isbn, title, author, publicationYear, genre, quantity);
            if (bookInventory.containsKey(isbn)) {
                bookInventory.get(isbn).incrementQuantity();
                logger.info("Book quantity updated: " + isbn);
            } else {
                bookInventory.put(isbn, book);
                logger.info("Book added: " + isbn);
            }
        } catch (Exception e) {
            throw new LibraryException("Failed to add book: " + e.getMessage(), e);
        }
    }

    public void removeBook(String isbn, int quantity) throws LibraryException {
        Book book = bookInventory.get(isbn);
        if (book == null) {
            throw new LibraryException("Book not found: " + isbn);
        }
        if (book.getQuantity() < quantity) {
            throw new LibraryException("Insufficient quantity to remove");
        }
        book.setQuantity(book.getQuantity() - quantity);
        if (book.getQuantity() <= 0) {
            bookInventory.remove(isbn);
        }
        logger.info("Book removed: " + isbn);
    }

    public void updateBook(String isbn, String title, String author, String genre) throws LibraryException {
        Book book = bookInventory.get(isbn);
        if (book == null) {
            throw new LibraryException("Book not found: " + isbn);
        }
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        logger.info("Book updated: " + isbn);
    }

    public Book searchByISBN(String isbn) {
        return bookInventory.get(isbn);
    }

    public List<Book> searchByTitle(String title) {
        return bookInventory.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchByAuthor(String author) {
        return bookInventory.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchByGenre(String genre) {
        return bookInventory.values().stream()
                .filter(book -> book.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    public void addPatron(String name, String email, String phoneNumber) throws LibraryException {
        try {
            Patron patron = PatronFactory.createPatron(name, email, phoneNumber);
            patrons.put(patron.getPatronId(), patron);
            logger.info("Patron added: " + patron.getPatronId());
        } catch (Exception e) {
            throw new LibraryException("Failed to add patron: " + e.getMessage(), e);
        }
    }

    public void removePatron(String patronId) throws LibraryException {
        if (!patrons.containsKey(patronId)) {
            throw new LibraryException("Patron not found: " + patronId);
        }
        patrons.remove(patronId);
        logger.info("Patron removed: " + patronId);
    }

    public void updatePatron(String patronId, String name, String email, String phoneNumber) throws LibraryException {
        Patron patron = patrons.get(patronId);
        if (patron == null) {
            throw new LibraryException("Patron not found: " + patronId);
        }
        patron.setName(name);
        patron.setEmail(email);
        patron.setPhoneNumber(phoneNumber);
        logger.info("Patron updated: " + patronId);
    }

    public Patron getPatron(String patronId) {
        return patrons.get(patronId);
    }

    public List<Book> getAvailableBooks() {
        return bookInventory.values().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    public Map<String, Book> getAllBooks() {
        return new HashMap<>(bookInventory);
    }

    public Map<String, Patron> getAllPatrons() {
        return new HashMap<>(patrons);
    }

    public int getTotalBooks() {
        return bookInventory.values().stream().mapToInt(Book::getQuantity).sum();
    }

    public int getAvailableBooksCount() {
        return getAvailableBooks().size();
    }
}
