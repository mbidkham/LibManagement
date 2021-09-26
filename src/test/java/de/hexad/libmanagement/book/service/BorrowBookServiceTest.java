package de.hexad.libmanagement.book.service;

import de.hexad.libmanagement.dto.BorrowBookRequest;
import de.hexad.libmanagement.exception.BorrowBookException;
import de.hexad.libmanagement.model.entity.Book;
import de.hexad.libmanagement.model.entity.User;
import de.hexad.libmanagement.model.repository.BookRepository;
import de.hexad.libmanagement.model.repository.UserRepository;
import de.hexad.libmanagement.service.BorrowBookService;
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
class BorrowBookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    private BorrowBookService borrowBookService;

    private Book borrowedBook;
    private Book notBorrowedBook ;
    private Book notBorrowedBook2;
    private User user;



    @BeforeEach
    void init() {

        borrowedBook = new Book();
        borrowedBook.setId(1);
        borrowedBook.setName("The Clown!");
        borrowedBook.setBorrowed(true);

        notBorrowedBook = new Book();
        notBorrowedBook.setId(2);
        notBorrowedBook.setName("A Fraction of the whole!");

        notBorrowedBook2 = new Book();
        notBorrowedBook2.setId(3);
        notBorrowedBook2.setName("Shining!");

        user = new User();
        user.setId(1);
        user.setName("Mehraneh");
        user.setBorrowedBooks(new ArrayList<>(List.of(borrowedBook)));

        borrowBookService = new BorrowBookService(userRepository, bookRepository);
    }

    @Test
    void borrowBookTest() {

        //GIVEN


        BorrowBookRequest borrowBookRequest = new BorrowBookRequest(1, 3);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(borrowedBook));
        when(bookRepository.findById(3L)).thenReturn(Optional.of(notBorrowedBook2));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        ArrayList<Book> bookArrayList = new ArrayList<>();
        bookArrayList.add(notBorrowedBook);
        bookArrayList.add(borrowedBook);
        when(bookRepository.findAll()).thenReturn(bookArrayList);


        //WHEN
        borrowBookService.borrowBook(borrowBookRequest);

        //THEN
        Assertions.assertThat(userRepository.findById(1L).get().getBorrowedBooks().size()).isEqualTo(2);
        Assertions.assertThat(bookRepository.findById(3L).get().isBorrowed()).isTrue();
        Assertions.assertThat(bookRepository.findAll().stream().anyMatch(Book::isBorrowed)).isTrue();
        Assertions.assertThat(bookRepository.findAll().stream().anyMatch(Book::isBorrowed)).isTrue();

    }

    @Test
    void borrowBookWhenUserNotFoundTest() {

        //GIVEN
        Book borrowedBook = new Book();
        borrowedBook.setId(1);
        borrowedBook.setName("The Clown!");
        borrowedBook.setBorrowed(true);

        Book notBorrowedBook = new Book();
        notBorrowedBook.setId(2);
        notBorrowedBook.setName("A Fraction of the whole!");

        User user = new User();
        user.setId(1);
        user.setName("Mehraneh");
        user.setBorrowedBooks(new ArrayList<>(List.of(borrowedBook)));

        BorrowBookRequest borrowBookRequest = new BorrowBookRequest(3, 2);
        when(bookRepository.findById(2L)).thenReturn(Optional.of(notBorrowedBook));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        ArrayList<Book> bookArrayList = new ArrayList<>();
        bookArrayList.add(notBorrowedBook);
        bookArrayList.add(borrowedBook);
        when(bookRepository.findAll()).thenReturn(bookArrayList);

        //THEN
        Assertions.assertThatThrownBy(() -> borrowBookService.borrowBook(borrowBookRequest)).isInstanceOf(BorrowBookException.class);

    }

    @Test
    void borrowBookWhenUserWantsToBorrowMoreThan2Books() {

        //GIVEN
        Book borrowedBook = new Book();
        borrowedBook.setId(1);
        borrowedBook.setName("The Clown!");
        borrowedBook.setBorrowed(true);

        Book notBorrowedBook = new Book();
        notBorrowedBook.setId(2);
        notBorrowedBook.setName("A Fraction of the whole!");

        User user = new User();
        user.setId(1);
        user.setName("Mehraneh");
        user.setBorrowedBooks(new ArrayList<>(List.of(borrowedBook)));

        BorrowBookRequest borrowBookRequest = new BorrowBookRequest(1, 2);

        when(bookRepository.findById(2L)).thenReturn(Optional.of(notBorrowedBook));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(borrowedBook));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));


        //WHEN
        borrowBookService.borrowBook(borrowBookRequest);
        BorrowBookRequest borrowBookRequest2 = new BorrowBookRequest(1, 3);
        //THEN
        Assertions.assertThatThrownBy(() -> borrowBookService.borrowBook(borrowBookRequest2)).isInstanceOf(BorrowBookException.class);

    }


}
