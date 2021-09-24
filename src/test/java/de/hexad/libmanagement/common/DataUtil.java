package de.hexad.libmanagement.common;

import de.hexad.libmanagement.model.entity.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.Collections;
import java.util.List;

public class DataUtil {

    public static final User user = new User();
    public static final Book book = new Book();
    public static final Book notBorrowedBook = new Book();
    public static final PageImpl<Book> zeroBookResponse;
    public static final PageImpl<Book> existBookResponse;


    static {
        user.setId(1);
        user.setName("Mehraneh");
        book.setId(1);
        book.setName("The Clown!");
        book.setBorrowedUser(user);
        book.setBorrowed(true);
        existBookResponse = new PageImpl<>(List.of(book), PageRequest.of(0, 20), 1);
        zeroBookResponse = new PageImpl<>(Collections.emptyList(),
                PageRequest.of(0, 20),
                0);
        notBorrowedBook.setId(2);
        notBorrowedBook.setBorrowedUser(null);
        notBorrowedBook.setName("A Fraction of the whole!");

    }
}
