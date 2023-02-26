package uz.pdp.spring_boot_security_web.controller;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.lifecycle.Startables;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class AuthControllerTest extends BaseTest {
    private final static String PATH_ADD = "/api/user/add";

    @BeforeAll
    static void beforeAll() {
        Startables.deepStart(postgres).join();
    }

    @AfterEach
    void afterAll() {
         userRepository.deleteAll();
    }

    @Test
    public void addUserShouldReturnOKStatus() throws Exception {
        callAdd().andExpect(view().name("verify"));

    }
    @Test
    public void addUserShouldThrowUserExist() throws Exception {
        callAdd();
//        callAdd().andExpect(status().isBadRequest());
        callAdd().andExpect(view().name("404"));
    }

    private ResultActions callAdd() throws Exception {

        final MockHttpServletRequestBuilder request =
                post(PATH_ADD)
                        .param("email", "123@gmail.com")
                        .param("password", "123");
        return mockMvc.perform(request);
    }

}