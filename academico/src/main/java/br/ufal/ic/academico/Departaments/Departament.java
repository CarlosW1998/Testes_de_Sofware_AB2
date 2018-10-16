package br.ufal.ic.academico.Departaments;

import br.ufal.ic.academico.Secretarys.Secretary;
import br.ufal.ic.academico.exemplos.Log;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Departament {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Setter
    private List<Long> courses;
    @Setter
    private List<Long> secretaries;
    @Setter
    private List<Long> professors;

    public Departament (String name){
        this.name = name;
    }

}
