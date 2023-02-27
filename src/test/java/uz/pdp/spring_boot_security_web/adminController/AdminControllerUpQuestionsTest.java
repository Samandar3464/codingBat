package uz.pdp.spring_boot_security_web.adminController;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


class AdminControllerUpQuestionsTest extends BaseTest {
    @BeforeAll
    static void beforeAll() {
        Startables.deepStart(postgres).join();
    }

    @AfterEach
    void afterEach() {
        subjectRepository.deleteAll();
        topicRepository.deleteAll();
        questionRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void canAddQuestion() throws Exception {
        addSubject();
        addTopic();
        addMockQuestion().andExpect(view().name("redirect:/adminQuestion/questions"));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void canDeleteQuestionById() throws Exception {
        addSubject();
        addTopic();
        addMockQuestion();
        deleteMockQuestion(4).andExpect(view().name("redirect:/adminQuestion/questions"));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void updateQuestionById() throws Exception {
        addSubject();
        addTopic();
        editMockQuestion().andExpect(view().name("redirect:/adminQuestion/questions"));


    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void addQuestionThrowException() throws Exception {
        addSubject();
        addTopic();
        addMockQuestion();
        addMockQuestion().andExpect(view().name("404"));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void deleteQuestionThrowException() throws Exception {
        deleteMockQuestion(3).andExpect(view().name("404"));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void editSubjectCanThrow() throws Exception {
        editMockQuestion().andExpect(view().name("404"));
    }


    public ResultActions addMockQuestion() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder
                = post("/adminQuestion/addQuestion")
                .param("name", "Hello")
                .param("text", "Hello")
                .param("example", "Hello")
                .param("topicName", "Array")
                .param("subjectName", "Kotlin");
        return mockMvc.perform(requestBuilder);
    }

    public ResultActions editMockQuestion() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder
                = post("/adminQuestion/addQuestion")
                .param("id", "1")
                .param("name", "Hello")
                .param("text", "Hello")
                .param("example", "Hello")
                .param("topicName", "Array")
                .param("subjectName", "Kotlin");
        return mockMvc.perform(requestBuilder);
    }

    public ResultActions deleteMockQuestion(int id) throws Exception {
        final MockHttpServletRequestBuilder requestBuilder
                = get("/adminQuestion/deleteQuestion/{id}", id);
        return mockMvc.perform(requestBuilder);
    }


    public ResultActions addTopic() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder
                = post("/adminTopic/addTopic")
                .param("name", "Array")
                .param("subject", "Kotlin");
        return mockMvc.perform(requestBuilder);
    }

    private ResultActions addSubject() throws Exception {
        final MockHttpServletRequestBuilder request =
                post("/adminSubject/addSubject").param("title", "Kotlin");
        return mockMvc.perform(request);
    }
}