package br.ufal.ic.academico.Studants;

import br.ufal.ic.academico.Subjects.Subject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("students")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class ResourceStudent {

    private final StudentDAO studentDAO;

    @GET
    @UnitOfWork
    public Response getAll() {
        log.info("getAll");
        return Response.ok(studentDAO.list()).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id) {
        log.info("getById: id={}", id);
        Student p = studentDAO.get(id);
        return Response.ok(p).build();
    }

    @POST
    @UnitOfWork
    @Consumes("application/json")
    public Response save(StudentDTO entity) {

        log.info("save: {}", entity);

        Student p = new Student(entity.getName(),  entity.getDepartament(), entity.getCourse());
        p.setCoursing(entity.getCoursing());
        p.setAproved(entity.getAproved());

        return Response.ok(studentDAO.persist(p)).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, StudentDTO entity) {

        log.info("update: id={}, {}", id, entity);

        Student p = studentDAO.get(id);
        p.setCoursing(entity.getCoursing());
        p.setAproved(entity.getAproved());
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {
        log.info("delete: id={}", id);
        studentDAO.delete(id);
        return Response.ok(id).build();
    }


    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StudentDTO {

        private String name;
        private int credites;
        private int course, departament;
        private ArrayList<Subject> coursing;
        private ArrayList<Subject> aproved;
    }

}
