package br.ufal.ic.academico.Subjects;

import br.ufal.ic.academico.Courses.Course;
import br.ufal.ic.academico.Studants.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@RequiredArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private Long courses;
    private Long departament;
    private int credites, requisitedCredits;
    private String type;
    private String professor;
    @Setter
    private ArrayList<Long> prerequisite;
    @Setter
    private ArrayList<Long> studants;

    public Subject(String name, Long couses, int credites, int requisitedCredits, String type, String professor, Long departament){
        this.name = name;
        this.credites = credites;
        this.requisitedCredits = requisitedCredits;
        this.type = type;
        this.professor = professor;
        this.courses = couses;
        this.departament = departament;
    }

}
