package com.cepa.generalservice.exceptions;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.cepa.generalservice.data.dto.response.ExceptionResponse;

@RestControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException ex) {
        ExceptionResponse errors = ExceptionResponse.builder().message(ex.getMessage()).code(4).build();
        return new ResponseEntity<>(errors, HttpStatus.OK);
    }

    @ExceptionHandler(SuccessHandler.class)
    public ResponseEntity<ExceptionResponse> handleSuccessException(SuccessHandler ex) {
        ExceptionResponse errors = ExceptionResponse.builder().message(ex.getMessage()).code(9).build();
        return new ResponseEntity<>(errors, HttpStatus.OK);
    }

    @ExceptionHandler(InValidInformation.class)
    public ResponseEntity<ExceptionResponse> inValidInformation(InValidInformation ex) {
        ExceptionResponse errors = ExceptionResponse.builder().message(ex.getMessage()).code(1).build();
        return new ResponseEntity<>(errors, HttpStatus.OK);
    }

    @ExceptionHandler(DataConfilictException.class)
    public ResponseEntity<ExceptionResponse> dataConfilictException(DataConfilictException ex) {
        ExceptionResponse errors = ExceptionResponse.builder().message(ex.getMessage()).code(2).build();
        return new ResponseEntity<>(errors, HttpStatus.OK);
    }

    @ExceptionHandler(UserNotExistException.class)
    public ResponseEntity<ExceptionResponse> userNotExistException(UserNotExistException ex) {
        ExceptionResponse errors = ExceptionResponse.builder().message(ex.getMessage()).code(3).build();
        return new ResponseEntity<>(errors, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        ExceptionResponse errors = ExceptionResponse.builder().build();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.setMessage(fieldName + ":" + errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException ex) {
        ExceptionResponse error = ExceptionResponse.builder().message(ex.getMessage()).code(5).build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleForbiddenException(ForbiddenException ex) {
        ExceptionResponse error = ExceptionResponse.builder().message(ex.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EmptyException.class)
    public ResponseEntity<ExceptionResponse> handleEmptyException(EmptyException ex) {
        ExceptionResponse error = ExceptionResponse.builder().message(ex.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            ConversionNotSupportedException.class,
            HttpMessageNotWritableException.class,
            MissingServletRequestParameterException.class,
            ServletRequestBindingException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestPartException.class,
            BindException.class,
            NoHandlerFoundException.class,
            AsyncRequestTimeoutException.class
    })
    public ResponseEntity<ExceptionResponse> handleException(Exception ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            httpStatus = HttpStatus.NOT_ACCEPTABLE;
        } else if (ex instanceof MissingPathVariableException
                || ex instanceof ConversionNotSupportedException
                || ex instanceof HttpMessageNotWritableException) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (ex instanceof NoHandlerFoundException
                || ex instanceof AsyncRequestTimeoutException) {
            httpStatus = HttpStatus.NOT_FOUND;
        }
        ExceptionResponse error = ExceptionResponse.builder().message(ex.getMessage()).build();
        return new ResponseEntity<>(error, httpStatus);
    }
}