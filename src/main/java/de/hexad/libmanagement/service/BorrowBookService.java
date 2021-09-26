package de.hexad.libmanagement.service;

import de.hexad.libmanagement.dto.BorrowBookRequest;
import de.hexad.libmanagement.dto.RestApiMessage;
import de.hexad.libmanagement.exception.BorrowBookException;
import de.hexad.libmanagement.model.entity.Book;
import de.hexad.libmanagement.model.entity.User;
import de.hexad.libmanagement.model.repository.BookRepository;
import de.hexad.libmanagement.model.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BorrowBookService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;


    public BorrowBookService(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;

    }

    public void borrowBook(BorrowBookRequest borrowBookRequest) {
        User validUser = checkUserIsValid(borrowBookRequest.getUserId());
        Book validBook = checkBookIsValid(borrowBookRequest.getBookId());
        validUser.getBorrowedBooks().add(validBook);
        validBook.setBorrowed(true);
        userRepository.save(validUser);
    }

    public void borrowCopyBook(BorrowBookRequest borrowBookRequest) {
        User validUser = checkUserWhenBorrowCopyIsValid(borrowBookRequest.getUserId(), borrowBookRequest.getBookId());
        Book validBook = checkCopyBookIsValid(borrowBookRequest.getBookId());
        if (validUser.getBorrowedBooks().isEmpty()) {
            validUser.setBorrowedBooks(new ArrayList<>(List.of(validBook)));
        } else {
            validUser.getBorrowedBooks().add(validBook);
        }
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

    private Book checkCopyBookIsValid(long bookId) {
        List<Book> clonedBooks = bookRepository.findByParentBook_IdAndBorrowedIsFalse(bookId);
        if (clonedBooks.isEmpty()) {
            throw new BorrowBookException(RestApiMessage.NOT_COPY_EXIST);
        }
        return clonedBooks.get(0);
    }

    private User checkUserIsValid(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BorrowBookException(RestApiMessage.USER_NOT_FOUND));
        if (user.getBorrowedBooks().size() >= 2) {
            throw new BorrowBookException(RestApiMessage.LIMIT_BORROW_VALUE);
        }
        return user;
    }

    private User checkUserWhenBorrowCopyIsValid(long userId, long bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BorrowBookException(RestApiMessage.USER_NOT_FOUND));
        if (user.getBorrowedBooks().size() >= 2) {
            throw new BorrowBookException(RestApiMessage.LIMIT_BORROW_VALUE);
        }
        user.getBorrowedBooks().forEach(book -> {
            if (Objects.nonNull(book.getParentBook()) && book.getParentBook().getId() == bookId) {
                throw new BorrowBookException(RestApiMessage.DUPLICATE_CLONE_OF_BOOK);
            }
        });
        return user;
    }
}
