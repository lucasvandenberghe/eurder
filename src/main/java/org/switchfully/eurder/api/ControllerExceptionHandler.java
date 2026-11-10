package org.switchfully.eurder.api;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.switchfully.eurder.service.exception.NotFoundException;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    protected void handleNotFoundException(NotFoundException e, HttpServletResponse response) throws IOException {
        logger.warn(e.getMessage());

        response.sendError(NOT_FOUND.value(), e.getMessage());
    }
}
