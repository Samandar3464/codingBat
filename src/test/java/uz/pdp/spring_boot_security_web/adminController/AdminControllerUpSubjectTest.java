package uz.pdp.spring_boot_security_web.adminController;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.shaded.org.hamcrest.CoreMatchers;
import uz.pdp.spring_boot_security_web.controller.BaseTest;
import uz.pdp.spring_boot_security_web.entity.SubjectEntity;
import uz.pdp.spring_boot_security_web.model.dto.SubjectRequestDTO;

import javax.security.auth.Subject;

import java.util.List;

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


//    @Test
//    void findById() throws Exception {
//        callAdd();
//        findById(1).andExpect(status().isOk());
//    }
//
//
//    @Test
//    void findByIdThrow() throws Exception {
//        findById(4).andExpect(status().isNotFound());
//    }

    @Test
    void addSubject() throws Exception {
        callAdd().andExpect(status().isOk());
    }

//    @Test
//    void addSubjectThrow() throws Exception {
//        callAdd();
//        callAdd().andExpect(status().isAlreadyReported());
//    }

//    @Test
//    void deleteById() throws Exception {
//        callAdd();
//        deleteById(1).andExpect(status().isOk());
//    }
//
//    @Test
//    void deleteByIdThrow() throws Exception {
//        deleteById(3).andExpect(status().isNotFound());
//    }
////    Controllerga ozi request jonatadi
//
//    @Test
//    void editById() throws Exception {
//        callAdd();
//        editById(1, "Go").andExpect(status().isOk());
//    }
//
//    @Test
//    void editByIdThrow() throws Exception {
//        callAdd();
//        editById(3, "Go").andExpect(status().isNotFound());
//    }
//

//    private ResultActions deleteById(int id) throws Exception {
//        final MockHttpServletRequestBuilder request =
//                get("/adminSubject/deleteSubject/{id}", id);
//        return mockMvc.perform(request);
//    }
//
//    private ResultActions findById(int id) throws Exception {
//        final MockHttpServletRequestBuilder request =
//                get("/adminSubject/get/{id}", id);
//        return mockMvc.perform(request);
//    }

    private ResultActions callAdd() throws Exception {

        final MockHttpServletRequestBuilder request =
                post("/adminSubject/addSubject")
                        .param("title", "Kotlin");
        return mockMvc.perform(request);
    }

    @Test
    void givenSubjectsList() throws Exception{
        List<SubjectEntity> subjects =
                List.of(SubjectEntity.builder().title("Kotlin").build());
        subjectRepository.saveAll(subjects);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/adminSubject/subjects"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect((ResultMatcher) MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(subjects.size())));
    }


//    private ResultActions editById(int id, String title) throws  Exception{
//        final MockHttpServletRequestBuilder request =
//                post("/adminSubject/editSubject")
//                        .param("id", String.valueOf(id))
//                        .param("title", title);
//
//        return mockMvc.perform(request);
//    }

}