package br.ufal.ic.academico.CRUDTest;

import br.ufal.ic.academico.AcademicoApp;
import br.ufal.ic.academico.ConfigApp;
import br.ufal.ic.academico.Departaments.Departament;
import br.ufal.ic.academico.Departaments.DepartamentDAO;
import br.ufal.ic.academico.Departaments.ResourceDepartament;
import ch.qos.logback.classic.Level;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@ExtendWith(DropwizardExtensionsSupport.class)
public class DepartamentTest {
    static {
        BootstrapLogging.bootstrap(Level.DEBUG);
    }

    private DepartamentDAO ResourceDepartament = mock(DepartamentDAO.class);

    private final ResourceDepartament resourceDepartament = new ResourceDepartament(ResourceDepartament);

    public static DropwizardAppExtension<ConfigApp> RULE = new DropwizardAppExtension(AcademicoApp.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));

    private String academic = "academicotest";

    @Test
    public void testDelete(){

        List<Departament> allDepartaments = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Departament>>() {});

        assertTrue(allDepartaments.size() > 0);

        Long id = allDepartaments.get(0).getId();
        int size = allDepartaments.size();

        Long removed = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments/%d", RULE.getLocalPort(), academic, id))
                .request()
                .delete(new GenericType<Long>() {});

        assertEquals(id, removed);

        List<Departament> newAllDepartaments = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Departament>>() {});

        int actualsize = newAllDepartaments.size();

        assertEquals(actualsize, size-1);
    }
    @Test
    public void testSave() {

        Departament departament = new Departament("Matematica");
        departament.setCourses(new ArrayList<Long>());
        departament.setSecretaries(new ArrayList<Long>());
        departament.setProfessors(new ArrayList<Long>());

        Departament postedDepartament = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments", RULE.getLocalPort(), academic))
                .request()
                .post(Entity.json(departament), Departament.class);

        assertNotNull(postedDepartament);
        assertNotNull(postedDepartament.getId());

        List<Departament> allDepartaments = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Departament>>() {});

        assertTrue(allDepartaments.size() > 0, "A lista está vazia");

        //GET COM ID
        Long id = allDepartaments.get(0).getId();

        Departament getDepartament = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments/%d", RULE.getLocalPort(), academic, id))
                .request()
                .get(new GenericType<Departament>() {});

        assertNotNull(getDepartament, "Objeto retornou null");
        assertEquals(id, getDepartament.getId(), "Os Ids não batem");


    }

    @Test
    public void testList() {

        List<Departament> allDepartaments = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Departament>>() {});

        assertTrue(allDepartaments.size() > 0);
    }

    @Test
    public void testPost(){
        Departament departament = new Departament("Matematica");
        departament.setCourses(new ArrayList<Long>());
        departament.setSecretaries(new ArrayList<Long>());
        departament.setProfessors(new ArrayList<Long>());

        Departament postedDepartament = RULE.client().target(
                String.format("http://localhost:%d/%s/departaments", RULE.getLocalPort(), academic))
                .request()
                .post(Entity.json(departament), Departament.class);

        assertAll(
                () -> {
                    assertNotNull(postedDepartament);
                    assertNotNull(postedDepartament.getId());
                },
                () -> {
                    assertEquals("Computacao", postedDepartament.getName());
                },
                () -> {
                    assertNotNull(postedDepartament.getCourses());
                    assertEquals(0, postedDepartament.getCourses().size());
                },
                () -> {
                    assertNotNull(postedDepartament.getProfessors());
                    assertEquals(0, postedDepartament.getProfessors().size());
                },
                () -> {
                    assertNotNull(postedDepartament.getSecretaries());
                }
        );
    }


}
