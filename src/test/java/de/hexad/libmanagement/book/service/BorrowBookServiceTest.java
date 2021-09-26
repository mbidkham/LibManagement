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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private Book copyBorrowedBook;
    private Book copyBorrowedBook2;
    private Book notBorrowedBook;
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

        copyBorrowedBook = new Book();
        copyBorrowedBook.setId(3);
        copyBorrowedBook.setName("The Clown Copy!");

        copyBorrowedBook2 = new Book();
        copyBorrowedBook2.setId(4);
        copyBorrowedBook2.setName("The Clown Copy!");

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
        when(bookRepository.findById(2L)).thenReturn(Optional.of(notBorrowedBook));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        ArrayList<Book> bookArrayList = new ArrayList<>();
        bookArrayList.add(notBorrowedBook);
        bookArrayList.add(borrowedBook);
        when(bookRepository.findAll()).thenReturn(bookArrayList);

        //WHEN
        BorrowBookRequest borrowBookRequest = new BorrowBookRequest(3, 2);

        //THEN
        Assertions.assertThatThrownBy(() -> borrowBookService.borrowBook(borrowBookRequest)).isInstanceOf(BorrowBookException.class);

    }

    @Test
    void borrowBookWhenUserWantsToBorrowMoreThan2Books() {

        //GIVEN
        when(bookRepository.findById(2L)).thenReturn(Optional.of(notBorrowedBook));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(borrowedBook));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        //WHEN
        BorrowBookRequest borrowBookRequest = new BorrowBookRequest(1, 2);
        borrowBookService.borrowBook(borrowBookRequest);
        borrowBookRequest.setBookId(3);
        Throwable exceptionThatWasThrown = assertThrows(BorrowBookException.class, () -> borrowBookService.borrowCopyBook(borrowBookRequest));

        //THEN
        Assertions.assertThat(exceptionThatWasThrown.getMessage()).isEqualTo("You can't borrow more than 2 books.");

    }

    @Test
    void borrowCopyOfBookWhenOneVersionExist() {

        //GIVEN
        copyBorrowedBook.setParentBook(borrowedBook);
        ArrayList<Book> copyList = new ArrayList<>();
        copyList.add(copyBorrowedBook);
        BorrowBookRequest borrowBookRequest = new BorrowBookRequest(1, 1);
        when(bookRepository.findByParentBook_IdAndBorrowedIsFalse(1L)).thenReturn(copyList);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        //WHEN
        borrowBookService.borrowCopyBook(borrowBookRequest);

        //THEN
        List<Book> foundList = bookRepository.findByParentBook_IdAndBorrowedIsFalse(1L);
        //one copy was borrowed
        Assertions.assertThat(foundList.stream().anyMatch(Book::isBorrowed)).isTrue();
        //remaining number copy of book is zero
        Assertions.assertThat(foundList.stream().anyMatch(book -> !book.isBorrowed())).isFalse();
    }

    @Test
    void borrowCopyOfBookWhenMoreThanOneVersionExist() {

        //GIVEN
        copyBorrowedBook.setParentBook(borrowedBook);
        copyBorrowedBook2.setParentBook(borrowedBook);
        ArrayList<Book> copyList = new ArrayList<>();
        copyList.add(copyBorrowedBook);
        copyList.add(copyBorrowedBook2);
        BorrowBookRequest borrowBookRequest = new BorrowBookRequest(1, 1);
        when(bookRepository.findByParentBook_IdAndBorrowedIsFalse(1L)).thenReturn(copyList);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        //WHEN
        borrowBookService.borrowCopyBook(borrowBookRequest);

        //THEN
        List<Book> foundList = bookRepository.findByParentBook_IdAndBorrowedIsFalse(1L);
        //one copy was borrowed
        Assertions.assertThat(foundList.stream().anyMatch(Book::isBorrowed)).isTrue();
        //remaining number copy of book is 1
        Assertions.assertThat(foundList.stream().anyMatch(book -> !book.isBorrowed())).isTrue();
    }

    @Test
    void borrowWhenUserWantsToBorrow2VersionOfBook() {

        //GIVEN
        user.setBorrowedBooks(Collections.emptyList());
        copyBorrowedBook.setParentBook(borrowedBook);
        copyBorrowedBook2.setParentBook(borrowedBook);
        ArrayList<Book> copyList = new ArrayList<>();
        copyList.add(copyBorrowedBook);
        BorrowBookRequest borrowBookRequest = new BorrowBookRequest(1, 1);
        when(bookRepository.findByParentBook_IdAndBorrowedIsFalse(1L)).thenReturn(copyList);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        borrowBookService.borrowCopyBook(borrowBookRequest);

        //WHEN
        copyList.remove(copyBorrowedBook);
        Throwable exceptionThatWasThrown = assertThrows(BorrowBookException.class, () -> borrowBookService.borrowCopyBook(borrowBookRequest));

        //THEN
        Assertions.assertThat(exceptionThatWasThrown.getMessage()).isEqualTo("You have borrowed one copy of this book before.");

    }

}
