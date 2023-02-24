package uz.pdp.spring_boot_security_web.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminControlUpUser {
    private final UserService userService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/allUsers")
    public String getUserList(Model model) {
        List<UserEntity> userEntities = userService.getList();
        model.addAttribute("users", userEntities);
        return "userPageForAdmin";
    }
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/deleteUser/{id}")
    public String getSubjectsList(@PathVariable int id,Model model) {
        userService.delete(id);

        List<UserEntity> userEntities = userService.getList();
        model.addAttribute("users", userEntities);
        return "userPageForAdmin";

    }
}
