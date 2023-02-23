package uz.pdp.spring_boot_security_web.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;
import uz.pdp.spring_boot_security_web.model.dto.TopicEditRequestDto;
import uz.pdp.spring_boot_security_web.model.dto.TopicRequestDto;
import uz.pdp.spring_boot_security_web.service.TopicService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adminTopic")
public class AdminControllerUpTopic {

    private final TopicService topicService;

//    @ResponseBody
//    @PreAuthorize("hasRole('ADMIN') or hasAuthority('TOPIC_ADD')")
    @PostMapping("/addTopic")
    public String addTopic(@ModelAttribute TopicRequestDto topicRequestDTO) {
        topicService.add(topicRequestDTO);
        return "redirect:/adminTopic/topics";
    }

    @GetMapping("/topics")
    public String getTopicsList(Model model) {
        List<TopicEntity> topicEntities = topicService.getList();
        model.addAttribute("topics", topicEntities);
        return "admin/topicPageForAdmin";
    }

//    @ResponseBody
//    @PreAuthorize("hasRole('ADMIN') or hasAuthority('TOPIC_DELETE')")
    @GetMapping("/deleteTopic/{id}")
    public String getDeleteTopicById(@PathVariable int id) {
        topicService.delete(id);
        return "redirect:/adminTopic/topics";
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('UPDATE')")
    @PostMapping("/editTopic/{id}")
    public String editSubject(@ModelAttribute TopicEditRequestDto editRequestDto, @PathVariable int id){
        topicService.edit(id,editRequestDto);
        return "redirect:/adminTopic/topics";
    }
}
