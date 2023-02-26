package uz.pdp.spring_boot_security_web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.lifecycle.Startables;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class SubjectControllerTest extends BaseTest {

    @BeforeAll
    static void beforeAll() {
        Startables.deepStart(postgres).join();
    }


    @AfterEach
    void afterAll() {
        subjectRepository.deleteAll();
    }
   @WithMockUser(roles = "SUPER_ADMIN")
    @Test
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