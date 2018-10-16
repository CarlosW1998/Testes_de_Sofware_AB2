package br.ufal.ic.academico;

import br.ufal.ic.academico.Courses.Course;
import br.ufal.ic.academico.Courses.CourseDAO;
import br.ufal.ic.academico.Courses.ResourceCourse;
import br.ufal.ic.academico.Departaments.Departament;
import br.ufal.ic.academico.Departaments.DepartamentDAO;
import br.ufal.ic.academico.Departaments.ResourceDepartament;
import br.ufal.ic.academico.Professors.Professor;
import br.ufal.ic.academico.Professors.ProfessorDAO;
import br.ufal.ic.academico.Professors.ResourceProfessor;
import br.ufal.ic.academico.Secretarys.ResourceSecretary;
import br.ufal.ic.academico.Secretarys.Secretary;
import br.ufal.ic.academico.Secretarys.SecretaryDAO;
import br.ufal.ic.academico.Studants.ResourceStudent;
import br.ufal.ic.academico.Studants.Student;
import br.ufal.ic.academico.Studants.StudentDAO;
import br.ufal.ic.academico.Subjects.ResourceSubject;
import br.ufal.ic.academico.Subjects.Subject;
import br.ufal.ic.academico.Subjects.SubjectDAO;
import br.ufal.ic.academico.exemplos.MyResource;
import br.ufal.ic.academico.exemplos.Person;
import br.ufal.ic.academico.exemplos.PersonDAO;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Willy
 */
@Slf4j
public class AcademicoApp extends Application<ConfigApp> {

    public static void main(String[] args) throws Exception {
        new AcademicoApp().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<ConfigApp> bootstrap) {
        log.info("initialize");
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(ConfigApp config, Environment environment) {

        final SubjectDAO subjectDAO = new SubjectDAO(hibernate.getSessionFactory());
        final StudentDAO studentDAO = new StudentDAO(hibernate.getSessionFactory());
        final SecretaryDAO secretaryDAO = new SecretaryDAO(hibernate.getSessionFactory());
        final ProfessorDAO professorDAO = new ProfessorDAO(hibernate.getSessionFactory());
        final DepartamentDAO departamentDAO = new DepartamentDAO(hibernate.getSessionFactory());
        final CourseDAO courseDAO = new CourseDAO(hibernate.getSessionFactory());
        final PersonDAO dao = new PersonDAO(hibernate.getSessionFactory());

        final ResourceStudent resourceStudent = new ResourceStudent(studentDAO);
        final ResourceSubject resourceSubject = new ResourceSubject(subjectDAO);
        final ResourceSecretary resourceSecretary = new ResourceSecretary(secretaryDAO);
        final ResourceProfessor resourceProfessor = new ResourceProfessor(professorDAO);
        final ResourceDepartament resourceDepartament = new ResourceDepartament(departamentDAO);
        final ResourceCourse resourceCourse = new ResourceCourse(courseDAO);
        final MyResource resource = new MyResource(dao);

        final ResourceSystemRequiriments  requirements = new ResourceSystemRequiriments(subjectDAO, studentDAO,  secretaryDAO, professorDAO, departamentDAO, courseDAO);
        environment.jersey().register(resourceSubject);
        environment.jersey().register(resourceStudent);
        environment.jersey().register(resourceSecretary);
        environment.jersey().register(resourceProfessor);
        environment.jersey().register(resourceDepartament);
        environment.jersey().register(resourceCourse);

        environment.jersey().register(requirements);
    }

    private final HibernateBundle<ConfigApp> hibernate
            = new HibernateBundle<ConfigApp>(Person.class, Subject.class, Student.class, Secretary.class, Professor.class, Departament.class, Course.class) {
        
        @Override
        public DataSourceFactory getDataSourceFactory(ConfigApp configuration) {
            return configuration.getDatabase();
        }
    };
}
