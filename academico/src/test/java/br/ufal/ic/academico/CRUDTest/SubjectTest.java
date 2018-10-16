package br.ufal.ic.academico.CRUDTest;

import br.ufal.ic.academico.AcademicoApp;
import br.ufal.ic.academico.ConfigApp;
import br.ufal.ic.academico.Studants.Student;
import br.ufal.ic.academico.Subjects.ResourceSubject;
import br.ufal.ic.academico.Subjects.Subject;
import br.ufal.ic.academico.Subjects.SubjectDAO;
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
public class SubjectTest {

    private SubjectDAO subjectDAO = mock(SubjectDAO.class);

    private final ResourceSubject resourceSubject = new ResourceSubject(subjectDAO);

    public static DropwizardAppExtension<ConfigApp> RULE = new DropwizardAppExtension(AcademicoApp.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));



    private String academic = "academicotest";

    @Test
    public void deleteTest(){
        List<Subject> subjectsList = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Subject>>() {});

        assertTrue(subjectsList.size() > 0);

        Long id = subjectsList.get(0).getId();
        int size = subjectsList.size();

        Long deleted = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects/%d", RULE.getLocalPort(), academic, id))
                .request()
                .delete(new GenericType<Long>() {});

        assertEquals(id, deleted);

        List<Subject> subjectsList2 = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Subject>>() {});

        id = subjectsList2.get(0).getId();

        deleted = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects/%d", RULE.getLocalPort(), academic, id))
                .request()
                .delete(new GenericType<Long>() {});
        assertEquals(id, deleted);

        subjectsList2 = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Subject>>() {});

        int newSize = subjectsList2.size();

        assertEquals(newSize, size-2);
    }

    @Test
    public void testSave() {

        Subject subject = new Subject("Programação 1", new Long(22), 35, 300, "graduate", "Wylle", new Long(14));
        subject.setStudants(new ArrayList<Long>());
        subject.setPrerequisite(new ArrayList<Long>());

        Subject postedSubject = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), academic))
                .request()
                .post(Entity.json(subject), Subject.class);

        assertNotNull(postedSubject);
        assertNotNull(postedSubject.getId());

        List<Subject> AllSubjects = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Subject>>() {});

        assertTrue(AllSubjects.size() > 0);

        Long id = AllSubjects.get(0).getId();

        Subject subjectGet = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects/%d", RULE.getLocalPort(), academic, id))
                .request()
                .get(new GenericType<Subject>() {});

        assertNotNull(subjectGet, "Objeto retornou null");
        assertEquals(id, subjectGet.getId(), "Os Ids não batem");
    }

    @Test
    public void testPost(){
        Subject subject = new Subject("Compiladores", new Long(22), 35, 300, "Postgraduate", "Alcino", new Long(44));
        subject.setStudants(new ArrayList<Long>());
        subject.setPrerequisite(new ArrayList<Long>());
        Subject postdSubject = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), academic))
                .request()
                .post(Entity.json(subject), Subject.class);

        assertAll(
                () -> {
                    assertNotNull(postdSubject);
                    assertNotNull(postdSubject.getId());
                },
                () -> {
                    assertEquals("Compiladores", postdSubject.getName());
                    assertEquals(new Long(22), postdSubject.getCourses());
                    assertEquals(35, postdSubject.getCredites());
                    assertEquals(300, postdSubject.getRequisitedCredits());
                    assertEquals("Postgraduate", postdSubject.getType());
                    assertEquals("Alcino", postdSubject.getProfessor());
                    assertEquals(new Long(44), postdSubject.getDepartament());
                },
                () -> {
                    assertEquals(0, postdSubject.getStudants().size());
                    assertEquals(0, postdSubject.getPrerequisite().size());
                }
        );
    }

    @Test
    public void testPut(){
        Subject subject = new Subject("Sistemas Operacionais", new Long(55), 30, 8001, "Postgraduate", "Alla", new Long(52));
        subject.setStudants(new ArrayList<Long>());
        subject.setPrerequisite(new ArrayList<Long>());

        Subject postedSubject = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects", RULE.getLocalPort(), academic))
                .request()
                .post(Entity.json(subject), Subject.class);

        assertNotNull(postedSubject);
        assertNotNull(postedSubject.getId());
        Long id = postedSubject.getId();

        ArrayList<Long> subjectStudents = new ArrayList<Long>();
        subjectStudents.add(new Long(19));
        subjectStudents.add(new Long(21));
        subjectStudents.add(new Long(52));
        subjectStudents.add(new Long(36));
        subjectStudents.add(new Long(37));
        subject.setStudants(subjectStudents);

        Subject newSubject = RULE.client().target(
                String.format("http://localhost:%d/%s/subjects/%d", RULE.getLocalPort(), academic, id))
                .request()
                .put(Entity.json(subject), Subject.class);

        assertAll(
                () -> {
                    assertNotNull(newSubject);
                    assertNotNull(newSubject.getStudants());
                },
                () -> {
                    assertEquals(5, newSubject.getStudants().size());
                    assertEquals(new Long(19), newSubject.getStudants().get(0));
                    assertEquals(new Long(21), newSubject.getStudants().get(1));
                    assertEquals(new Long(52), newSubject.getStudants().get(2));
                    assertEquals(new Long(36), newSubject.getStudants().get(3));
                    assertEquals(new Long(37), newSubject.getStudants().get(4));
                }
        );
    }



}
