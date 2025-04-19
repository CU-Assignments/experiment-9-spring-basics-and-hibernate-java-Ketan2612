Hibernate.cfg.xml:

<hibernate-configuration>
 <session-factory>
   <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
   <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/testdb</property>
   <property name="hibernate.connection.username">root</property>
   <property name="hibernate.connection.password">password</property>
   <property name="hibernate.dialect">org.hibernate.dialect.MySQL8Dialect</property>
   <property name="hibernate.hbm2ddl.auto">update</property>
   <mapping class="model.Student"/>
 </session-factory>
</hibernate-configuration>

Student.java:

package model;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int age;

    public Student() {}
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getters/Setters
}

HibernateUtil.java:

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

StudentDAO.java:

import model.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class StudentDAO {
    public void create(Student s) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(s);
        tx.commit();
        session.close();
    }

    // Add read, update, delete similarly
}

MainApp.java:

public class MainApp {
    public static void main(String[] args) {
        StudentDAO dao = new StudentDAO();
        Student s = new Student("Bob", 22);
        dao.create(s);
    }
}



