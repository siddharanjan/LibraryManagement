package com.airtribe.librarymanagement.service;

import com.airtribe.librarymanagement.exception.LibraryException;
import com.airtribe.librarymanagement.model.Book;
import com.airtribe.librarymanagement.model.Loan;
import com.airtribe.librarymanagement.model.Patron;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LoanService {
    private static final Logger logger = Logger.getLogger(LoanService.class.getName());
    private static final int DEFAULT_BORROW_DAYS = 14;
    private List<Loan> loans;
    private LibraryService libraryService;

    public LoanService(LibraryService libraryService) {
        this.libraryService = libraryService;
        this.loans = new ArrayList<>();
    }

    public Loan checkoutBook(String patronId, String isbn) throws LibraryException {
        return checkoutBook(patronId, isbn, DEFAULT_BORROW_DAYS);
    }

    public Loan checkoutBook(String patronId, String isbn, int borrowDays) throws LibraryException {
        Patron patron = libraryService.getPatron(patronId);
        if (patron == null) {
            throw new LibraryException("Patron not found: " + patronId);
        }

        Book book = libraryService.searchByISBN(isbn);
        if (book == null) {
            throw new LibraryException("Book not found: " + isbn);
        }

        if (!book.isAvailable()) {
            throw new LibraryException("Book not available: " + isbn);
        }

        String loanId = "L" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Loan loan = new Loan(loanId, patronId, isbn, borrowDays);

        book.decrementQuantity();
        loans.add(loan);
        patron.addBorrowingHistory(loan);

        logger.info("Book checked out: " + isbn + " by patron: " + patronId);
        return loan;
    }

    public void returnBook(String loanId) throws LibraryException {
        Loan loan = loans.stream()
                .filter(l -> l.getLoanId().equals(loanId))
                .findFirst()
                .orElse(null);

        if (loan == null) {
            throw new LibraryException("Loan not found: " + loanId);
        }

        if (loan.getStatus() == Loan.LoanStatus.RETURNED) {
            throw new LibraryException("Book already returned: " + loanId);
        }

        Book book = libraryService.searchByISBN(loan.getIsbn());
        if (book != null) {
            book.incrementQuantity();
        }

        loan.setReturnDate(LocalDate.now());

        if (loan.isOverdue()) {
            logger.warning("Book returned late - Loan ID: " + loanId +
                         ", Overdue days: " + loan.getOverdueDays());
        }

        logger.info("Book returned: " + loan.getIsbn());
    }

    public Loan getLoan(String loanId) {
        return loans.stream()
                .filter(l -> l.getLoanId().equals(loanId))
                .findFirst()
                .orElse(null);
    }

    public List<Loan> getActiveLoansByPatron(String patronId) {
        return loans.stream()
                .filter(l -> l.getPatronId().equals(patronId) &&
                           l.getStatus() == Loan.LoanStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    public List<Loan> getOverdueLoans() {
        return loans.stream()
                .filter(Loan::isOverdue)
                .collect(Collectors.toList());
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    public long getOverdueDays(String loanId) throws LibraryException {
        Loan loan = getLoan(loanId);
        if (loan == null) {
            throw new LibraryException("Loan not found: " + loanId);
        }
        return loan.getOverdueDays();
    }
}
