package br.ufal.ic.academico;

import br.ufal.ic.academico.Courses.Course;
import br.ufal.ic.academico.Courses.CourseDAO;
import br.ufal.ic.academico.Departaments.Departament;
import br.ufal.ic.academico.Departaments.DepartamentDAO;
import br.ufal.ic.academico.Professors.ProfessorDAO;
import br.ufal.ic.academico.Secretarys.Secretary;
import br.ufal.ic.academico.Secretarys.SecretaryDAO;
import br.ufal.ic.academico.Studants.Student;
import br.ufal.ic.academico.Studants.StudentDAO;
import br.ufal.ic.academico.Subjects.Subject;
import br.ufal.ic.academico.Subjects.SubjectDAO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Path("requirements")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class ResourceSystemRequiriments {

    private final SubjectDAO subjectDAO;
    private final StudentDAO studentDAO;
    private final SecretaryDAO secretaryDAO;
    private final ProfessorDAO professorDAO;
    private final DepartamentDAO departamentDAO;
    private final CourseDAO courseDAO;


    @GET
    @Path("/registry")
    @UnitOfWork
    public Response getAll() {

        log.info("getAll students");

        return Response.ok(studentDAO.list()).build();
    }


    @GET
    @Path("/coursesubjects/{secretary}")
    @UnitOfWork
    public Response secretaryList(@PathParam("secretary") Long id) {

        log.info("getById: id={}", id);

        Secretary secretary = secretaryDAO.get(id);
        if(secretary.equals(null))
            return Response.status(404).build();
        ArrayList<Subject> belongs = new ArrayList<Subject>();
        ArrayList<Subject> subject = (ArrayList) subjectDAO.list();
        ArrayList<Long> subIds = sec.getSubjects();


        for (Subject subj : subject) {
            if(subIds.contains(subj.getId()))
                exit.add(subj);
        }

        return Response.ok(exit).build();
    }


    @GET
    @Path("/matricula/{id}/{depId}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id, @PathParam("depId") Long depId) {

        log.info("getById: id={}", id);

        Student student = studentDAO.get(id);

        if(student.equals(null))
            return Response.status(404).build();
        Departament departament = departamentDAO.get(depId);
        if(departament.equals(null) || (student.getDepartament() != depId))
            return Response.status(404).build();
        ArrayList<Subject> allSubjects = (ArrayList<Subject>) subjectDAO.list();
        ArrayList<Subject> belongs =  new ArrayList<Subject>();
        for(Subject subject : allSubjects){
            if(subject.getDepartament() == depId)
                belongs.add(subject);
        }

        return Response.ok(belongs).build();
    }


}
