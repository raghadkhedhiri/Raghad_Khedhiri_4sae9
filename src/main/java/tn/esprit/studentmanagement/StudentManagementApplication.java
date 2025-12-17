package tn.esprit.studentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "tn.esprit.studentmanagement")
@EnableJpaRepositories(basePackages = "tn.esprit.studentmanagement.repositories")
@EntityScan(basePackages = "tn.esprit.studentmanagement.entities")
public class StudentManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApplication.class, args);
    }
}
