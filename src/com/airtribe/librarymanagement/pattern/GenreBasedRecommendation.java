package com.airtribe.librarymanagement.pattern;

import com.airtribe.librarymanagement.model.Book;
import com.airtribe.librarymanagement.model.Patron;
import java.util.*;
import java.util.stream.Collectors;

public class GenreBasedRecommendation implements RecommendationStrategy {
    @Override
    public List<Book> getRecommendations(Patron patron, List<Book> availableBooks, int count) {
        Set<String> preferences = patron.getPreferences();

        if (preferences.isEmpty()) {
            return availableBooks.stream()
                    .limit(count)
                    .collect(Collectors.toList());
        }

        return availableBooks.stream()
                .filter(book -> preferences.contains(book.getGenre()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
