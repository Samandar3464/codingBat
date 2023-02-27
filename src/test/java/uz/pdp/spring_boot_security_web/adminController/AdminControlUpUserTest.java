package uz.pdp.spring_boot_security_web.adminController;

import org.junit.jupiter.api.*;
import org.springframework.core.annotation.Order;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.lifecycle.Startables;
import uz.pdp.spring_boot_security_web.controller.BaseTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class AdminControlUpUserTest extends BaseTest {
    @BeforeAll
    static void beforeAll() {
        Startables.deepStart(postgres).join();
    }

    @AfterEach
     void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    @WithMockUser(roles = "SUPER_ADMIN")
    void deleteUser() throws Exception {
        addUser();
        deleteUser(3).andExpect(view().name("redirect:/admin/allUsers"));
    }
    @Test
    @Order(2)
    @WithMockUser(roles = "SUPER_ADMIN")
    void deleteUserCanThrow() throws Exception {
        deleteUser(5).andExpect(view().name("404"));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    @Order(3)
    void editUserRolePermission() throws Exception {
        addUser();
        addRolePermission(1).andExpect(view().name("redirect:/admin/allUsers"));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    @Order(3)
    void deleteUserRolePermission() throws Exception {
        addUser();
        deleteRolePermission(2).andExpect(view().name("redirect:/admin/allUsers"));
    }


    private ResultActions addRolePermission(int id) throws Exception {
        final  MockHttpServletRequestBuilder request=
                post("/admin/editUser/{id}",id)
                        .param("role","ADMIN")
                        .param("permission","GET");
        return mockMvc.perform(request);
    }
    private ResultActions deleteRolePermission(int id) throws Exception {
        final  MockHttpServletRequestBuilder request=
                post("/admin/deleteRPUser/{id}",id)
                        .param("role","ADMIN")
                        .param("permission","GET");
        return mockMvc.perform(request);
    }
    private ResultActions addUser() throws Exception {
        final MockHttpServletRequestBuilder request =
                post("/api/user/add")
                        .param("email", "codinglife2022@gmail.com")
                        .param("password", "1");
        return mockMvc.perform(request);
    }
    private ResultActions deleteUser(int id) throws Exception {
        final MockHttpServletRequestBuilder request =
                get("/admin/deleteUser/{id}" ,id);
        return mockMvc.perform(request);
    }
}