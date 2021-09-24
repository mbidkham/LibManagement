package de.hexad.libmanagement.book.controller;


import de.hexad.libmanagement.LibManagementApplication;
import de.hexad.libmanagement.common.DataUtil;
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


    @BeforeEach
    public void init(){
        userRepository.save(DataUtil.user);
    }


    @Test
    void testWhenThereIsOneBookToView() throws Exception {

        bookRepository.save(DataUtil.book);
        bookRepository.save(DataUtil.notBorrowedBook);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].name").value("A Fraction of the whole!"));

    }

}
