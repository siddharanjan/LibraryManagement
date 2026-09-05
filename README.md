# Library Management System

A complete Java-based Library Management System with core features and advanced extensions.

## Features

### Core Requirements
- **Book Management** - Add, update, remove, and search books by title, author, ISBN, or genre
- **Patron Management** - Register patrons and track their borrowing history
- **Lending Process** - Checkout and return books with due date tracking
- **Inventory Management** - Track available and borrowed book quantities

### Optional Extensions
- **Multi-Branch Support** - Manage multiple library branches and transfer books between them
- **Reservation System** - Patrons can reserve unavailable books with automatic notifications
- **Recommendation System** - Get book recommendations based on patron preferences

## Project Structure

```
src/com/airtribe/librarymanagement/
├── model/              # Domain models (Book, Patron, Loan, Branch, Reservation)
├── service/            # Business logic (Library, Loan, Branch, Reservation, Recommendation)
├── pattern/            # Design patterns (Factory, Observer, Strategy)
├── exception/          # Custom exceptions
└── LibraryManagementApp.java  # Main application with demo
```

## Design Patterns

1. **Factory Pattern** - `BookFactory` and `PatronFactory` for creating objects with validation
2. **Observer Pattern** - `NotificationService` for sending reservation notifications
3. **Strategy Pattern** - `RecommendationStrategy` for flexible recommendation algorithms


## Usage Examples

### Add Books
```java
libraryService.addBook("ISBN001", "The Great Gatsby", "F. Scott Fitzgerald", 1925, "Fiction", 5);
```

### Register Patron
```java
libraryService.addPatron("John Doe", "john@example.com", "123-456-7890");
```

### Search Books
```java
List<Book> books = libraryService.searchByAuthor("F. Scott Fitzgerald");
Book book = libraryService.searchByISBN("ISBN001");
List<Book> fictionBooks = libraryService.searchByGenre("Fiction");
```

### Checkout Book
```java
Loan loan = loanService.checkoutBook(patronId, "ISBN001", 14);
```

### Return Book
```java
loanService.returnBook(loan.getLoanId());
```

### Make Reservation
```java
String reservationId = reservationService.makeReservation(patronId, "ISBN001");
reservationService.notifyReservationFulfilled("ISBN001");
```

### Manage Branches
```java
branchService.createBranch("B001", "Downtown Branch", "123 Main St", "555-0001");
branchService.addBookToBranch("B001", book);
branchService.transferBook("B001", "B002", "ISBN001", 2);
```

### Get Recommendations
```java
patron.addPreference("Fiction");
List<Book> recommendations = recommendationService.getRecommendations(patronId, 5);
```

## Key Classes

| Class | Purpose |
|-------|---------|
| `Book` | Represents a book with ISBN, title, author, quantity |
| `Patron` | Represents a library member with preferences |
| `Loan` | Tracks book checkouts and returns |
| `Branch` | Represents a library branch with inventory |
| `Reservation` | Represents a book reservation with priority |
| `LibraryService` | Manages books and patrons |
| `LoanService` | Handles checkout and return operations |
| `BranchService` | Manages library branches |
| `ReservationService` | Manages reservations and notifications |
| `RecommendationService` | Generates book recommendations |

## Technology Stack

- **Language:** Java 11+
- **Collections:** HashMap, ArrayList, Queue, Set
- **Logging:** Java Util Logging
- **Exception Handling:** Custom LibraryException

## Implementation Highlights

✅ **SOLID Principles** - Single responsibility, open/closed, proper abstraction  
✅ **Design Patterns** - Factory, Observer, Strategy patterns implemented  
✅ **Error Handling** - Custom exceptions for library-specific errors  
✅ **Comprehensive Logging** - All major operations logged  
✅ **Clean Code** - Well-organized, readable, properly documented  

## Demo Application

Run `LibraryManagementApp.java` to see a complete demonstration of all features:
- Adding books and patrons
- Searching functionality
- Book checkout and return
- Making and fulfilling reservations
- Multi-branch management
- Book recommendations

