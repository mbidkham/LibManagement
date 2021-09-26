package de.hexad.libmanagement.service;

import de.hexad.libmanagement.dto.BorrowBookRequest;
import de.hexad.libmanagement.dto.RestApiMessage;
import de.hexad.libmanagement.exception.BorrowBookException;
import de.hexad.libmanagement.model.entity.Book;
import de.hexad.libmanagement.model.entity.User;
import de.hexad.libmanagement.model.repository.BookRepository;
import de.hexad.libmanagement.model.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReturnBookService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;


    @Transactional
    public void returnBook(BorrowBookRequest borrowBookRequest) {
        User validUser = checkUserIsValid(borrowBookRequest.getUserId(), borrowBookRequest.getBookId());
        Book borrowedBookByUser = new Book();
        for (Book borrowedBook : validUser.getBorrowedBooks()) {
            if(borrowedBook.getId() == borrowBookRequest.getBookId()){
                borrowedBookByUser = borrowedBook;
                break;
            }
        }
        borrowedBookByUser.setBorrowed(false);
        validUser.getBorrowedBooks().remove(borrowedBookByUser);
        bookRepository.save(borrowedBookByUser);
        userRepository.save(validUser);
    }

    private User checkUserIsValid(long userId, long bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BorrowBookException(RestApiMessage.USER_NOT_FOUND));
        List<Long> borrowedBookIds = user.getBorrowedBooks().stream().map(Book::getId).collect(Collectors.toList());
        if(!borrowedBookIds.contains(bookId)){
            throw new BorrowBookException(RestApiMessage.NOT_BORROWED_BEFORE);
        }
        return user;
    }
}
