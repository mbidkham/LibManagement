package de.hexad.libmanagement.common;

import de.hexad.libmanagement.model.entity.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataUtil {

    public static final User user = new User();
    public static final Book borrowedBook = new Book();
    public static final PageImpl<Book> zeroBookResponse;
    public static final PageImpl<Book> existBookResponse;


    static {

        borrowedBook.setId(1);
        borrowedBook.setName("The Clown!");
        borrowedBook.setBorrowed(true);
        user.setId(1);
        user.setName("Mehraneh");
        user.setBorrowedBooks(new ArrayList<>(List.of(borrowedBook)));
        existBookResponse = new PageImpl<>(List.of(borrowedBook), PageRequest.of(0, 20), 1);
        zeroBookResponse = new PageImpl<>(Collections.emptyList(),
                PageRequest.of(0, 20),
                0);
    }
}
