package br.ufal.ic.academico.CRUDTest;

import br.ufal.ic.academico.AcademicoApp;
import br.ufal.ic.academico.ConfigApp;
import br.ufal.ic.academico.Courses.Course;
import br.ufal.ic.academico.Courses.CourseDAO;
import br.ufal.ic.academico.Courses.ResourceCourse;
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
public class Courses {

    static {
        BootstrapLogging.bootstrap(Level.DEBUG);
    }

    private CourseDAO courseDAO = mock(CourseDAO.class);

    private final ResourceCourse resourceCourse = new ResourceCourse(courseDAO);

    public static DropwizardAppExtension<ConfigApp> RULE = new DropwizardAppExtension(AcademicoApp.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));

    private String academic = "academicotest";

    @Test
    public void testDelete(){

        List<Course> allCourses = RULE.client().target(
                String.format("http://localhost:%d/%s/courses", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Course>>() {});

        assertTrue(allCourses.size() > 0);

        Long id = allCourses.get(0).getId();
        int size = allCourses.size();

        Long removed = RULE.client().target(
                String.format("http://localhost:%d/%s/courses/%d", RULE.getLocalPort(), academic, id))
                .request()
                .delete(new GenericType<Long>() {});

        assertEquals(id, removed);

        allCourses = RULE.client().target(
                String.format("http://localhost:%d/%s/courses", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Course>>() {});

        int newSize = allCourses.size();

        assertEquals(newSize, size-1);
    }

    @Test
    public void testList() {

        List<Course> allCourses = RULE.client().target(
                String.format("http://localhost:%d/%s/courses", RULE.getLocalPort(), academic))
                .request()
                .get(new GenericType<List<Course>>() {});

        assertTrue(allCourses.size() > 0);
    }
    @Test
    public void testPost(){
        Course c = new Course("Computacao", "Postgraduate",12, 19);
        c.setSubjects(new ArrayList<Long>());
        c.setStudents(new ArrayList<Long>());

        Course postedCourse = RULE.client().target(
                String.format("http://localhost:%d/%s/courses", RULE.getLocalPort(), academic))
                .request()
                .post(Entity.json(c), Course.class);

        assertAll(
                () -> {
                    assertNotNull(postedCourse);
                    assertNotNull(postedCourse.getId());
                    assertNotNull(postedCourse.getDepartament());
                },
                () -> {
                    assertEquals("Computacao", postedCourse.getName());
                    assertEquals("Postgraduate", postedCourse.getType());
                    assertEquals(12, postedCourse.getDepartament());
                    assertEquals(19, postedCourse.getSecretary());
                },
                () -> {
                    assertNotNull(postedCourse.getSubjects());
                    assertEquals(0, postedCourse.getSubjects().size());
                },
                () -> {
                    assertNotNull(postedCourse.getStudents());
                    assertEquals(0, postedCourse.getStudents().size());
                }
        );
    }


}
