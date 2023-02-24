package uz.pdp.spring_boot_security_web.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.entity.SubjectEntity;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.entity.role.RolePermissionEntity;
import uz.pdp.spring_boot_security_web.service.SubjectService;
import uz.pdp.spring_boot_security_web.service.TopicService;
import uz.pdp.spring_boot_security_web.service.UserService;

import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {

    private final SubjectService subjectService;
    private final TopicService topicService;


    @GetMapping("/")
    public ModelAndView home(
            ModelAndView modelAndView
            ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user=null;
        if (!(authentication.getPrincipal()+"").equals("anonymousUser")) {
            user = (UserEntity) authentication.getPrincipal();
        }
        modelAndView.addObject("users", user);
        if (user!=null){

            RolePermissionEntity rolePermissionEntities = user.getRolePermissionEntities();
            List<String> roleEnum = rolePermissionEntities.getRoleEnum();
            if(roleEnum.contains("ADMIN")){
                List<SubjectEntity> subjectEntityList = subjectService.getList();
                modelAndView.addObject("subjects", subjectEntityList);
                modelAndView.setViewName("admin/subjectPageForAdmin");
                return modelAndView;
            }
        }
        List<SubjectEntity> subjectList = subjectService.getList();
        List<TopicEntity> topicEntityList = topicService.getBySubjectTitleList(subjectList.get(0).getTitle());
        modelAndView.addObject("subjects", subjectList);
        modelAndView.addObject("topics", topicEntityList);

        modelAndView.setViewName("index");
        return modelAndView;
    }
    @GetMapping("login")
    public  String cus(){
        return "login";
    }
}
