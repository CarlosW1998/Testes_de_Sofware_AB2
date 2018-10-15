package br.ufal.ic.academico.Secretarys;

import br.ufal.ic.academico.Courses.Course;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Secretary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String type;

    @OneToMany
    List<Course> courses;

    public Secretary(String name,String type, ArrayList<Course> courses) {
        this.name = name;
        this.type = type;
        this.courses = courses;
    }
}
