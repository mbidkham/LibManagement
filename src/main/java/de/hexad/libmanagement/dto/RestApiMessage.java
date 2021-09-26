package de.hexad.libmanagement.dto;

public enum RestApiMessage {
    USER_NOT_FOUND("User doesn't exist!"),
    BOOK_NOT_FOUND("Book doesnt' exist"),
    BOOK_IS_BORROWED("This book has borrowed by another, sorry."),
    LIMIT_BORROW_VALUE("You can't borrow more than 2 books."),
    NOT_COPY_EXIST("Not enough copy of this book exists  now."),
    DUPLICATE_CLONE_OF_BOOK("You have borrowed one copy of this book before.");


    private final String value;

    RestApiMessage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
