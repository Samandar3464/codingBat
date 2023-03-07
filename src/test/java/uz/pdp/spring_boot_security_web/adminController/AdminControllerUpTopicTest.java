package uz.pdp.spring_boot_security_web.adminController;

import org.junit.AfterClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.lifecycle.Startables;
import uz.pdp.spring_boot_security_web.controller.BaseTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void canAddTopic() throws Exception {
        callAddSubject();
        callAdd().andExpect(view().name("redirect:/adminTopic/topics"));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void canThrowExceptionWhenAdd() throws Exception {
        callAddSubject();
        callAdd();
        callAdd().andExpect(view().name("404"));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void canDelete() throws Exception {
        callAddSubject();
        callAdd();
        deleteById(2).andExpect(view().name("redirect:/adminTopic/topics"));
    }
    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void deleteCanThrow() throws Exception {
        callAddSubject();
        callAdd();
        deleteById(9).andExpect(view().name("404"));
    }


    public ResultActions callAdd() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder
                = post("/adminTopic/addTopic")
                .param("name", "Array")
                .param("subject", "Kotlin");
        return mockMvc.perform(requestBuilder);
    }

    private ResultActions callAddSubject() throws Exception {
        final MockHttpServletRequestBuilder request =
                post("/adminSubject/addSubject")
                        .param("title", "Kotlin");
        return mockMvc.perform(request);
    }

    public ResultActions deleteById(int id) throws Exception {
        final MockHttpServletRequestBuilder requestBuilder =
                get("/adminTopic/deleteTopic/{id}", id);
        return mockMvc.perform(requestBuilder);
    }
}