package com.mikaeru.user_api.exceptionHandler;

import com.mikaeru.user_api.domain.exception.BusinessException;
import com.mikaeru.user_api.domain.exception.EntityNotFoundException;
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

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    @ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        StringBuilder msg = new StringBuilder();

        if (ex instanceof MethodArgumentNotValidException) {
            List<ObjectError> errors = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
            for (ObjectError error : errors) {
                msg.append(error.getDefaultMessage()).append("\n");
            }
        } else {
            msg.append(ex.getMessage());
        }

        var problem = new Problem();
        problem.setStatus(status.value());
        problem.setTitle(msg.toString());
        problem.setDateTime(OffsetDateTime.now());
        return new ResponseEntity<>(problem, headers, status);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class})
    protected ResponseEntity<Object> handleExceptionDataIntegrity(Exception ex) {
        String msg = "";

        if (ex instanceof DataIntegrityViolationException) {
            msg = ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();
        } else if (ex instanceof SQLException) {
            msg = ((SQLException) ex).getCause().getCause().getMessage();
        } else if (ex instanceof ConstraintViolationException) {
            msg = ((ConstraintViolationException) ex).getCause().getCause().getMessage();
        } else {
            msg = ex.getMessage();
        }

        var problem = new Problem();
        problem.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        problem.setTitle(msg);
        problem.setDateTime(OffsetDateTime.now());
        return new ResponseEntity<>(problem, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusiness(BusinessException exception, WebRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        return getHandleExceptionInternal(exception, status, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handlerEntityNotFound(BusinessException exception, WebRequest request) {
        var status = HttpStatus.NOT_FOUND;
        return getHandleExceptionInternal(exception, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var fields = new ArrayList<Problem.Field>();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String name = ((FieldError) error).getField();
            String msg = messageSource.getMessage(error, Locale.ENGLISH);

            fields.add(new Problem.Field(name, msg));
        }

        var problem = new Problem();
        problem.setFields(fields);
        problem.setStatus(status.value());
        problem.setDateTime(OffsetDateTime.now());
        problem.setTitle("one or two fields is invalid!");

        return super.handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> getHandleExceptionInternal(BusinessException exception, HttpStatus status, WebRequest request) {
        var problem = new Problem();
        problem.setStatus(status.value());
        problem.setTitle(exception.getMessage());
        problem.setDateTime(OffsetDateTime.now());
        return handleExceptionInternal(exception, problem, new HttpHeaders(), status, request);
    }
}
