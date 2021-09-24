package de.hexad.libmanagement.controller;

import de.hexad.libmanagement.dto.BookDto;
import de.hexad.libmanagement.dto.PaginatedFindBooksResponse;
import de.hexad.libmanagement.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @GetMapping("/books")
    public ResponseEntity<PageImpl<BookDto>> getAllBooks(
            @RequestParam(value = "page", defaultValue = "0", required = false) @Valid int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size) {

        PaginatedFindBooksResponse bookList = bookService.getAllBooks(page, size);
        PageImpl<BookDto> responseDto = new PageImpl<>(bookList.getBookDtoList(),
                PageRequest.of(bookList.getPageNumber(), bookList.getPageSize()),
                bookList.getTotalElements());

        return ResponseEntity.ok(responseDto);

    }

}
