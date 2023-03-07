package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.entity.SubjectEntity;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.model.dto.PrintTopicDto;
import uz.pdp.spring_boot_security_web.model.dto.SubjectRequestDTO;
import uz.pdp.spring_boot_security_web.service.QuestionService;
import uz.pdp.spring_boot_security_web.service.SubjectService;
import uz.pdp.spring_boot_security_web.service.TopicService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;
    private final TopicService topicService;
    private final QuestionService questionService;

    @GetMapping("/list")
    public String getSubjectList(Model model) {
        List<SubjectEntity> list = subjectService.getList();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = null;
        if (!(authentication.getPrincipal() + "").equals("anonymousUser")) {
            user = (UserEntity) authentication.getPrincipal();
            model.addAttribute("users", user);
        }
        model.addAttribute("subjects", subjectService.getList());
        List<PrintTopicDto> printTopicDto = questionService.printTopicWithSolvedQuestionNumbers(topicService.getBySubjectTitleList(list.get(0).getTitle()), user);
        model.addAttribute("topics", printTopicDto);
        return "index";
    }


    @GetMapping("/{title}")
    public String getByTitle(@PathVariable("title") String title,Model model) {
        SubjectEntity byTitle = subjectService.getByTitle(title);
        List<SubjectEntity> list = subjectService.getList();
        if (byTitle!=null){
            model.addAttribute("subjects", list);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserEntity user = null;
            if (!(authentication.getPrincipal() + "").equals("anonymousUser")) {
                user = (UserEntity) authentication.getPrincipal();
                model.addAttribute("users", user);
                List<PrintTopicDto> printTopicDto = questionService.printTopicWithSolvedQuestionNumbers(topicService.getBySubjectTitleList(title), user);
                model.addAttribute("topics", printTopicDto);
            } else {
                List<TopicEntity> topicEntities = topicService.getBySubjectTitleList(title);
                model.addAttribute("topics", topicEntities);
            }
            return "index";
        }
        else {
            return "redirect:/404";
        }
    }
}
