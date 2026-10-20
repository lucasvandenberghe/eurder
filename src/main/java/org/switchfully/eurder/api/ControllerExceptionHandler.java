package org.switchfully.eurder.api;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.switchfully.eurder.domain.exceptions.CustomerException;
import org.switchfully.eurder.services.exceptions.CustomerServiceException;

import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(CustomerException.class)
    protected void handleCustomerException(CustomerException ex, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(CustomerServiceException.class)
    protected void handleCustomerServiceException(CustomerServiceException ex, HttpServletResponse response) throws IOException {
        response.sendError(INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }
}
