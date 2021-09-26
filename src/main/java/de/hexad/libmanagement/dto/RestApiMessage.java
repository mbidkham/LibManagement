package de.hexad.libmanagement.dto;

public enum RestApiMessage {
    USER_NOT_FOUND("User doesn't exist!"),
    BOOK_NOT_FOUND("Book doesnt' exist"),
    BOOK_IS_BORROWED("This book has borrowed by another, sorry."),
    LIMIT_BORROW_VALUE("You can't borrow more than 2 books.");


    private final String value;

    RestApiMessage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
