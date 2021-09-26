package de.hexad.libmanagement.book.service;


import de.hexad.libmanagement.dto.BorrowBookRequest;
import de.hexad.libmanagement.model.entity.Book;
import de.hexad.libmanagement.model.entity.User;
import de.hexad.libmanagement.model.repository.BookRepository;
import de.hexad.libmanagement.model.repository.UserRepository;
import de.hexad.libmanagement.service.BorrowBookService;
import de.hexad.libmanagement.service.ReturnBookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class ReturnBookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    private ReturnBookService returnBookService;

    private Book borrowedBook;
    private Book borrowedBook2;
    private User user;


    @BeforeEach
    void init() {

        borrowedBook = new Book();
        borrowedBook.setId(1);
        borrowedBook.setName("The Clown!");
        borrowedBook.setBorrowed(true);

        borrowedBook2 = new Book();
        borrowedBook2.setId(2);
        borrowedBook2.setName("Shining!");
        borrowedBook2.setBorrowed(true);

        user = new User();
        user.setId(1);
        user.setName("Mehraneh");
        ArrayList<Book> bookArrayList = new ArrayList<>();
        bookArrayList.add(borrowedBook);
        bookArrayList.add(borrowedBook2);
        user.setBorrowedBooks(bookArrayList);

        returnBookService = new ReturnBookService(userRepository, bookRepository);
    }

    @Test
    void returnOneBook() {

        //GIVEN
        BorrowBookRequest borrowBookRequest = new BorrowBookRequest(1, 1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(borrowedBook));

        //WHEN
        returnBookService.returnBook(borrowBookRequest);

        //THEN
        User returnedUser = userRepository.findById(1L).orElseThrow();
        Book returnedBook = bookRepository.findById(2L).orElseThrow();
        Assertions.assertThat(returnedUser.getBorrowedBooks().size()).isEqualTo(1);
        Assertions.assertThat(returnedBook.isBorrowed()).isFalse();

    }


    @Test
    void returnALlBooks() {

        //GIVEN
        BorrowBookRequest borrowBookRequest = new BorrowBookRequest(1, 1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(borrowedBook));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(borrowedBook2));

        //WHEN
        returnBookService.returnBook(borrowBookRequest);
        borrowBookRequest.setBookId(2L);
        returnBookService.returnBook(borrowBookRequest);

        //THEN
        User returnedUser = userRepository.findById(1L).orElseThrow();
        Book returnedBook = bookRepository.findById(2L).orElseThrow();
        Assertions.assertThat(returnedUser.getBorrowedBooks().size()).isZero();
        Assertions.assertThat(returnedBook.isBorrowed()).isFalse();

    }

}
