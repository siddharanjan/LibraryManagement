package com.airtribe.librarymanagement.service;

import com.airtribe.librarymanagement.exception.LibraryException;
import com.airtribe.librarymanagement.model.Book;
import com.airtribe.librarymanagement.model.Branch;

import java.util.*;
import java.util.logging.Logger;

public class BranchService {
    private static final Logger logger = Logger.getLogger(BranchService.class.getName());
    private Map<String, Branch> branches;

    public BranchService() {
        this.branches = new HashMap<>();
    }

    public void createBranch(String branchId, String branchName, String location, String contactNumber) throws LibraryException {
        if (branches.containsKey(branchId)) {
            throw new LibraryException("Branch already exists: " + branchId);
        }
        Branch branch = new Branch(branchId, branchName, location, contactNumber);
        branches.put(branchId, branch);
        logger.info("Branch created: " + branchId);
    }

    public void addBookToBranch(String branchId, Book book) throws LibraryException {
        Branch branch = branches.get(branchId);
        if (branch == null) {
            throw new LibraryException("Branch not found: " + branchId);
        }
        branch.addBook(book);
        logger.info("Book added to branch: " + branchId + " - " + book.getIsbn());
    }

    public void removeBookFromBranch(String branchId, String isbn) throws LibraryException {
        Branch branch = branches.get(branchId);
        if (branch == null) {
            throw new LibraryException("Branch not found: " + branchId);
        }
        branch.removeBook(isbn);
        logger.info("Book removed from branch: " + branchId + " - " + isbn);
    }

    public void transferBook(String sourceBranchId, String destinationBranchId, String isbn, int quantity) throws LibraryException {
        Branch sourceBranch = branches.get(sourceBranchId);
        Branch destinationBranch = branches.get(destinationBranchId);

        if (sourceBranch == null) {
            throw new LibraryException("Source branch not found: " + sourceBranchId);
        }
        if (destinationBranch == null) {
            throw new LibraryException("Destination branch not found: " + destinationBranchId);
        }

        Book sourceBook = sourceBranch.getBook(isbn);
        if (sourceBook == null || sourceBook.getQuantity() < quantity) {
            throw new LibraryException("Insufficient books in source branch: " + sourceBranchId);
        }

        for (int i = 0; i < quantity; i++) {
            sourceBranch.removeBook(isbn);
        }

        Book destinationBook = new Book(sourceBook.getIsbn(), sourceBook.getTitle(),
                                       sourceBook.getAuthor(), sourceBook.getPublicationYear(),
                                       sourceBook.getGenre(), quantity);
        destinationBranch.addBook(destinationBook);

        logger.info("Book transferred: " + isbn + " from " + sourceBranchId + " to " + destinationBranchId);
    }

    public Branch getBranch(String branchId) {
        return branches.get(branchId);
    }

    public List<Branch> getAllBranches() {
        return new ArrayList<>(branches.values());
    }

    public Book searchBookInBranch(String branchId, String isbn) throws LibraryException {
        Branch branch = branches.get(branchId);
        if (branch == null) {
            throw new LibraryException("Branch not found: " + branchId);
        }
        return branch.getBook(isbn);
    }

    public List<String> findBranchesWithBook(String isbn) {
        List<String> branchIds = new ArrayList<>();
        for (Branch branch : branches.values()) {
            if (branch.getBook(isbn) != null) {
                branchIds.add(branch.getBranchId());
            }
        }
        return branchIds;
    }

    public void updateBranch(String branchId, String branchName, String location, String contactNumber) throws LibraryException {
        Branch branch = branches.get(branchId);
        if (branch == null) {
            throw new LibraryException("Branch not found: " + branchId);
        }
        branch.setBranchName(branchName);
        branch.setLocation(location);
        branch.setContactNumber(contactNumber);
        logger.info("Branch updated: " + branchId);
    }

    public void deleteBranch(String branchId) throws LibraryException {
        Branch branch = branches.get(branchId);
        if (branch == null) {
            throw new LibraryException("Branch not found: " + branchId);
        }
        if (!branch.getBooks().isEmpty()) {
            throw new LibraryException("Branch has books. Transfer or remove them first.");
        }
        branches.remove(branchId);
        logger.info("Branch deleted: " + branchId);
    }
}
