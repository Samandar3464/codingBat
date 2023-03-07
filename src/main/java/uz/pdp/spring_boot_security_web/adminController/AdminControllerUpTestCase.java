package uz.pdp.spring_boot_security_web.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.entity.TestCaseEntity;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.model.dto.TestCaseDto;
import uz.pdp.spring_boot_security_web.service.TestCaseService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/testCase")
public class AdminControllerUpTestCase {
    private final TestCaseService testCaseService;
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('ADD') or hasRole('SUPER_ADMIN')")
    @PostMapping("/add")
    public String addTestCase(@ModelAttribute TestCaseDto testCaseDto) {
        testCaseService.add(testCaseDto);
        return "redirect:/adminQuestion/questions";
    }
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('READ') or hasRole('SUPER_ADMIN')")
    @GetMapping("/testCases")
    public String getTestCaseList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();
        model.addAttribute("users", user);
        List<TestCaseEntity> questionEntities = testCaseService.getList();
        model.addAttribute("questions", questionEntities);
        return "admin/questionPageForAdmin";
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('DELETE') or hasRole('SUPER_ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteQuestionById(@PathVariable int id) {
        testCaseService.delete(id);
        return "redirect:/adminQuestion/questions";
    }
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('READ') or hasRole('SUPER_ADMIN')")
    @GetMapping("/page")
    public String getTopicsList(Model model) {
        List<TestCaseEntity> list = testCaseService.getList();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();
        model.addAttribute("users", user);
        model.addAttribute("tests", list);
        return "admin/testCasePageForAdmin";
    }
}
