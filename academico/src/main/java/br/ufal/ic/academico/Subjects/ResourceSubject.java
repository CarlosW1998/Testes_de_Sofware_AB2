package br.ufal.ic.academico.Subjects;

import br.ufal.ic.academico.Studants.Student;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("subjects")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class ResourceSubject {

    private final SubjectDAO subjectDAO;

    @GET
    @UnitOfWork
    public Response getAll() {

        log.info("getAll");

        return Response.ok(subjectDAO.list()).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id) {

        log.info("getById: id={}", id);

        Subject p = subjectDAO.get(id);

        return Response.ok(p).build();
    }

    @POST
    @UnitOfWork
    @Consumes("application/json")
    public Response save(SubjectDTO entity) {

        log.info("save: {}", entity);

        Subject p = new Subject(entity.getName(), entity.getCourses(), entity.getCredites(), entity.getRequisitedCredits(), entity.getType(), entity.getProfessor(), entity.getDepartament());

        p.setStudants(entity.getStudants());
        p.setPrerequisite(entity.getPrerequisite());


        return Response.ok(subjectDAO.persist(p)).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, SubjectDTO entity) {

        log.info("update: id={}, {}", id, entity);

        Subject p = subjectDAO.get(id);
        p.setStudants(entity.getStudants());

        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {
        log.info("delete: id={}", id);
        subjectDAO.delete(id);
        return Response.ok(id).build();
    }


    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SubjectDTO {

        private String name;
        private Long courses;
        private Long departament;
        private int credites, requisitedCredits;
        private String type;
        private String professor;
        private ArrayList<Long> prerequisite;
        private ArrayList<Long> studants;
    }


}
