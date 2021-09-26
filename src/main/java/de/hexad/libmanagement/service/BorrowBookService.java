package de.hexad.libmanagement.service;

import de.hexad.libmanagement.dto.BorrowBookRequest;
import de.hexad.libmanagement.dto.RestApiMessage;
import de.hexad.libmanagement.exception.BorrowBookException;
import de.hexad.libmanagement.model.entity.Book;
import de.hexad.libmanagement.model.entity.User;
import de.hexad.libmanagement.model.repository.BookRepository;
import de.hexad.libmanagement.model.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class BorrowBookService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;


    public BorrowBookService(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;

    }

    public void borrowBook(BorrowBookRequest borrowBookRequest){
        User validUser = checkUserIsValid(borrowBookRequest.getUserId());
        Book validBook = checkBookIsValid(borrowBookRequest.getBookId());
        validUser.getBorrowedBooks().add(validBook);
        validBook.setBorrowed(true);
        userRepository.save(validUser);
    }

    private Book checkBookIsValid(long bookId) {
        Book existBook = bookRepository.findById(bookId).orElseThrow(() -> new BorrowBookException(RestApiMessage.BOOK_NOT_FOUND));
        if (existBook.isBorrowed()) {
            throw new BorrowBookException(RestApiMessage.BOOK_IS_BORROWED);
        }
        return existBook;
    }
    private User checkUserIsValid(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BorrowBookException(RestApiMessage.USER_NOT_FOUND));
        if (user.getBorrowedBooks().size() >= 2) {
            throw new BorrowBookException(RestApiMessage.LIMIT_BORROW_VALUE);
        }
        return user;
    }
}
