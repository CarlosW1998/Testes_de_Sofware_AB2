package br.ufal.ic.academico.Studants;

import br.ufal.ic.academico.Professors.Professor;
//import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Proof {
    private String name;

    @Setter
    private ArrayList<String> subjects;

    public Proof(String name){
        this.name = name;

    }
}
