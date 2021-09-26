package de.hexad.libmanagement.exception;

import de.hexad.libmanagement.dto.RestApiMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BorrowBookException extends RuntimeException {
    private final RestApiMessage restApiMessage;

}

