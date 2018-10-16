package br.ufal.ic.academico.Studants;
import br.ufal.ic.academico.Courses.Course;
import br.ufal.ic.academico.Subjects.Subject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private int credites;
    private int course, departament;

    @Setter
    private ArrayList<Subject> coursing;
    @Setter
    private ArrayList<Subject> aproved;

    public Student(String name,  int departament, int course){
        this.name = name;
        this.credites = 0;
        this.departament = departament;
        this.course = course;
    }


}