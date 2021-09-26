package de.hexad.libmanagement.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import de.hexad.libmanagement.LibManagementApplication;
import de.hexad.libmanagement.dto.BorrowBookRequest;
import de.hexad.libmanagement.model.entity.Book;
import de.hexad.libmanagement.model.entity.User;
import de.hexad.libmanagement.model.repository.BookRepository;
import de.hexad.libmanagement.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest()
@ContextConfiguration(classes = {LibManagementApplication.class})
@AutoConfigureMockMvc
class ReturnBookControllerTest {


    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    private Book borrowedBook;
    private Book borrowedBook2;
    private User user;

    @BeforeEach
    void init() {
        borrowedBook = new Book();
        borrowedBook.setId(5);
        borrowedBook.setName("The Castle");
        borrowedBook.setBorrowed(true);

        borrowedBook2 = new Book();
        borrowedBook2.setId(6);
        borrowedBook2.setName("Adventure");
        borrowedBook2.setBorrowed(true);


        user = new User();
        user.setId(2);
        user.setName("Mehraneh's Friend");
        ArrayList<Book> bookArrayList = new ArrayList<>();
        bookArrayList.add(borrowedBook);
        bookArrayList.add(borrowedBook2);
        user.setBorrowedBooks(bookArrayList);

    }

    @Test
    @Transactional
    void testWhenReturn1BookOf2BorrowedBooks() throws Exception {

        //GIVEN
        bookRepository.save(borrowedBook);
        bookRepository.save(borrowedBook2);
        userRepository.save(user);

        //WHEN
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        BorrowBookRequest borrowBookRequest = new BorrowBookRequest(2, 5);
        String requestJson = ow.writeValueAsString(borrowBookRequest);

        //THEN
        mockMvc.perform(put("/return")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        User returnerUser = userRepository.findById(2L).orElseThrow();
        Book returnedBook = bookRepository.findById(5L).orElseThrow();
        assertThat(returnerUser.getBorrowedBooks().size()).isEqualTo(1);
        assertThat(returnedBook.isBorrowed()).isFalse();

    }

    @Test
    @Transactional
    void testWhenReturnAllBorrowedBooks() throws Exception {

        //GIVEN
        bookRepository.save(borrowedBook);
        bookRepository.save(borrowedBook2);
        userRepository.save(user);

        //WHEN
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        BorrowBookRequest borrowBookRequest = new BorrowBookRequest(2, 5);
        String requestJson = ow.writeValueAsString(borrowBookRequest);

        //THEN
        mockMvc.perform(put("/return")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        borrowBookRequest.setBookId(6);
        requestJson = ow.writeValueAsString(borrowBookRequest);
        mockMvc.perform(put("/return")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        User returnerUser = userRepository.findById(2L).orElseThrow();
        Book returnedBook = bookRepository.findById(5L).orElseThrow();
        Book returnedBook2 = bookRepository.findById(6L).orElseThrow();
        assertThat(returnerUser.getBorrowedBooks().size()).isZero();
        assertThat(returnedBook.isBorrowed()).isFalse();
        assertThat(returnedBook2.isBorrowed()).isFalse();

    }
}
