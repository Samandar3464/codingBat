package uz.pdp.spring_boot_security_web.adminController;

import org.junit.AfterClass;
import org.junit.jupiter.api.AfterAll;
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

class AdminControllerUpTopicTest extends BaseTest {

    @BeforeAll
    static void beforeAll() {
        Startables.deepStart(postgres).join();
    }

    @AfterEach
    void afterEach() {
        topicRepository.deleteAll();
//        subjectRepository.deleteAll();
    }

//    @AfterAll
//    static void afterAll() {
//        subjectRepository.deleteAll();
//    }

//    @AfterClass
//    public void aa(){
//    topicRepository.truncateMyTable();
//    }


    @Test
    void canAddTopic() throws Exception {
        callAddSubject();
        callAdd().andExpect(status().isOk());
    }

    @Test
    void canThrowException() throws Exception {
        callAddSubject();
        callAdd();
        callAdd().andExpect(status().isAlreadyReported());
    }

    @Test
    void canDelete() throws Exception {
        callAddSubject();
        callAdd();
        deleteById(3).andExpect(status().isOk());
    }

    @Test
    void deleteTopicCanThrowException() throws Exception {
        deleteById(10).andExpect(status().isNotFound());
    }

    public ResultActions callAdd() throws Exception {
//        SubjectEntity java = subjectRepository.save(new SubjectEntity("Java"));
        int id = 0;
        final MockHttpServletRequestBuilder requestBuilder
                = post("/adminTopic/addTopic")
                .param("name", "Array")
                .param("subjectId", String.valueOf(++id));
        return mockMvc.perform(requestBuilder);
    }

    private ResultActions callAddSubject() throws Exception {
        final MockHttpServletRequestBuilder request =
                post("/adminSubject/addSubject").param("title", "Kotlin");
        return mockMvc.perform(request);
    }

    public ResultActions deleteById(int id) throws Exception {
        final MockHttpServletRequestBuilder requestBuilder =
                get("/adminTopic/deleteTopic/{id}", id);
        return mockMvc.perform(requestBuilder);
    }
}