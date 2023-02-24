package uz.pdp.spring_boot_security_web.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.entity.QuestionEntity;
import uz.pdp.spring_boot_security_web.entity.SubjectEntity;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.model.dto.QuestionRequestDto;
import uz.pdp.spring_boot_security_web.repository.TopicRepository;
import uz.pdp.spring_boot_security_web.service.QuestionService;
import uz.pdp.spring_boot_security_web.service.SubjectService;
import uz.pdp.spring_boot_security_web.service.TopicService;
import uz.pdp.spring_boot_security_web.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
    @RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;
    private final SubjectService subjectService;
    private final TopicRepository topicService;
    private final UserService userService;

    @GetMapping("/{name}")
    public String getTopicQuestions(@PathVariable String name, Model model){
        List<QuestionEntity> questionEntities = questionService.getList(name);
        model.addAttribute("questions",questionEntities);
        model.addAttribute("subjects",subjectService.getList());
        model.addAttribute("topic",topicService.findByName(name));
        return "allQuestions";
    }

    @GetMapping("/problem/{id}")
    public String getTopicQuestion(@PathVariable int id, Model model){
        QuestionEntity byId = questionService.getById(id);
        List<SubjectEntity> list = subjectService.getList();
        model.addAttribute("question",byId);
        model.addAttribute("subjects",list);
        String str = questionService.getTopicNameByQuestion(byId, list);
        if(str!=null){
            model.addAttribute("topic",str);
        }
        return "question";
    }

    @PostMapping("/response/{id}")
    public String checkAnswer(@PathVariable int id, HttpServletRequest request, Model model){
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QuestionEntity question = questionService.getById(id);
        String text = request.getParameter("text");
        if(text.equals("Solved")){
            question.setTickIcon(question.getTickIcon().replace('1','2'));

        }
        List<SubjectEntity> list = subjectService.getList();
        model.addAttribute("question",question);
        model.addAttribute("subjects",list);
        String str = questionService.getTopicNameByQuestion(question, list);
        if(str!=null){
            model.addAttribute("topic",str);
        }
        return "question";
    }

}
