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
    private int credites, courseId;
    @ManyToOne
    private Course studying;
    @OneToMany
    private List<Subject> coursing;
    @OneToMany
    private List<Subject> aproved;

    public Student(String name, Course studying){
        this.name = name;
        this.studying = studying;
        this.credites = 0;
        this.coursing = new ArrayList<Subject>();
        this.aproved = new ArrayList<Subject>();
    }


}