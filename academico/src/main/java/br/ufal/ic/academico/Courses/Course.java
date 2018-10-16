package br.ufal.ic.academico.Courses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String type;

    private int departament;
    private  int secretary;
    @Setter
    private ArrayList<Long> subjects;
    @Setter
    private ArrayList<Long> students;

    public Course(String name, String Type, int departament, int secretary){
        this.name = name;
        this.type = type;
        this.departament = departament;
        this.secretary = secretary;
    }
}
