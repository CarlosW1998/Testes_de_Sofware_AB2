package br.ufal.ic.academico.Courses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("courses")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class ResourceCourse {

    private final CourseDAO courseDAO;

    @GET
    @UnitOfWork
    public Response getAll() {
        log.info("getAll");
        return Response.ok(courseDAO.list()).build();
    }


    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id) {
        log.info("getById: id={}", id);
        Course c = courseDAO.get(id);
        return Response.ok(c).build();
    }

    @POST
    @UnitOfWork
    @Consumes("application/json")
    public Response save(CourseDTO entity) {

        log.info("save: {}", entity);

        Course p = new Course(entity.getName(), entity.getType(), entity.getDepartament(), entity.getSecretary());
        p.setSubjects(entity.getSubjects());
        p.setStudents(entity.getStudents());

        return Response.ok(courseDAO.persist(p)).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, CourseDTO entity) {

        log.info("update: id={}, {}", id, entity);

        Course p = courseDAO.get(id);
        p.setSubjects(entity.getSubjects());
        p.setStudents(entity.getStudents());
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {

        log.info("delete: id={}", id);
        Long delId = courseDAO.get(id).getId();
        courseDAO.delete(id);
        return Response.ok(delId).build();
    }


    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CourseDTO {
        private String name;
        private String type;
        private int departament;
        private  int secretary;

        private ArrayList<Long> subjects;
        private ArrayList<Long> students;
    }
}
