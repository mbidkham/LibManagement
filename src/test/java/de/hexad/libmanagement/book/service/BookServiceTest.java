package de.hexad.libmanagement.book.service;

import de.hexad.libmanagement.common.DataUtil;
import de.hexad.libmanagement.dto.PaginatedFindBooksResponse;
import de.hexad.libmanagement.model.entity.Book;
import de.hexad.libmanagement.model.entity.User;
import de.hexad.libmanagement.model.repository.BookRepository;
import de.hexad.libmanagement.service.BookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    private BookService bookService;

    @BeforeEach
    public void init() {
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

        bookService = new BookService(bookRepository);
    }

    @Test
    void getAllBooksWhenNotBookToView() {

        //GIVEN
        when(bookRepository.findAllByBorrowedIsFalse(PageRequest.of(0, 20))).thenReturn(DataUtil.zeroBookResponse);

        //WHEN
        PaginatedFindBooksResponse responsePagination = bookService.getAllBooks(0, 20);

        //THEN
        Assertions.assertThat(responsePagination.getBookDtoList()).isEmpty();
        Assertions.assertThat(responsePagination.getPageSize()).isEqualTo(20);
        Assertions.assertThat(responsePagination.getPageNumber()).isZero();
        Assertions.assertThat(responsePagination.getTotalElements()).isZero();

    }

    @Test
    void getAllBooksWhenBookExistToView() {

        //GIVEN
        when(bookRepository.findAllByBorrowedIsFalse(PageRequest.of(0, 20))).thenReturn(DataUtil.existBookResponse);

        //WHEN
        PaginatedFindBooksResponse responsePagination = bookService.getAllBooks(0, 20);

        //THEN
        Assertions.assertThat(responsePagination.getBookDtoList()).isNotEmpty();
        Assertions.assertThat(responsePagination.getPageSize()).isEqualTo(20);
        Assertions.assertThat(responsePagination.getPageNumber()).isZero();
        Assertions.assertThat(responsePagination.getTotalElements()).isEqualTo(1);

    }
}
