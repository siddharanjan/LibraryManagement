package com.airtribe.librarymanagement.pattern;

import com.airtribe.librarymanagement.model.Patron;
import java.util.UUID;

public class PatronFactory {
    public static Patron createPatron(String name, String email, String phoneNumber) {
        if (name == null || name.isEmpty() || email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Name and Email cannot be null or empty");
        }
        String patronId = "P" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new Patron(patronId, name, email, phoneNumber);
    }

    public static Patron createPatronWithId(String patronId, String name, String email, String phoneNumber) {
        if (patronId == null || patronId.isEmpty()) {
            throw new IllegalArgumentException("Patron ID cannot be null or empty");
        }
        return new Patron(patronId, name, email, phoneNumber);
    }
}
