package org.example.workingmoney.ui.controller.common;

import org.example.workingmoney.service.auth.exception.AuthExceptionDescription;
import org.example.workingmoney.service.common.exception.CommonExceptionDescription;
import org.example.workingmoney.service.common.exception.CustomException;
import org.example.workingmoney.service.common.exception.InvalidFormatException;
import org.example.workingmoney.service.common.exception.UnknownException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Response<Void>> handleCustomException(CustomException exception) {
        HttpStatus httpStatus = mapToHttpStatus(exception);
        return new ResponseEntity<>(Response.error(exception), httpStatus);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<Void>> handleIllegalArgumentException(IllegalArgumentException exception) {
        return new ResponseEntity<>(Response.error(exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Void> handleValidationExceptions(Exception exception) {
        String message = null;
        if (exception instanceof MethodArgumentNotValidException ex) {
            message = ex.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .filter(m -> m != null && !m.isBlank())
                    .distinct()
                    .reduce((a, b) -> a + " " + b)
                    .orElse(null);

            return Response.error(new InvalidFormatException(message));
        }

        return Response.error(new InvalidFormatException());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response<Void> handleNoResourceFoundException(Exception exception) {
        return Response.error(new IllegalArgumentException(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Void> handleUnknown(Exception exception) {
        return Response.error(new UnknownException(exception.getMessage()));
    }

    private HttpStatus mapToHttpStatus(CustomException exception) {
        var type = exception.getExceptionDescription();

        if (type instanceof CommonExceptionDescription common) {
            return switch (common) {
                case UNKNOWN -> HttpStatus.INTERNAL_SERVER_ERROR;
                case INVALID_FORMAT -> HttpStatus.BAD_REQUEST;
            };
        }
        if (type instanceof AuthExceptionDescription auth) {
            return switch (auth) {
                case DUPLICATED_EMAIL, DUPLICATED_NICKNAME -> HttpStatus.BAD_REQUEST;
            };
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}

