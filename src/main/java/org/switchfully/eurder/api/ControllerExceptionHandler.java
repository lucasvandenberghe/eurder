package org.switchfully.eurder.api;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.switchfully.eurder.domain.exception.CustomerException;
import org.switchfully.eurder.domain.exception.ItemException;
import org.switchfully.eurder.service.exception.ItemServiceException;
import org.switchfully.eurder.service.exception.NotFoundException;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    protected void handleNotFoundException(NotFoundException e, HttpServletResponse response) throws IOException {
        logger.warn(e.getMessage());

        response.sendError(NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(CustomerException.class)
    protected void handleCustomerException(CustomerException ex, HttpServletResponse response) throws IOException {
        logger.warn("Unable to create Customer", ex);

        response.sendError(BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(ItemException.class)
    protected void handleItemException(ItemException ex, HttpServletResponse response) throws IOException {
        logger.warn("Unable to create Item", ex);

        response.sendError(BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(ItemServiceException.class)
    protected void handleItemServiceException(ItemServiceException ex, HttpServletResponse response) throws IOException {
        logger.warn("Unable to execute ItemService method", ex);

        response.sendError(INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }
}
