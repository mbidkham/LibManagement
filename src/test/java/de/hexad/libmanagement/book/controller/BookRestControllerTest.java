package de.hexad.libmanagement.book.controller;


import de.hexad.libmanagement.LibManagementApplication;
import de.hexad.libmanagement.model.entity.Book;
import de.hexad.libmanagement.model.entity.User;
import de.hexad.libmanagement.model.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest()
@ContextConfiguration(classes = {LibManagementApplication.class})
@AutoConfigureMockMvc
class BookRestControllerTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    private Book borrowedBook;
    private Book notBorrowedBook ;
    private User user;

    @BeforeEach
    void init(){
        borrowedBook = new Book();
        borrowedBook.setId(1);
        borrowedBook.setName("The Clown!");
        borrowedBook.setBorrowed(true);

        notBorrowedBook = new Book();
        notBorrowedBook.setId(2);
        notBorrowedBook.setName("A Fraction of the whole!");

        user = new User();
        user.setId(1);
        user.setName("Mehraneh");
        user.setBorrowedBooks(new ArrayList<>(List.of(borrowedBook)));

    }
    @Test
    void testWhenThereAreMoreThanOneBookToView() throws Exception {

        bookRepository.save(borrowedBook);
        userRepository.save(user);
        bookRepository.save(notBorrowedBook);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].name").value("A Fraction of the whole!"));

    }

}
