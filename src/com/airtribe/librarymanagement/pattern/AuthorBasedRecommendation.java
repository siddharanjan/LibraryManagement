package com.airtribe.librarymanagement.pattern;

import com.airtribe.librarymanagement.model.Book;
import com.airtribe.librarymanagement.model.Loan;
import com.airtribe.librarymanagement.model.Patron;
import com.airtribe.librarymanagement.service.LibraryService;
import java.util.*;
import java.util.stream.Collectors;

public class AuthorBasedRecommendation implements RecommendationStrategy {
    private Map<String, Integer> authorFrequency;
    private LibraryService libraryService;

    public AuthorBasedRecommendation(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @Override
    public List<Book> getRecommendations(Patron patron, List<Book> availableBooks, int count) {
        calculateAuthorFrequency(patron);

        if (authorFrequency.isEmpty()) {
            return availableBooks.stream()
                    .limit(count)
                    .collect(Collectors.toList());
        }

        return availableBooks.stream()
                .sorted((b1, b2) -> Integer.compare(
                        authorFrequency.getOrDefault(b2.getAuthor(), 0),
                        authorFrequency.getOrDefault(b1.getAuthor(), 0)))
                .limit(count)
                .collect(Collectors.toList());
    }

    private void calculateAuthorFrequency(Patron patron) {
        authorFrequency = new HashMap<>();
        for (Loan loan : patron.getBorrowingHistory()) {
            Book book = libraryService.searchByISBN(loan.getIsbn());
            if (book != null) {
                String author = book.getAuthor();
                authorFrequency.put(author, authorFrequency.getOrDefault(author, 0) + 1);
            }
        }
    }
}
