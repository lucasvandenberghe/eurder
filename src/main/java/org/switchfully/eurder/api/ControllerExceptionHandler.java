package org.switchfully.eurder.api;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.switchfully.eurder.domain.exceptions.CustomerException;
import org.switchfully.eurder.services.exceptions.CustomerNotFoundException;
import org.switchfully.eurder.services.exceptions.CustomerServiceException;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(CustomerException.class)
    protected void handleCustomerException(CustomerException ex, HttpServletResponse response) throws IOException {
        logger.warn("Unable to create Customer", ex);

        response.sendError(BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    protected void handleCustomerNotFoundException(CustomerNotFoundException ex, HttpServletResponse response) throws IOException {
        logger.warn("Unable to find Customer", ex);

        response.sendError(NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(CustomerServiceException.class)
    protected void handleCustomerServiceException(CustomerServiceException ex, HttpServletResponse response) throws IOException {
        logger.warn("Unable to execute CustomerService method", ex);

        response.sendError(INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }
}
