package br.ufal.ic.academico.CRUDTest;

import br.ufal.ic.academico.AcademicoApp;
import br.ufal.ic.academico.ConfigApp;
import br.ufal.ic.academico.Professors.Professor;
import br.ufal.ic.academico.Professors.ProfessorDAO;
import br.ufal.ic.academico.Professors.ResourceProfessor;
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
public class ProfessorsTest {
    static {
        BootstrapLogging.bootstrap(Level.DEBUG);
    }

    private ProfessorDAO professorDAO = mock(ProfessorDAO.class);

    private final ResourceProfessor resourceProfessor = new ResourceProfessor(professorDAO);

    public static DropwizardAppExtension<ConfigApp> RULE = new DropwizardAppExtension(AcademicoApp.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));

    private String academic = "academicotest";

    @Test
    public void testDelete(){

        List<Professor> Allprofessors = RULE.client().target(
                String.format("http://localhost:%d/%s/professors", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Professor>>() {});

        assertTrue(Allprofessors.size() > 0);

        Long id = Allprofessors.get(0).getId();
        int size = Allprofessors.size();

        Long removed = RULE.client().target(
                String.format("http://localhost:%d/%s/professors/%d", RULE.getLocalPort(), academic, id))
                .request()
                .delete(new GenericType<Long>() {});

        assertEquals(id, removed);

        Allprofessors = RULE.client().target(
                String.format("http://localhost:%d/%s/professors", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Professor>>() {});

        id = Allprofessors.get(0).getId();

        RULE.client().target(
                String.format("http://localhost:%d/%s/professors/%d", RULE.getLocalPort(), academic, id))
                .request()
                .delete(new GenericType<Long>() {});
        Allprofessors = RULE.client().target(
                String.format("http://localhost:%d/%s/professors", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Professor>>() {});
        int newSize = Allprofessors.size();

        assertEquals(newSize, size-2);
    }

    @Test
    public void testList() {

        List<Professor> allProfessors = RULE.client().target(
                String.format("http://localhost:%d/%s/professors", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Professor>>() {});

        assertTrue(allProfessors.size() > 0);
    }
    @Test
    public void testPost(){
        Professor professor = new Professor("willy tiengo", 15);
        professor.setTeaching(new ArrayList<Long>());

        Professor postedProfessor = RULE.client().target(
                String.format("http://localhost:%d/%s/professors", RULE.getLocalPort(), academic))
                .request()
                .post(Entity.json(professor), Professor.class);

        assertAll(
                () -> {
                    assertNotNull(postedProfessor);
                    assertNotNull(postedProfessor.getId());
                    assertNotNull(postedProfessor.getDepartament());
                },
                () -> {
                    assertEquals("Willy Tiengo", postedProfessor.getName());
                    assertEquals(15, postedProfessor.getDepartament());
                },
                () -> {
                    assertNotNull(postedProfessor.getTeaching());
                    assertEquals(0, postedProfessor.getTeaching().size());
                }
        );
    }

    @Test
    public void testPut(){
        Professor professor = new Professor("Computacao", 19);
        professor.setTeaching(new ArrayList<Long>());

        Professor protedProfessor = RULE.client().target(
                String.format("http://localhost:%d/%s/professors", RULE.getLocalPort(), academic))
                .request()
                .post(Entity.json(professor), Professor.class);

        assertNotNull(protedProfessor);
        assertNotNull(protedProfessor.getId());
        Long id = protedProfessor.getId();

        ArrayList<Long> Teachings = new ArrayList<Long>();
        Teachings.add(new Long(9));
        Teachings.add(new Long(14));
        Teachings.add(new Long(12));
        Teachings.add(new Long(21));

        professor.setTeaching(Teachings);

        Professor updateProfessor = RULE.client().target(
                String.format("http://localhost:%d/%s/professors/%d", RULE.getLocalPort(), academic, id))
                .request()
                .put(Entity.json(professor), Professor.class);

        assertAll(
                () -> {
                    assertNotNull(updateProfessor);
                    assertNotNull(updateProfessor.getTeaching());
                },
                () -> {
                    assertEquals(4, updateProfessor.getTeaching().size());
                    assertEquals(new Long(9), updateProfessor.getTeaching().get(0));
                    assertEquals(new Long(14), updateProfessor.getTeaching().get(1));
                    assertEquals(new Long(12), updateProfessor.getTeaching().get(2));
                    assertEquals(new Long(21), updateProfessor.getTeaching().get(3));
                }
        );
    }

    @Test
    public void testSave() {

        Professor professor = new Professor("Computacao", 19);
        professor.setTeaching(new ArrayList<Long>());

        Professor posted = RULE.client().target(
                String.format("http://localhost:%d/%s/professors", RULE.getLocalPort(), academic))
                .request()
                .post(Entity.json(professor), Professor.class);

        assertNotNull(posted);
        assertNotNull(posted.getId());

        List<Professor> allProfessors = RULE.client().target(
                String.format("http://localhost:%d/%s/professors", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Professor>>() {});

        assertTrue(allProfessors.size() > 0, "A lista está vazia");

        //GET COM ID
        Long id = allProfessors.get(0).getId();

        Professor getProf = RULE.client().target(
                String.format("http://localhost:%d/%s/professors/%d", RULE.getLocalPort(), academic, id))
                .request()
                .get(new GenericType<Professor>() {});

        assertNotNull(getProf);
        assertEquals(id, getProf.getId());


    }


}
