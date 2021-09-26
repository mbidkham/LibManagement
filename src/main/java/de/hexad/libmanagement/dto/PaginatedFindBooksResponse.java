package de.hexad.libmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PaginatedFindBooksResponse {
    private List<BookDto> bookDtoList;
    private int pageSize;
    private int pageNumber;
    private long totalElements;
}
