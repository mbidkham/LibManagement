package de.hexad.libmanagement.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import de.hexad.libmanagement.LibManagementApplication;
import de.hexad.libmanagement.dto.BorrowBookRequest;
import de.hexad.libmanagement.model.entity.Book;
import de.hexad.libmanagement.model.entity.User;
import de.hexad.libmanagement.model.repository.BookRepository;
import de.hexad.libmanagement.model.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest()
@ContextConfiguration(classes = {LibManagementApplication.class})
@AutoConfigureMockMvc
class BorrowBookControllerTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    private Book borrowedBook;
    private Book notBorrowedBook;
    private Book copyBorrowedBook;
    private Book copyBorrowedBook2;
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

        user = new User();
        user.setId(1);
        user.setName("Mehraneh");
        user.setBorrowedBooks(new ArrayList<>(List.of(borrowedBook)));

    }

    @Test
    void testWhenThereIsBookExistToBorrow() throws Exception {

        //GIVEN
        bookRepository.save(borrowedBook);
        userRepository.save(user);
        bookRepository.save(notBorrowedBook);

        //WHEN
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        BorrowBookRequest borrowBookRequest = new BorrowBookRequest(1, 2);
        String requestJson = ow.writeValueAsString(borrowBookRequest);

        //THEN
        mockMvc.perform(put("/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void testWhenOneCopyOfBookIsExist() throws Exception {

        //GIVEN
        bookRepository.save(borrowedBook);
        copyBorrowedBook.setParentBook(borrowedBook);
        bookRepository.save(copyBorrowedBook);
        userRepository.save(user);

        //WHEN
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        BorrowBookRequest borrowBookRequest = new BorrowBookRequest(1, 1);
        String requestJson = ow.writeValueAsString(borrowBookRequest);
        mockMvc.perform(put("/borrow-copy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        //THEN
        assertThat(bookRepository.findByParentBook_IdAndBorrowedIsFalse(1)).isEmpty();

    }

    @Test
    void testWhenMoreThanOneCopyOfBookIsExist() throws Exception {

        //GIVEN
        bookRepository.save(borrowedBook);
        copyBorrowedBook.setParentBook(borrowedBook);
        copyBorrowedBook2.setParentBook(borrowedBook);
        bookRepository.save(copyBorrowedBook);
        bookRepository.save(copyBorrowedBook2);
        userRepository.save(user);

        //WHEN
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        BorrowBookRequest borrowBookRequest = new BorrowBookRequest(1, 1);
        String requestJson = ow.writeValueAsString(borrowBookRequest);
        mockMvc.perform(put("/borrow-copy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        //THEN
        assertThat(bookRepository.findByParentBook_IdAndBorrowedIsFalse(1)).isNotEmpty();

    }

    @Test
    void testWhenUserBorrow2VersionsOfBook() throws Exception {
        bookRepository.save(borrowedBook);
        copyBorrowedBook.setParentBook(borrowedBook);
        copyBorrowedBook2.setParentBook(borrowedBook);
        bookRepository.save(copyBorrowedBook);
        bookRepository.save(copyBorrowedBook2);

        user.setBorrowedBooks(Collections.emptyList());
        userRepository.save(user);

        //WHEN
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        BorrowBookRequest borrowBookRequest = new BorrowBookRequest(1, 1);
        String requestJson = ow.writeValueAsString(borrowBookRequest);
        mockMvc.perform(put("/borrow-copy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        //THEN
        MvcResult mvcResult = mockMvc.perform(put("/borrow-copy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("You have borrowed one copy of this book before.");
    }

}
