package com.nwhite.kaggle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
    private  String apiPath;

    private HttpStatus errorCode;

    private  String errorMessage;
}
