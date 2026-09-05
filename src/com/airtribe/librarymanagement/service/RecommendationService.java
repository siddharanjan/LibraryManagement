package com.airtribe.librarymanagement.service;

import com.airtribe.librarymanagement.exception.LibraryException;
import com.airtribe.librarymanagement.model.Book;
import com.airtribe.librarymanagement.model.Patron;
import com.airtribe.librarymanagement.pattern.RecommendationStrategy;
import com.airtribe.librarymanagement.pattern.GenreBasedRecommendation;

import java.util.List;
import java.util.logging.Logger;

public class RecommendationService {
    private static final Logger logger = Logger.getLogger(RecommendationService.class.getName());
    private LibraryService libraryService;
    private RecommendationStrategy strategy;

    public RecommendationService(LibraryService libraryService) {
        this.libraryService = libraryService;
        this.strategy = new GenreBasedRecommendation();
    }

    public void setRecommendationStrategy(RecommendationStrategy strategy) {
        this.strategy = strategy;
        logger.info("Recommendation strategy changed to: " + strategy.getClass().getSimpleName());
    }

    public List<Book> getRecommendations(String patronId, int count) throws LibraryException {
        Patron patron = libraryService.getPatron(patronId);
        if (patron == null) {
            throw new LibraryException("Patron not found: " + patronId);
        }

        List<Book> availableBooks = libraryService.getAvailableBooks();
        List<Book> recommendations = strategy.getRecommendations(patron, availableBooks, count);

        logger.info("Generated " + recommendations.size() + " recommendations for patron: " + patronId);
        return recommendations;
    }

    public List<Book> getTopRecommendations(String patronId) throws LibraryException {
        return getRecommendations(patronId, 5);
    }
}
