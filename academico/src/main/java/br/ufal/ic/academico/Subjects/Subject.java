package br.ufal.ic.academico.Subjects;

import br.ufal.ic.academico.Courses.Course;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
    @ManyToMany(cascade =  {CascadeType.ALL})
    private List<Subject> prerequisite;
    @ManyToMany
    private List<Course> courses;
    private int credites, requisitedCredits;
    private String type;
    private String professor;

    public Subject(String name, ArrayList<Subject> prerequisite, ArrayList<Course> couses, int credites, int requisitedCredits, String type, String professor){
        this.name = name;
        this.prerequisite = prerequisite;
        this.credites = credites;
        this.requisitedCredits = requisitedCredits;
        this.type = type;
        this.professor = professor;
        this.courses = couses;
    }

}
