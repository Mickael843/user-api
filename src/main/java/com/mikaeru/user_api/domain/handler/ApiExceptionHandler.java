package com.mikaeru.user_api.domain.handler;

import com.mikaeru.user_api.domain.exception.DomainException;
import com.mikaeru.user_api.domain.exception.DuplicatedDataException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired private MessageSource messageSource;

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException exception, WebRequest request) {
        Problem problem = new Problem();

        problem.setStatus(BAD_REQUEST.value());
        problem.setTitle(exception.getMessage());
        problem.setDateTime(OffsetDateTime.now());

        return handleExceptionInternal(exception, problem, new HttpHeaders(), BAD_REQUEST, request);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request) {
        Problem problem = new Problem();

        problem.setStatus(NOT_FOUND.value());
        problem.setTitle(exception.getMessage());
        problem.setDateTime(OffsetDateTime.now());

        return handleExceptionInternal(exception, problem, new HttpHeaders(), NOT_FOUND, request);
    }

    @ExceptionHandler({DomainException.class})
    protected ResponseEntity<Object> handleDomainException(DomainException exception, WebRequest request) {
        return getHandleExceptionInternal(exception, INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({DuplicatedDataException.class})
    protected ResponseEntity<Object> handleArgumentNotValidException(DuplicatedDataException exception, WebRequest request) {
        Problem problem = new Problem();

        List<Problem.Field> problemFields = new ArrayList<>();

        for (int i = 0; i < exception.getFields().size(); i++) {
            Problem.Field field = new Problem.Field(exception.getFields().get(i), exception.getMessage());
            problemFields.add(field);
        }

        problem.setFields(problemFields);
        problem.setStatus(BAD_REQUEST.value());
        problem.setTitle(exception.getMessage());
        problem.setDateTime(OffsetDateTime.now());

        return handleExceptionInternal(exception, problem, new HttpHeaders(), BAD_REQUEST, request);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class})
    protected ResponseEntity<Object> handleExceptionDataIntegrity(Exception exception) {
        String msg;

        if (exception instanceof DataIntegrityViolationException) {
            msg = ((DataIntegrityViolationException) exception).getMessage();
        } else {
            msg = exception.getMessage();
        }

        Problem problem = new Problem();

        problem.setTitle(msg);
        problem.setDateTime(OffsetDateTime.now());
        problem.setStatus(BAD_REQUEST.value());

        return new ResponseEntity<>(problem, BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var fields = new ArrayList<Problem.Field>();

        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            String name = ((FieldError) error).getField();
            String msg = messageSource.getMessage(error, Locale.ENGLISH);

            fields.add(new Problem.Field(name, msg));
        }

        Problem problem = new Problem();

        problem.setFields(fields);
        problem.setStatus(status.value());
        problem.setDateTime(OffsetDateTime.now());
        problem.setTitle("Invalid fields!");

        return super.handleExceptionInternal(exception, problem, headers, status, request);
    }

    private ResponseEntity<Object> getHandleExceptionInternal(DomainException exception, HttpStatus status, WebRequest request) {
        Problem problem = new Problem();

        problem.setStatus(status.value());
        problem.setTitle(exception.getMessage());
        problem.setDateTime(OffsetDateTime.now());

        return handleExceptionInternal(exception, problem, new HttpHeaders(), status, request);
    }
}
