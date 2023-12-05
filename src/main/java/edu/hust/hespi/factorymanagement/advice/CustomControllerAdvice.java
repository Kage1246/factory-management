package edu.hust.hespi.factorymanagement.advice;

import com.google.api.client.http.HttpStatusCodes;
import edu.hust.hespi.factorymanagement.exception.CustomException;
import edu.hust.hespi.factorymanagement.model.response.CustomResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomControllerAdvice extends ResponseEntityExceptionHandler {
    Logger log = LoggerFactory.getLogger(CustomControllerAdvice.class);
    @ExceptionHandler({CustomException.class})
    public ResponseEntity<?> handleCustomException(CustomException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Exception message: " + ex.getMessage());
        return handleExceptionInternal(ex, ex, headers, HttpStatus.valueOf(ex.getCode()), request);
    }
}
