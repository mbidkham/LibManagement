package de.hexad.libmanagement.exception;

import de.hexad.libmanagement.dto.RestApiMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowBookException extends RuntimeException {
    private final RestApiMessage restApiMessage;

    public BorrowBookException(RestApiMessage restApiMessage) {
        super(restApiMessage.getValue());
        this.restApiMessage = restApiMessage;
    }
}

