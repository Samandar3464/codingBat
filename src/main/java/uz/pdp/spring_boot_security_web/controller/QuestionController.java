package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.entity.QuestionEntity;
import uz.pdp.spring_boot_security_web.service.QuestionService;
import uz.pdp.spring_boot_security_web.service.SubjectService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;
    private final SubjectService subjectService;

    @GetMapping("/{name}")
    public String getTopicQuestions(@PathVariable String name, Model model){
        List<QuestionEntity> questionEntities = questionService.getList(name);
        model.addAttribute("questions",questionEntities);
        model.addAttribute("subjects",subjectService.getList());
        return "question";
    }

    @GetMapping("/problem/{id}")
    public String getTopicQuestions(@PathVariable int id, Model model){
        QuestionEntity byId = questionService.getById(id);
        model.addAttribute("question",byId);
        model.addAttribute("subjects",subjectService.getList());
        return "questionfull";
    }

}
