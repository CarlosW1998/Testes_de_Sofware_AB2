package br.ufal.ic.academico.CRUDTest;

import br.ufal.ic.academico.AcademicoApp;
import br.ufal.ic.academico.ConfigApp;
import br.ufal.ic.academico.Secretarys.ResourceSecretary;
import br.ufal.ic.academico.Secretarys.Secretary;
import br.ufal.ic.academico.Secretarys.SecretaryDAO;
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
public class SecretaryTest {
    static {
        BootstrapLogging.bootstrap(Level.DEBUG);
    }

    private SecretaryDAO secretaryDAO = mock(SecretaryDAO.class);

    private final ResourceSecretary resourceSecretary = new ResourceSecretary(secretaryDAO);

    public static DropwizardAppExtension<ConfigApp> RULE = new DropwizardAppExtension(AcademicoApp.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));



    private String academic = "academicotest";

    @Test
    public void testSave() {

        Secretary secretary = new Secretary("IC SECRETARY", "PostGraduate", new Long(19));
        secretary.setSubjects(new ArrayList<Long>());

        Secretary postedSecretary = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries", RULE.getLocalPort(), academic))
                .request()
                .post(Entity.json(secretary), Secretary.class);

        assertNotNull(postedSecretary);
        assertNotNull(postedSecretary.getId());

        List<Secretary> allSecretarys = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Secretary>>() {});

        assertTrue(allSecretarys.size() > 0);

        Long id = allSecretarys.get(0).getId();

        Secretary secretaryGet = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries/%d", RULE.getLocalPort(), academic, id))
                .request()
                .get(new GenericType<Secretary>() {});

        assertNotNull(secretaryGet);
        assertEquals(id, secretaryGet.getId());
    }


    @Test
    public void testDelete(){

        List<Secretary> secretaryList = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Secretary>>() {});

        assertTrue(secretaryList.size() > 0);

        Long id = secretaryList.get(0).getId();
        int size = secretaryList.size();

        Long remove = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries/%d", RULE.getLocalPort(), academic, id))
                .request()
                .delete(new GenericType<Long>() {});

        assertEquals(id, remove);

        List<Secretary> finalListSecretays = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Secretary>>() {});

        int newSize = finalListSecretays.size();

        assertEquals(newSize, size-1);
    }

    @Test
    public void testList() {

        List<Secretary> allSecretays = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Secretary>>() {});

        assertTrue(allSecretays.size() > 0);
    }

    @Test
    public void testPost(){
        Secretary secretary = new Secretary("IC SECRETARY", "Graduate", new Long(19));
        secretary.setSubjects(new ArrayList<Long>());

        Secretary sec = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries", RULE.getLocalPort(), academic))
                .request()
                .post(Entity.json(secretary), Secretary.class);

        assertAll(
                () -> {
                    assertNotNull(sec);
                    assertNotNull(sec.getId());
                    assertNotNull(sec.getDepartament());
                },
                () -> {
                    assertEquals("IC SECRETARY", sec.getName());
                    assertEquals(new Long(19), sec.getDepartament());
                    assertEquals("Graduate", sec.getType());
                },
                () -> {
                    assertNotNull(sec.getSubjects());
                    assertEquals(0, sec.getSubjects().size());
                }
        );
    }
    @Test
    public void testPut(){
        //FAZENDO POST
        Secretary secretary = new Secretary("Matematica", "Grauate", new Long(69));
        secretary.setSubjects(new ArrayList<Long>());

        Secretary postedSecretary = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries", RULE.getLocalPort(), academic))
                .request()
                .post(Entity.json(secretary), Secretary.class);

        //FAZENDO O UPDATE
        assertNotNull(postedSecretary);
        assertNotNull(postedSecretary.getId());
        Long id = postedSecretary.getId();

        ArrayList<Long> subjects = new ArrayList<Long>();
        subjects.add(new Long(10));
        subjects.add(new Long(25));
        subjects.add(new Long(19));

        secretary.setSubjects(subjects);

        Secretary updated = RULE.client().target(
                String.format("http://localhost:%d/%s/secretaries/%d", RULE.getLocalPort(), academic, id))
                .request()
                .put(Entity.json(secretary), Secretary.class);

        assertAll(
                () -> {
                    assertNotNull(updated);
                    assertNotNull(updated.getSubjects());
                },
                () -> {
                    assertEquals(3, updated.getSubjects().size());
                    assertEquals(new Long(10), updated.getSubjects().get(0));
                    assertEquals(new Long(25), updated.getSubjects().get(1));
                    assertEquals(new Long(19), updated.getSubjects().get(2));
                }
        );
    }
}
