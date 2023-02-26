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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


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
    @WithMockUser(roles = "SUPER_ADMIN")
    void findById() throws Exception {
        callAdd();
        findById(1).andExpect(view().name("redirect:/subject/list"));
    }


    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void findByIdThrow() throws Exception {
        findById(1).andExpect(view().name("404"));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void addSubject() throws Exception {
        callAdd().andExpect(view().name("redirect:/adminSubject/subjects"));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void addSubjectThrow() throws Exception {
        callAdd();
        callAdd().andExpect(view().name("404"));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void deleteById() throws Exception {
        callAdd();
        deleteById(5).andExpect(view().name("redirect:/adminSubject/subjects"));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void deleteByIdThrow() throws Exception {
        deleteById(100).andExpect(view().name("404"));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void editSubject() throws Exception {
        callAdd();
        editSubject(2).andExpect(view().name("redirect:/adminSubject/subjects"));
    }
//    Controllerga ozi request jonatadi


    private ResultActions editSubject(int id) throws Exception {
        final MockHttpServletRequestBuilder request=
                post("/adminSubject/editSubject/{id}",id)
                        .param("title", "Java");
        return mockMvc.perform(request);
    }
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