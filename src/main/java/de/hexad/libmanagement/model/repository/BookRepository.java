package de.hexad.libmanagement.model.repository;

import de.hexad.libmanagement.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findAll(Pageable pageable);

    Page<Book> findAllByBorrowedIsFalse(Pageable pageable);

    List<Book> findByParentBook_IdAndBorrowedIsFalse(long parentBookId);

}
