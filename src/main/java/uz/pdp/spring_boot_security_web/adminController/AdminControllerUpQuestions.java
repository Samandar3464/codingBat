package uz.pdp.spring_boot_security_web.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.entity.QuestionEntity;
import uz.pdp.spring_boot_security_web.model.dto.QuestionRequestDto;
import uz.pdp.spring_boot_security_web.service.QuestionService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adminQuestion")
public class AdminControllerUpQuestions {
    private final QuestionService questionService;

    @ResponseBody
    @PostMapping("/addQuestion")
    public String addQuestion(@ModelAttribute QuestionRequestDto questionRequestDTO) {
        questionService.add(questionRequestDTO);
        return "redirect:/adminQuestion/questions";
    }

    @GetMapping("/questions")
    public String getQuestionList(Model model) {
        List<QuestionEntity> questionEntities = questionService.getList();
        model.addAttribute("questions", questionEntities);
        return "admin/questionPageForAdmin";
    }

    @ResponseBody
    @GetMapping("/deleteQuestion/{id}")
    public String deleteQuestionById(@PathVariable int id) {
        questionService.delete(id);
        return "redirect:/adminQuestion/questions";
    }
}
