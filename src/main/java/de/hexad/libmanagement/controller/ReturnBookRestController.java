package de.hexad.libmanagement.controller;

import de.hexad.libmanagement.dto.BorrowBookRequest;
import de.hexad.libmanagement.service.ReturnBookService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class ReturnBookRestController {

    private final ReturnBookService returnBookService;

    @PutMapping("/return")
    public ResponseEntity<String> borrowBook(@RequestBody @Valid BorrowBookRequest borrowBookRequest){
        returnBookService.returnBook(borrowBookRequest);
        return ResponseEntity.ok("You have Returned the book successfully:)");
    }
}
