package uz.pdp.spring_boot_security_web.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.entity.TopicEntity;
import uz.pdp.spring_boot_security_web.model.dto.TopicRequestDto;
import uz.pdp.spring_boot_security_web.service.TopicService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adminTopic")
public class AdminControllerUpTopic {

    private final TopicService topicService;

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('ADD') or hasRole('SUPER_ADMIN')")
    @PostMapping("/addTopic")
    public String addSubject(@ModelAttribute TopicRequestDto topicRequestDTO) {
        topicService.add(topicRequestDTO);
        return "redirect:/adminTopic/topics";
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('READ') or hasRole('SUPER_ADMIN')")
    @GetMapping("/topics")
    public String getTopicsList(Model model) {
        List<TopicEntity> topicEntities = topicService.getList();
        model.addAttribute("topics", topicEntities);
        return "admin/topicPageForAdmin";
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('DELETE') or hasRole('SUPER_ADMIN')")
    @GetMapping("/deleteTopic/{id}")
    public String getDeleteTopicById(@PathVariable int id) {
        topicService.delete(id);
        return "redirect:/adminTopic/topics";
    }
}
