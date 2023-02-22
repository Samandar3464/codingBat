package uz.pdp.spring_boot_security_web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.lifecycle.Startables;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void findByTitle() throws Exception {
        callAdd();
        findByName("Kotlin").andExpect(status().isOk());
    }
    @Test
    void findByTitleThrow() throws Exception {
        findByName("Java").andExpect(status().isBadRequest());
    }
    private ResultActions findByName(String a) throws Exception {
        final MockHttpServletRequestBuilder request =
                get("/subject/{title}", a);
        return mockMvc.perform(request);
    }
    private ResultActions callAdd() throws Exception {
        final MockHttpServletRequestBuilder request =
                post("/adminSubject/addSubject")
                        .param("title", "Kotlin");
        return mockMvc.perform(request);
    }
}