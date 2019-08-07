package edu.mum.cs.cs425.demos.elibrarydemocrudweb.controller;

import edu.mum.cs.cs425.demos.elibrarydemocrudweb.model.Book;
import edu.mum.cs.cs425.demos.elibrarydemocrudweb.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class BookController {

//    @Autowired
    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = {"/elibrary/book/list"})
    public ModelAndView listBooks(@RequestParam(defaultValue = "0") int pageno) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("books", bookService.getAllBooksPaged(pageno));
        modelAndView.addObject("currentPageNo", pageno);
        modelAndView.setViewName("book/list");
        return modelAndView;
    }
    
    @RequestMapping(value = {"/searchBook"})
    public ModelAndView listSelectedBooks(@ModelAttribute("book") Book book,
            BindingResult bindingResult, @RequestParam(defaultValue = "0") int pageno )
    {   
    	if(book.getIsbn() == null)
    	{
    		return null;
    	}
    	ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("books", bookService.getSearchedBooksPaged(book.getIsbn()));
        modelAndView.addObject("currentPageNo", 1);
        modelAndView.setViewName("book/list");
        return modelAndView;
    	
    }

    @GetMapping(value = {"/elibrary/book/new"})
    public String displayNewBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "book/new";
    }


    @RequestMapping(value = {"/elibrary/searchOption", "/searchOption"})
    public String SearchOption() {
        return "book/search";
    }

    @PostMapping(value = {"/elibrary/book/new"})
    public String addNewBook(@Valid @ModelAttribute("book") Book book,
                                     BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "book/new";
        }
        book = bookService.saveBook(book);
        return "redirect:/elibrary/book/list";
    }

    @GetMapping(value = {"/elibrary/book/edit/{bookId}"})
    public String editBook(@PathVariable Integer bookId, Model model) {
        Book book = bookService.getBookById(bookId);
        if (book != null) {
            model.addAttribute("book", book);
            return "book/edit";
        }
        return "book/list";
    }

    @PostMapping(value = {"/elibrary/book/edit"})
    public String updateBook(@Valid @ModelAttribute("book") Book book,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "book/edit";
        }
        book = bookService.saveBook(book);
        return "redirect:/elibrary/book/list";
    }

    @GetMapping(value = {"/elibrary/book/delete/{bookId}"})
    public String deleteBook(@PathVariable Integer bookId, Model model) {
        bookService.deleteBookById(bookId);
        return "redirect:/elibrary/book/list";
    }
    
    @RequestMapping("/elibrary/book/SearchByOption")
    public ModelAndView searchByOption(@Valid @ModelAttribute("book") Book book,
            BindingResult bindingResult, Model model)
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("books", bookService.searchByOption(book.getTitle(), book.getIsbn(), book.getPublisher()));
        modelAndView.addObject("currentPageNo", 1);
        modelAndView.setViewName("book/list");
        return modelAndView;
    }

    @RequestMapping("/elibrary/book/advancedSearch")
    public ModelAndView advancedSearch(@RequestParam String searchText)
    {
        double overDuefee;
        LocalDate publishedDate;
        ModelAndView modelAndView = new ModelAndView();
       // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try{
            overDuefee = Double.parseDouble(searchText);
            Page<Book> books = bookService.advancedSearch(overDuefee);
            if(books.hasContent()) {
                modelAndView.addObject("books", books);
                modelAndView.addObject("currentPageNo",1);
                modelAndView.setViewName("book/list");
                return  modelAndView;
            }
        }
        catch (Exception e)
        {
            try
            {
                publishedDate = LocalDate.parse(searchText, formatter);
                Page<Book> books =  bookService.advancedSearch(publishedDate);
                if(books.hasContent())
                modelAndView.addObject("books",books );
            }
            catch (Exception e1) {
            }
        }

        modelAndView.addObject("books", bookService.advancedSearch(searchText));
        modelAndView.addObject("currentPageNo",1);
        modelAndView.setViewName("book/list");
        return  modelAndView;
    }

}
