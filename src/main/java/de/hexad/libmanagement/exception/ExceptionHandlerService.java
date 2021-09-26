package de.hexad.libmanagement.exception;

import de.hexad.libmanagement.dto.RestApiMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionHandlerService extends ResponseEntityExceptionHandler {

    private static final String DEFAULT_MESSAGE = "UNKNOWN_EXCEPTION";

    @ExceptionHandler(BorrowBookException.class)
    protected ResponseEntity<Object> handleAuthExceptions(
            BorrowBookException ex) {
        RestApiMessage apiMessage = ex.getRestApiMessage();
        switch (apiMessage) {
            case USER_NOT_FOUND:
            case BOOK_NOT_FOUND:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiMessage.getValue());
            case BOOK_IS_BORROWED:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiMessage.getValue());
            default:
                logger.error(DEFAULT_MESSAGE + ex.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }


}
