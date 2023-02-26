package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.entity.SubjectEntity;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.entity.role.RolePermissionEntity;
import uz.pdp.spring_boot_security_web.repository.UserRepository;
import uz.pdp.spring_boot_security_web.service.QuestionService;
import uz.pdp.spring_boot_security_web.service.SubjectService;
import uz.pdp.spring_boot_security_web.service.TopicService;
import uz.pdp.spring_boot_security_web.service.UserService;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {

    private final SubjectService subjectService;
    private final TopicService topicService;
    private final UserService userService;

    @GetMapping("/")
    public String home(
            Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = null;
        if (!(authentication.getPrincipal() + "").equals("anonymousUser")) {
            user = (UserEntity) authentication.getPrincipal();
        }
        model.addAttribute("users", user);
        if (user != null) {
            RolePermissionEntity rolePermissionEntities = user.getRolePermissionEntities();
            List<String> roleEnum = rolePermissionEntities.getRoleEnum();
            if (roleEnum.contains("ADMIN")) {
                List<SubjectEntity> subjectEntityList = subjectService.getList();
                model.addAttribute("subjects", subjectEntityList);
                return "admin/subjectPageForAdmin";
            } else if (roleEnum.contains("SUPER_ADMIN")) {
                model.addAttribute("users", userService.getList());
                return "admin/userPageForAdmin";
            } else if (roleEnum.contains("USER")) {
                List<SubjectEntity> subjectList = subjectService.getList();
                if (subjectList.isEmpty()) {
                    return "index";
                }
                List<TopicEntity> topicEntityList = topicService.getBySubjectTitleList(subjectList.get(0).getTitle());
                model.addAttribute("subjects", subjectList);
                model.addAttribute("topics", topicEntityList);
                return "index";
            }
        }
        List<SubjectEntity> subjectList = subjectService.getList();
        if (subjectList.isEmpty()) {
            return "index";
        }
        List<TopicEntity> topicEntityList = topicService.getBySubjectTitleList(subjectList.get(0).getTitle());
        model.addAttribute("subjects", subjectList);
        model.addAttribute("topics", topicEntityList);
        return "index";
    }


    @GetMapping("login")
    public String customLoginPage() {
        return "login";
    }
}
