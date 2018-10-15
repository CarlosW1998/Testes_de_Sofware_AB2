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

    @OneToOne
    private Secretary[] secretaries;

    public Departament (String name, Secretary graduate, Secretary postgraduate){
        this.name = name;
        this.secretaries = new Secretary[2];
        this.secretaries[0] = graduate;
        this.secretaries[1] = postgraduate;
    }

}
