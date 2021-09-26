package de.hexad.libmanagement.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private long id;
    @NotNull
    private String name;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "USER_BORROWED")
    private List<Book> borrowedBooks;

}
