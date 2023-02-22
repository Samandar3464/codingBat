package uz.pdp.spring_boot_security_web.adminController;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.lifecycle.Startables;
import uz.pdp.spring_boot_security_web.controller.BaseTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AdminControllerUpSubjectTest extends BaseTest {

    @BeforeAll
    static void beforeAll() {
        Startables.deepStart(postgres).join();
    }

    @AfterEach
    void afterAll() {
        subjectRepository.deleteAll();
    }


    @Test
    void findById() throws Exception {
        callAdd();
        findById(1).andExpect(status().isOk());
    }


    @Test
    void findByIdThrow() throws Exception {
        findById(1).andExpect(status().isNotFound());
    }

    @Test
    void addSubject() throws Exception {
        callAdd().andExpect(status().isOk());
    }

    @Test
    void addSubjectThrow() throws Exception {
        callAdd();
        callAdd().andExpect(status().isAlreadyReported());
    }

    @Test
    void deleteById() throws Exception {
        ResultActions resultActions = callAdd();
        deleteById(1).andExpect(status().isOk());
    }

    @Test
    void deleteByIdThrow() throws Exception {
        deleteById(100).andExpect(status().isNotFound());
    }
//    Controllerga ozi request jonatadi

    private ResultActions deleteById(int id) throws Exception {
        final MockHttpServletRequestBuilder request =
                get("/adminSubject/deleteSubject/{id}", id);
        return mockMvc.perform(request);
    }

    private ResultActions findById(int id) throws Exception {
        final MockHttpServletRequestBuilder request =
                get("/adminSubject/get/{id}", id);
        return mockMvc.perform(request);
    }

    private ResultActions callAdd() throws Exception {
        final MockHttpServletRequestBuilder request =
                post("/adminSubject/addSubject")
                        .param("title", "Kotlin");
        return mockMvc.perform(request);
    }

}