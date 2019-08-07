package edu.mum.cs.cs425.demos.elibrarydemocrudweb.service.impl;

import edu.mum.cs.cs425.demos.elibrarydemocrudweb.model.Book;
import edu.mum.cs.cs425.demos.elibrarydemocrudweb.repository.BookRepository;
import edu.mum.cs.cs425.demos.elibrarydemocrudweb.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

//@Service(value = "MainBookService")
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository repository;

    @Override
    public Iterable<Book> getAllBooks() {
//        return ((List<Book>)repository.findAll())
//                .stream()
//                .sorted(Comparator.comparing(Book::getTitle))
//                .collect(Collectors.toList());
        return repository.findAll(Sort.by("title"));
    }

    @Override
    public Page<Book> getAllBooksPaged(int pageNo) {
        return repository.findAll(PageRequest.of(pageNo, 3, Sort.by("title")));
    }

    @Override
    public Book saveBook(Book book) {
        return repository.save(book);
    }

    @Override
    public Book getBookById(Integer bookId) {
        return repository.findById(bookId).orElse(null);
    }

    @Override
    public void deleteBookById(Integer bookId) {
        repository.deleteById(bookId);
    }

    @Override
    public Optional<Book> findByISBN(String isbn) {
        return repository.findBookByIsbn(isbn);
    }

	@Override
	public Page<Book> getSearchedBooksPaged(String isbn) {
		//return repository.findBookByIsbn(isbn).orElse(null);
		 Page<Book> page = new PageImpl<>(Arrays.asList(repository.findBookByIsbn(isbn).orElse(null)));
		 return page;
	}

    @Override
    public Page<Book> searchByOption(String title, String isbn, String publisher) {
        List<Book> books =repository.findAllByTitleContainingAndIsbnContainingAndPublisherContaining(title,isbn,publisher);
        return new PageImpl<>(books);
    }

    @Override
    public Page<Book> advancedSearch(String searchString) {
        List<Book> books = repository.findAllByTitleContainingOrIsbnContainingOrPublisherContaining(searchString, searchString, searchString);
        return new PageImpl<>(books);
    }

    @Override
    public Page<Book> advancedSearch(Double fee) {
       List<Book> books = repository.findAllByOverdueFee(fee);
        return new PageImpl<>(books);
    }

    @Override
    public Page<Book> advancedSearch(LocalDate date) {
       List<Book> books = repository.findAllByDatePublishedEquals(date);
       return new PageImpl<>(books);
    }

}
