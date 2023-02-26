package uz.pdp.spring_boot_security_web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.lifecycle.Startables;
import uz.pdp.spring_boot_security_web.adminController.AdminControllerUpSubject;
import uz.pdp.spring_boot_security_web.config.SecurityConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
@Import(SecurityConfig.class)
class SubjectControllerTest extends BaseTest {

    @BeforeAll
    static void beforeAll() {
        Startables.deepStart(postgres).join();
    }


    @AfterEach
    void afterAll() {
        subjectRepository.deleteAll();
    }
    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void findByTitle() throws Exception {
        callAdd();
        findByName("Kotlin").andExpect(view().name("index"));
    }
    @Test
    void findByTitleThrow() throws Exception {
        findByName("Java").andExpect(view().name("404"));
    }



    private ResultActions findByName(String a) throws Exception {
        final MockHttpServletRequestBuilder request =
                get("/subject/{title}", a);
        return mockMvc.perform(request);
    }
    @WithMockUser(roles = "SUPER_ADMIN")
    private ResultActions callAdd() throws Exception {
        final MockHttpServletRequestBuilder request =
                post("/adminSubject/addSubject")
                        .param("title", "Kotlin");
        return mockMvc.perform(request);
    }
}