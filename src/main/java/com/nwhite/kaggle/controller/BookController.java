package com.nwhite.kaggle.controller;

import com.nwhite.kaggle.dto.BookDto;
import com.nwhite.kaggle.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {
    BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/top10")
    public ResponseEntity<List<BookDto>> top10(@RequestParam(required = false) Integer year,
                                               @RequestParam String column,
                                               @RequestParam String sort) {
        return bookService.top10(year, column, sort);
    }
}
