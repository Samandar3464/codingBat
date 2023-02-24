//package uz.pdp.spring_boot_security_web.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import uz.pdp.spring_boot_security_web.entity.UserEntity;
//import uz.pdp.spring_boot_security_web.service.SubjectService;
//import uz.pdp.spring_boot_security_web.service.TopicService;
//
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/")
//public class TopicController {
//    private final TopicService topicService;
//    private final SubjectService subjectService;
//
//    @GetMapping("/{tittle}")
//    public String getTopicList(@PathVariable String tittle, Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserEntity user = (UserEntity) authentication.getPrincipal();
//        model.addAttribute("users", user);
//        model.addAttribute("topics", topicService.getBySubjectTitleList(tittle));
//        model.addAttribute("subjects", subjectService.getList());
//        return "topic";
//    }
//
//}
