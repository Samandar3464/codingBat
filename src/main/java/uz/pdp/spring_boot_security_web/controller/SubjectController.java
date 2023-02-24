package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.entity.SubjectEntity;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.model.dto.SubjectRequestDTO;
import uz.pdp.spring_boot_security_web.service.SubjectService;
import uz.pdp.spring_boot_security_web.service.TopicService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;
    private final TopicService topicService;

    @GetMapping("/list")
    public String getSubjectList(Model model) {
        List<SubjectEntity> list = subjectService.getList();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();
        model.addAttribute("users", user);
        model.addAttribute("subjects", subjectService.getList());
        model.addAttribute("topics", topicService.getBySubjectTitleList(list.get(0).getTitle()));
        return "index";
    }


    @GetMapping("/{title}")
    public String getByTitle(@PathVariable("title") String title,Model model) {
        SubjectEntity byTitle = subjectService.getByTitle(title);
        if (byTitle!=null){
            model.addAttribute("subject", subjectService.getList());
            return "redirect:/subject/list";
        }
        return "redirect:/404";
    }
}
