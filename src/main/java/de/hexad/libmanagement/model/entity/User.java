package de.hexad.libmanagement.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class User {

    @Id
    private long id;
    @NotNull
    private String name;

}
