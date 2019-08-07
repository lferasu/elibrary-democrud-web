package edu.mum.cs.cs425.demos.elibrarydemocrudweb.repository;

import edu.mum.cs.cs425.demos.elibrarydemocrudweb.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    // This interface definition relies on the public abstract methods
    // inherited from the super interface, CrudRepository<T, ID>
    // We may override any or add more methods here, if needed.
    Optional<Book> findBookByIsbn(String isbn);
    List<Book> findAllByTitleContainingAndIsbnContainingAndPublisherContaining(String title, String isbn, String publisher);
    List<Book> findAllByTitleContainingOrIsbnContainingOrPublisherContaining(String title, String isbn, String publisher);
    List<Book> findAllByOverdueFee(Double fee);
    List<Book> findAllByDatePublishedEquals(LocalDate date);
}
