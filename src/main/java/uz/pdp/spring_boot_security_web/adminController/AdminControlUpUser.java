package uz.pdp.spring_boot_security_web.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.entity.role.RolePermissionEntity;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRolePermissionDto;
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
        return "admin/userPageForAdmin";
    }
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable int id,Model model) {
        userService.delete(id);
        return "redirect:/admin/allUsers";
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/editUser/{id}")
    public String editUserRolePermission(@PathVariable int id, @ModelAttribute UserRolePermissionDto userRolePermissionDto) {
        userService.editUserRolePermission(id,userRolePermissionDto);
        return "redirect:/admin/allUsers";
    }
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/deleteRPUser/{id}")
    public String deleteUserRolePermission(@PathVariable int id, @ModelAttribute UserRolePermissionDto userRolePermissionDto) {
        userService.deleteUserRolePermission(id,userRolePermissionDto);
        return "redirect:/admin/allUsers";
    }
}
