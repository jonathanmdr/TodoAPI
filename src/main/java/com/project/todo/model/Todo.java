package com.project.todo.model;

import com.project.todo.model.utils.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tarefa")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @Size(min = 5, max = 60)
    @Column(name = "titulo")
    private String title;

    @Column(name = "descricao")
    @Size(min = 0, max = 255)
    private String description;

    @Column(name = "situacao")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Column(name = "data_criacao")
    private LocalDate creationDate;

    @Column(name = "data_edicao")
    private LocalDate updatedDate;

    @Column(name = "data_conclusao")
    private LocalDate conclusedDate;

}
