package com.airtribe.librarymanagement.pattern;

import com.airtribe.librarymanagement.model.Book;
import com.airtribe.librarymanagement.model.Patron;
import java.util.List;

public interface RecommendationStrategy {
    List<Book> getRecommendations(Patron patron, List<Book> availableBooks, int count);
}
