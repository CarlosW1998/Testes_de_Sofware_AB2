package br.ufal.ic.academico;

import br.ufal.ic.academico.Courses.Course;
import br.ufal.ic.academico.Courses.CourseDAO;
import br.ufal.ic.academico.Departaments.Departament;
import br.ufal.ic.academico.Departaments.DepartamentDAO;
import br.ufal.ic.academico.Professors.ProfessorDAO;
import br.ufal.ic.academico.Secretarys.Secretary;
import br.ufal.ic.academico.Secretarys.SecretaryDAO;
import br.ufal.ic.academico.Studants.Proof;
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
import java.util.ArrayList;

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
    @Path("/coursesubjects/{secretaryid}")
    @UnitOfWork
    public Response secretarySubjectList(@PathParam("secretaryid") Long id) {

        log.info("getById: id={}", id);

        Secretary secretary = secretaryDAO.get(id);
        if(secretary.equals(null))
            return Response.status(404).build();
        ArrayList<Subject> belongs = new ArrayList<Subject>();
        ArrayList<Subject> subjects = (ArrayList) subjectDAO.list();
        ArrayList<Long> secretarySubjects = secretary.getSubjects();


        for (Subject sub : subjects) {
            if(secretarySubjects.contains(sub.getId()))
                belongs.add(sub);
        }

        return Response.ok(belongs).build();
    }

    @PUT
    @Path("/matricula/{studentId}/{departamentId}/{subjectId}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("studentId") Long studentId, @PathParam("departamentId") Long departamentId, @PathParam("subjectId") Long subjectId, RequirementDTO entity) {

        log.info("update: id={}, {}", studentId, entity);

        Student student = studentDAO.get(studentId);
        Course course = courseDAO.get(student.getCourse());
        if(course.equals(null))
            return Response.status(404).build();
        if(student.equals(null))
            return Response.status(404).build();

        Departament departament = departamentDAO.get(departamentId);
        if(departament.equals(null) || (student.getDepartament() != departamentId))
            return Response.status(404).build();

        Subject subject = subjectDAO.get(subjectId);
        if(subject.equals(null))
            return Response.status(404).build();

        if(subject.getRequisitedCredits() > student.getCredites())
            return Response.status(403).build();

        if(subject.getType().equals("postgraduate") && !course.getType().equals("postgraduate") && student.getCredites() < 170)
            return Response.status(403).build();

        if(subject.getType().equals("graduate") && course.getType().equals("postgraduate"))
            return Response.status(403).build();

        ArrayList<Long> coursing = student.getCoursing();

        for (Long i: coursing ) {
            if(i.equals(subjectId))
                return Response.status(403).build();
        }

        if(!subject.getPrerequisite().isEmpty()){
            ArrayList<Long> approved = student.getAproved();
            ArrayList<Long> preRequisite = subject.getPrerequisite();

            for (Long i : preRequisite) {
                if(!approved.contains(i))
                    return Response.status(403).build();
            }
            return Response.ok(entity).build();
        }

        ArrayList<Long> output = student.getCoursing();
        output.add(subjectId);
        student.setCoursing(output);

        return Response.ok(entity).build();


    }

    @GET
    @Path("/proof/{id}")
    @UnitOfWork
    public Response getProof(@PathParam("id") Long id) {

        log.info("getById: id={}", id);

        Student student = studentDAO.get(id);

        if(student.equals(null))
            return Response.status(404).build();

        ArrayList<Subject> allSubjects = (ArrayList) subjectDAO.list();
        ArrayList<Long> studentSbjects = student.getCoursing();
        Proof myproff = new Proof(student.getName());
        ArrayList<String> belongs = new ArrayList<String>();


        for (Subject sub : allSubjects) {
            if(studentSbjects.contains(sub.getId()))
                belongs.add(sub.getName());
        }

        myproff.setSubjects(belongs);

        return Response.ok(myproff).build();
    }

    @GET
    @Path("/matricula/{id}/{departamentid}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id, @PathParam("departamentid") Long depId) {

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

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RequirementDTO {
        private Long subjectId;
    }


}
