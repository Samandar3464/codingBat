package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.service.SubjectService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {

    private final SubjectService subjectService;

    @ResponseBody
    @GetMapping("/")
    public ModelAndView home(
           ModelAndView modelAndView
            ){
       modelAndView.addObject("subjects",subjectService.getList());
       modelAndView.setViewName("index");
       return modelAndView;
    }
}
