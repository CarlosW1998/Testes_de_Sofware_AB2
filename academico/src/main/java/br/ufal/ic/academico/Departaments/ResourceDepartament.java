package br.ufal.ic.academico.Departaments;

import br.ufal.ic.academico.Secretarys.Secretary;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("departaments")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class ResourceDepartament {

    private final DepartamentDAO departamentDAO;


    @GET
    @UnitOfWork
    public Response getAll() {
        log.info("getAll");
        return Response.ok(departamentDAO.list()).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id) {

        log.info("getById: id={}", id);
        Departament p = departamentDAO.get(id);
        return Response.ok(p).build();
    }

    @POST
    @UnitOfWork
    @Consumes("application/json")
    public Response save(DepartamentDTO entity) {

        log.info("save: {}", entity);

        Departament d = new Departament(entity.getName());
        d.setCourses(entity.getCourses());
        d.setSecretaries(entity.getSecretaries());
        d.setProfessors(entity.getProfessors());
        return Response.ok(departamentDAO.persist(d)).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, DepartamentDTO entity) {

        log.info("update: id={}, {}", id, entity);

        Departament p = departamentDAO.get(id);
        p.setCourses(entity.getCourses());
        p.setSecretaries(entity.getSecretaries());
        p.setProfessors(entity.getProfessors());
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {
        log.info("delete: id={}", id);
        Long delId = departamentDAO.get(id).getId();
        departamentDAO.delete(id);
        return Response.ok(delId).build();
    }

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DepartamentDTO {
        private String name;
        private List<Long> courses;
        private List<Long> secretaries;
        private List<Long> professors;
    }
}
