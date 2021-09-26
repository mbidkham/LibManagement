package de.hexad.libmanagement.service;

import de.hexad.libmanagement.dto.*;
import de.hexad.libmanagement.model.entity.Book;
import de.hexad.libmanagement.model.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {


    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public PaginatedFindBooksResponse getAllBooks(int page, int size) {
        Page<Book> books = bookRepository.findAllByBorrowedIsFalse(PageRequest.of(page, size));
        List<BookDto> bookList = books.stream().map(book -> modelMapper.map(book, BookDto.class)).collect(Collectors.toList());

        return PaginatedFindBooksResponse.builder()
                .bookDtoList(bookList)
                .pageSize(books.getPageable().getPageSize())
                .pageNumber(books.getPageable().getPageNumber())
                .totalElements(books.getTotalElements())
                .build();
    }

}
