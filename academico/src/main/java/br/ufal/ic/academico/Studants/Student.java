package br.ufal.ic.academico.Studants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Getter
@RequiredArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @Setter
    private int credites;
    private int course, departament;

    @Setter
    private ArrayList<Long> coursing;
    @Setter
    private ArrayList<Long> aproved;

    public Student(String name,  int departament, int course){
        this.name = name;
        this.credites = 0;
        this.departament = departament;
        this.course = course;
    }


}