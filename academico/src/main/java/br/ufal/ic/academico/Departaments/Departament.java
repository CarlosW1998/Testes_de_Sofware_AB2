package br.ufal.ic.academico.Departaments;

import br.ufal.ic.academico.Secretarys.Secretary;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class Departament {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Setter
    private int[] courses;
    @Setter
    private Secretary[] secretaries;
    @Setter
    private int professors;

    public Departament (String name){
        this.name = name;
    }

}
