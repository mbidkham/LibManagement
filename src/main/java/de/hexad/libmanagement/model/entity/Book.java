package de.hexad.libmanagement.model.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Book {

    @Id
    private long id;

    private boolean borrowed;

    @OneToOne()
    @JoinColumn(name = "USER_ID")
    private User borrowedUser;

    @NotNull
    private String name;


}
