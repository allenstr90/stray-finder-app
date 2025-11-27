package aem.java.strayfinder.errors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>(ex.getConstraintViolations().size());
        ex.getConstraintViolations().forEach(e -> {
            errors.add(e.getPropertyPath() + ": " + e.getMessage());
        });
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> {
            errors.add(e.getField() + ": " + e.getDefaultMessage());
        });
        return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(StrayNotFoundException.class)
    public ResponseEntity<Object> handleBeerNotFound(StrayNotFoundException e, WebRequest request) {
        return handleExceptionInternal(e, Collections.singletonList(e.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(InvalidImageReferenceException.class)
    public ResponseEntity<Object> handleInvalidImageReference(InvalidImageReferenceException e, WebRequest request) {
        return handleExceptionInternal(e, Collections.singletonList(e.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<Object> handleImageNotFound(ImageNotFoundException e, WebRequest request) {
        return handleExceptionInternal(e, Collections.singletonList(e.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
