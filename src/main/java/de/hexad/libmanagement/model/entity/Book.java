package de.hexad.libmanagement.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    private long id;

    private boolean borrowed;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cloned_book_id")
    private Book parentBook;

    @NotNull
    private String name;

}
