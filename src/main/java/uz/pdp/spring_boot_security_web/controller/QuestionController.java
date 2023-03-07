package uz.pdp.spring_boot_security_web.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.compiler.CompliedClass;
import uz.pdp.spring_boot_security_web.entity.QuestionEntity;
import uz.pdp.spring_boot_security_web.entity.SubjectEntity;
import uz.pdp.spring_boot_security_web.entity.TestCaseEntity;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.repository.TestCaseRepository;
import uz.pdp.spring_boot_security_web.service.QuestionService;
import uz.pdp.spring_boot_security_web.service.SubjectService;
import uz.pdp.spring_boot_security_web.service.TestCaseService;

import java.util.List;

@Controller
@RequiredArgsConstructor
    @RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;
    private final SubjectService subjectService;
    private final CompliedClass compliedClass;
    private final TestCaseRepository testCaseRepository;
    private final TestCaseService testCaseService;

    @GetMapping("/{name}")
    public String getTopicQuestions(@PathVariable String name, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = null;
        if (!(authentication.getPrincipal() + "").equals("anonymousUser")) {
            user = (UserEntity) authentication.getPrincipal();
            List<QuestionEntity> questionEntities = questionService.printSolvedAndUnsolvedQuestions(name, user);
            model.addAttribute("questions",questionEntities);
            model.addAttribute("users", user);
        }
        List<QuestionEntity> questionEntities = questionService.getList(name);
        model.addAttribute("subjects",subjectService.getList());
        model.addAttribute("questions",questionEntities);
        model.addAttribute("topic",name);
        return "allQuestions";
    }

    @GetMapping("/problem/{id}")
    public String getTopicQuestion(@PathVariable int id, Model model){
        QuestionEntity question = questionService.getById(id);
        List<SubjectEntity> list = subjectService.getList();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = null;
        if (!(authentication.getPrincipal() + "").equals("anonymousUser")) {
            user = (UserEntity) authentication.getPrincipal();
            model.addAttribute("users", user);
        }
        String[] split = question.getMethodAndParams().split(",");
        model.addAttribute("compile",
                "public class Solution {\n"
                            +"    public "+split[0]+" "+split[1]+"("+split[2]+" firstParam, "+split[3]+" secondParam) {\n"
                            +"        return ;\n"
                            +"    }\n"
                            +"}");
        model.addAttribute("question",question);
        model.addAttribute("subjects",list);
        String str = questionService.getTopicNameByQuestion(question, list);
        if(str!=null){
            model.addAttribute("topic",str);
        }
        return "question";
    }

    @PostMapping("/response/{id}")
    public String checkAnswer(@PathVariable int id, HttpServletRequest request, Model model){
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QuestionEntity question = questionService.getById(id);
        String code = request.getParameter("code");
        String[] split = question.getMethodAndParams().split(",");
        List<String> passedTestCases=null;
        List<TestCaseEntity> testCases=null;
        if(code!=null){
            testCases = testCaseRepository.findAllByQuestionId(question.getId());
            passedTestCases = compliedClass.passAllTestCases(testCases, split, code);
            int size = testCaseService.quantityOfSuccessfulTestCases(passedTestCases).size();
            String tick = "❌";
            if(size==testCases.size()){
                questionService.makeQuestionSolved(user,question);
                tick = "✅";
            }
            model.addAttribute("passMessage",tick+" "+size+" test cases out of "+testCases.size()+" passed successfully");
        }
        List<SubjectEntity> list = subjectService.getList();
        model.addAttribute("passedTestCases",passedTestCases);
        model.addAttribute("compile",code);
        model.addAttribute("question",question);
        model.addAttribute("subjects",list);
        String str = questionService.getTopicNameByQuestion(question, list);
        if(str!=null){
            model.addAttribute("topic",str);
        }
        return "question";
    }

}
