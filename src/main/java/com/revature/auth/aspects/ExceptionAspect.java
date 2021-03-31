package com.revature.auth.aspects;

import com.revature.auth.exceptions.UnauthorizedException;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Component
@Aspect
@RestControllerAdvice
public class ExceptionAspect {
    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<String> handleEntityNotFound(NoSuchElementException e){
        return error(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleEntityNotFound(IllegalArgumentException e){
        return error(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler({ChangeSetPersister.NotFoundException.class})
    public ResponseEntity<String> handleEntityNotFound(ChangeSetPersister.NotFoundException e){
        return error(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e){
        return error(HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler({DuplicateKeyException.class})
    public ResponseEntity<String> handleDuplicateKeyException(DuplicateKeyException e){
        return error(HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException e){
        return error(HttpStatus.UNAUTHORIZED, e);
    }

//    @ExceptionHandler({SQLException.class})
//    public ResponseEntity<String> handleInternalServerErrorException(SQLException e){
//        return error(HttpStatus.CONFLICT, e);
//    }
//    @ExceptionHandler({PSQLException.class})
//    public ResponseEntity<String> handleRuntimeException(PSQLException e){
//        return error(HttpStatus.CONFLICT, e);
//    }

    private ResponseEntity<String> error(HttpStatus status, Exception e){
        // TODO add logger here
        return ResponseEntity.status(status).body(e.getMessage());
    }
}
