package com.nwhite.kaggle.service;

import com.nwhite.kaggle.KaggleApplication;
import com.nwhite.kaggle.dto.BookDto;
import com.nwhite.kaggle.enums.ColumnType;
import com.nwhite.kaggle.enums.SortOrder;
import com.nwhite.kaggle.exception.WrongParamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    KaggleApplication application;

    @Autowired
    public BookService(KaggleApplication application) {
        this.application = application;
    }

    public ResponseEntity<List<BookDto>> top10(Integer year, String column,String sort) {
        try {
            ColumnType columnType = ColumnType.valueOf(column.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Некорректное значение параметра column: " + column);
        }

        try {
            SortOrder sortOrder = SortOrder.valueOf(sort.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Некорректное значение параметра sort: " + sort);
        }

        List<BookDto> bookList = application.getBookList();
        System.out.println(bookList.size());
        List<BookDto> filteredBooks = new ArrayList<>(bookList);

        if (year != null) {
            filteredBooks = bookList.stream()
                    .filter(book -> book.getPublicationDate() != null && book.getPublicationDate().getYear() == year)
                    .collect(Collectors.toList());
        }

        Comparator<BookDto> comparator = null;
        switch (column) {
            case "book":
                comparator = Comparator.comparing(BookDto::getTitle);
                break;
            case "author":
                comparator = Comparator.comparing(BookDto::getAuthor);
                break;
            case "numPages":
                comparator = Comparator.comparingInt(BookDto::getNumPages);
                break;
            case "publicationDate":
                comparator = Comparator.comparing(BookDto::getPublicationDate);
                break;
            case "rating":
                comparator = Comparator.comparing(BookDto::getRating);
                break;
            case "numberOfVoters":
                comparator = Comparator.comparing(BookDto::getNumberOfVoters);
                break;
            default:
                throw new IllegalArgumentException("Некорректное значение параметра column");
        }

        if ("DESC".equals(sort)) {
            comparator = comparator.reversed();
        }

        filteredBooks.sort(comparator);

        List<BookDto> result = filteredBooks.stream().limit(10).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
