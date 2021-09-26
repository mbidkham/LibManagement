package de.hexad.libmanagement.controller;

import de.hexad.libmanagement.dto.BorrowBookRequest;
import de.hexad.libmanagement.service.BorrowBookService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class BorrowBookController {

    private final BorrowBookService borrowBookService;

    @PutMapping("/borrow")
    public ResponseEntity<String> borrowBook(@RequestBody @Valid BorrowBookRequest borrowBookRequest){
        borrowBookService.borrowBook(borrowBookRequest);
        return ResponseEntity.ok("You have Borrowed the book successfully:)");
    }

    @PutMapping("/borrow-copy")
    public ResponseEntity<String> borrowBookCopy(@RequestBody @Valid BorrowBookRequest borrowBookRequest){
        borrowBookService.borrowCopyBook(borrowBookRequest);
        return ResponseEntity.ok("You have Borrowed the copy of the book successfully:)");
    }

}
