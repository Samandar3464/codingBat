package uz.pdp.spring_boot_security_web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import uz.pdp.spring_boot_security_web.repository.QuestionRepository;
import uz.pdp.spring_boot_security_web.repository.SubjectRepository;
import uz.pdp.spring_boot_security_web.repository.TopicRepository;
import uz.pdp.spring_boot_security_web.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class BaseTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected SubjectRepository subjectRepository;
    @Autowired
    protected   TopicRepository topicRepository;

    @Autowired
    protected QuestionRepository questionRepository;
    protected static final PostgreSQLContainer<?> postgres;

    static {
        postgres = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
                .withInitScript("sql/table-init.sql")
                .withExposedPorts(5432);
        postgres.withReuse(true);
    }

    @DynamicPropertySource
    static void setUpPostgresConnectionProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.database", postgres::getDatabaseName);
    }
}
