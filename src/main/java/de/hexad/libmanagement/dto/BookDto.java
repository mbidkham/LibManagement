package de.hexad.libmanagement.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class BookDto {

    private boolean borrowed;
    private UserDto borrowedUser;

    @NotNull
    private String name;
}
