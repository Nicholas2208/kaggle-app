package com.nwhite.kaggle.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookDto {
    private Long id;
    private String title;
    private String series;
    private Integer releaseNumber;
    private String author;
    private String description;
    private Integer numPages;
    private String format;
    private List<String> genres;
    private LocalDate publicationDate;
    private Float rating;
    private Float numberOfVoters;

}
