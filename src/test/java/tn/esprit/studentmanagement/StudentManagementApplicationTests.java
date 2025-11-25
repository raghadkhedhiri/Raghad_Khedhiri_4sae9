package tn.esprit.studentmanagement;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Context test disabled for CI (DB not available on Jenkins)")
class StudentManagementApplicationTests {

    @Test
    void contextLoads() {
        // Test de contexte désactivé en CI
    }
}
