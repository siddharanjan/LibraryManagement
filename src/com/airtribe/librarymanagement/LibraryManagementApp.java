package com.airtribe.librarymanagement;

import com.airtribe.librarymanagement.exception.LibraryException;
import com.airtribe.librarymanagement.model.Book;
import com.airtribe.librarymanagement.model.Loan;
import com.airtribe.librarymanagement.model.Patron;
import com.airtribe.librarymanagement.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LibraryManagementApp {
    private static final Logger logger = Logger.getLogger(LibraryManagementApp.class.getName());

    public static void main(String[] args) {
        try {
            logger.info("=== Library Management System Started ===");

            // Initialize services
            LibraryService libraryService = new LibraryService();
            LoanService loanService = new LoanService(libraryService);
            BranchService branchService = new BranchService();
            ReservationService reservationService = new ReservationService();
            RecommendationService recommendationService = new RecommendationService(libraryService);

            // Add books to library
            logger.info("\n--- Adding Books ---");
            libraryService.addBook("ISBN001", "The Slap", "Christos Tsiolkas", 2008, "Fiction", 5);
            libraryService.addBook("ISBN002", "The Narrow Road to the Deep North", "Richard Flanagan", 2013, "Fiction", 3);
            libraryService.addBook("ISBN003", "Cloudstreet", "Tim Winton", 1991, "Fiction", 4);
            libraryService.addBook("ISBN004", "The Thorn Birds", "Colleen McCullough", 1977, "Romance", 2);
            libraryService.addBook("ISBN005", "The Secret River", "Kate Grenville", 2005, "Historical Fiction", 3);
            logger.info("Books added successfully");

            // Add patrons
            logger.info("\n--- Adding Patrons ---");
            libraryService.addPatron("James Mitchell", "james.mitchell@example.com.au", "02 9555 1234");
            libraryService.addPatron("Emma Taylor", "emma.taylor@example.com.au", "03 8765 4321");
            libraryService.addPatron("Liam Thompson", "liam.thompson@example.com.au", "07 3555 9876");
            logger.info("Patrons added successfully");

            // Get actual patron IDs
            Map<String, Patron> allPatrons = libraryService.getAllPatrons();
            ArrayList<String> patronIds = new ArrayList<>(allPatrons.keySet());
            String patronId1 = patronIds.get(0);
            String patronId2 = patronIds.get(1);

            // Display available books
            logger.info("\n--- Available Books ---");
            libraryService.getAvailableBooks().forEach(book ->
                logger.info("  " + book.getTitle() + " by " + book.getAuthor() +
                           " (Available: " + book.getQuantity() + ")"));

            // Search functionality
            logger.info("\n--- Search Functionality ---");
            logger.info("Searching for books by author 'Tim Winton':");
            libraryService.searchByAuthor("Tim Winton").forEach(book ->
                logger.info("  Found: " + book.getTitle()));

            logger.info("Searching for books by genre 'Fiction':");
            libraryService.searchByGenre("Fiction").forEach(book ->
                logger.info("  Found: " + book.getTitle()));

            // Checkout books
            logger.info("\n--- Checkout Books ---");
            Loan loan1 = loanService.checkoutBook(patronId1, "ISBN001", 14);
            logger.info("Checked out: " + loan1.getLoanId());

            Loan loan2 = loanService.checkoutBook(patronId1, "ISBN003", 14);
            logger.info("Checked out: " + loan2.getLoanId());

            Loan loan3 = loanService.checkoutBook(patronId2, "ISBN002", 14);
            logger.info("Checked out: " + loan3.getLoanId());

            // Make reservations
            logger.info("\n--- Make Reservations ---");
            String reservation1 = reservationService.makeReservation(patronId2, "ISBN001");
            logger.info("Reservation created: " + reservation1);

            // Display patron info
            logger.info("\n--- Patron Information ---");
            libraryService.getAllPatrons().values().forEach(patron -> {
                logger.info("Patron: " + patron.getName() + " (" + patron.getPatronId() + ")");
                List<Loan> activeLoans = loanService.getActiveLoansByPatron(patron.getPatronId());
                logger.info("  Active Loans: " + activeLoans.size());
            });

            // Return book
            logger.info("\n--- Return Book ---");
            loanService.returnBook(loan1.getLoanId());
            logger.info("Book returned successfully");

            // Notify reserved patrons
            logger.info("\n--- Fulfill Reservations ---");
            reservationService.notifyReservationFulfilled("ISBN001");
            List<String> notifications = reservationService.getPatronNotifications(patronId2);
            logger.info("Notifications for patron " + patronId2 + ": " + notifications.size());
            if (!notifications.isEmpty()) {
                notifications.forEach(notif -> logger.info("  " + notif));
            }

            // Multi-branch support
            logger.info("\n--- Multi-Branch Management ---");
            branchService.createBranch("B001", "Sydney Central Library", "456 George Street, Sydney NSW 2000", "02 9273 3645");
            branchService.createBranch("B002", "Melbourne CBD Branch", "328 Swanston Street, Melbourne VIC 3000", "03 9348 4777");

            Book book = libraryService.searchByISBN("ISBN001");
            if (book != null) {
                branchService.addBookToBranch("B001", book);
                branchService.addBookToBranch("B002", new Book("ISBN001", book.getTitle(),
                        book.getAuthor(), book.getPublicationYear(), book.getGenre(), 2));
                logger.info("Books added to branches");
            }

            // Transfer books between branches
            logger.info("\n--- Transfer Books Between Branches ---");
            branchService.transferBook("B001", "B002", "ISBN001", 1);
            logger.info("Book transferred successfully");

            // Recommendations
            logger.info("\n--- Book Recommendations ---");
            Patron patron = libraryService.getPatron(patronId1);
            if (patron != null) {
                patron.addPreference("Fiction");
                patron.addPreference("Dystopian");
                List<Book> recommendations = recommendationService.getTopRecommendations(patronId1);
                logger.info("Recommendations for " + patron.getName() + ":");
                recommendations.forEach(rec ->
                    logger.info("  " + rec.getTitle() + " (" + rec.getGenre() + ")"));
            }

            // Summary
            logger.info("\n--- Library Summary ---");
            logger.info("Total Books: " + libraryService.getTotalBooks());
            logger.info("Available Books: " + libraryService.getAvailableBooksCount());
            logger.info("Total Patrons: " + libraryService.getAllPatrons().size());
            logger.info("Total Loans: " + loanService.getAllLoans().size());

            logger.info("\n=== Library Management System Ended ===");

        } catch (LibraryException e) {
            logger.severe("Library Exception: " + e.getMessage());
        }
    }
}
