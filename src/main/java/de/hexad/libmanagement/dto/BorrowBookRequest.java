package de.hexad.libmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class BorrowBookRequest {

    @NotNull
    private long userId;
    @NotNull
    private long bookId;
}
