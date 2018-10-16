package br.ufal.ic.academico.CRUDTest;

import br.ufal.ic.academico.AcademicoApp;
import br.ufal.ic.academico.ConfigApp;
import br.ufal.ic.academico.Studants.ResourceStudent;
import br.ufal.ic.academico.Studants.Student;
import br.ufal.ic.academico.Studants.StudentDAO;
import br.ufal.ic.academico.Subjects.Subject;
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
import static org.mockito.Mockito.mock;

@ExtendWith(DropwizardExtensionsSupport.class)
public class StudantsTest {
    static {
        BootstrapLogging.bootstrap(Level.DEBUG);
    }

    private StudentDAO studentDAO = mock(StudentDAO.class);

    private final ResourceStudent resourceStudent = new ResourceStudent(studentDAO);

    public static DropwizardAppExtension<ConfigApp> RULE = new DropwizardAppExtension(AcademicoApp.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));



    private String academic = "academicotest";

    @Test
    public void deleteTest(){
        List<Student> studentList = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Student>>() {});

        assertTrue(studentList.size() > 0);

        Long id = studentList.get(0).getId();
        int size = studentList.size();

        Long deleted = RULE.client().target(
                String.format("http://localhost:%d/%s/students/%d", RULE.getLocalPort(), academic, id))
                .request()
                .delete(new GenericType<Long>() {});

        assertEquals(id, deleted);

        List<Student> studentList2 = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Student>>() {});

        id = studentList2.get(0).getId();

        deleted = RULE.client().target(
                String.format("http://localhost:%d/%s/students/%d", RULE.getLocalPort(), academic, id))
                .request()
                .delete(new GenericType<Long>() {});
        assertEquals(id, deleted);

        List<Student> getStudent = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Student>>() {});

        int newSize = getStudent.size();

        assertEquals(newSize, size-2);
    }
    @Test
    public void testSave() {

        Student student = new Student("Jojo", 12, 19);
        student.setCoursing(new ArrayList<Long>());
        student.setAproved(new ArrayList<Long>());

        Student postedStudent = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), academic))
                .request()
                .post(Entity.json(student), Student.class);

        assertNotNull(postedStudent);
        assertNotNull(postedStudent.getId());

        List<Student> allStudents = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Student>>() {});

        assertTrue(allStudents.size() > 0);

        Long id = allStudents.get(0).getId();

        Student studentGet = RULE.client().target(
                String.format("http://localhost:%d/%s/students/%d", RULE.getLocalPort(), academic, id))
                .request()
                .get(new GenericType<Student>() {});

        assertNotNull(studentGet);
        assertEquals(id, studentGet.getId());
    }


    @Test
    public void testPost(){
        Student student = new Student("Noir", 13, 91);
        student.setCoursing(new ArrayList<Long>());
        student.setAproved(new ArrayList<Long>());
        Student postdStudent = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), academic))
                .request()
                .post(Entity.json(student), Student.class);

        assertAll(
                () -> {
                    assertNotNull(postdStudent);
                    assertNotNull(postdStudent.getId());
                },
                () -> {
                    assertEquals("Noir", postdStudent.getName());
                    assertEquals(13, postdStudent.getDepartament());
                    assertEquals(91, postdStudent.getCourse());
                },
                () -> {
                    assertEquals(0, postdStudent.getCoursing().size());
                    assertEquals(0, postdStudent.getAproved().size());
                }
        );
    }
    @Test
    public void testList() {

        List<Student> allStudents = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Student>>() {});

        assertTrue(allStudents.size() > 0);
    }

    @Test
    public void testPut(){
        Student student = new Student("GUADALARRARA", 12, 19);
        student.setCoursing(new ArrayList<Long>());
        student.setAproved(new ArrayList<Long>());
        Student post = RULE.client().target(
                String.format("http://localhost:%d/%s/students", RULE.getLocalPort(), academic))
                .request()
                .post(Entity.json(student), Student.class);

        assertNotNull(post.getId());
        Long id = post.getId();
        student.setCredites(37);

        ArrayList<Long> coursing = new ArrayList<Long>();
        coursing.add(new Long(16));
        coursing.add(new Long(42));
        coursing.add(new Long(37));
        coursing.add(new Long(96));
        student.setCoursing(coursing);

        ArrayList<Long> approved = new ArrayList<Long>();
        approved.add(new Long(13));
        approved.add(new Long(17));
        approved.add(new Long(71));
        student.setAproved(approved);

        Student newStudent = RULE.client().target(
                String.format("http://localhost:%d/%s/students/%d", RULE.getLocalPort(), academic, id))
                .request()
                .put(Entity.json(student), Student.class);

        assertAll(
                () -> {
                    assertNotNull(newStudent.getCredites());
                    assertEquals(37, newStudent.getCredites());
                },
                () -> {
                    assertNotNull(newStudent.getCredites());
                    assertEquals(4, newStudent.getCoursing().size());
                    assertEquals(new Long(16), newStudent.getCoursing().get(0));
                    assertEquals(new Long(42), newStudent.getCoursing().get(1));
                    assertEquals(new Long(37), newStudent.getCoursing().get(2));
                    assertEquals(new Long(96), newStudent.getCoursing().get(3));
                },
                () -> {
                    assertNotNull(newStudent.getAproved());
                    assertEquals(3, newStudent.getAproved().size());
                    assertEquals(new Long(13), newStudent.getAproved().get(0));
                    assertEquals(new Long(17), newStudent.getAproved().get(1));
                    assertEquals(new Long(71), newStudent.getAproved().get(2));
                    }

        );
    }
}
