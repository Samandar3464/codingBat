package uz.pdp.spring_boot_security_web.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.entity.SubjectEntity;
import uz.pdp.spring_boot_security_web.model.dto.SubjectRequestDTO;
import uz.pdp.spring_boot_security_web.repository.SubjectRepository;
import uz.pdp.spring_boot_security_web.service.SubjectService;

import javax.security.auth.Subject;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adminSubject")
public class AdminControllerUpSubject {
    private final SubjectService subjectService;
    private final SubjectRepository subjectRepository;

    @PostMapping("/addSubject")
    public String addSubject(@ModelAttribute SubjectRequestDTO title) {
        String add = String.valueOf(subjectService.add(title));
        if (add != null) {
            return "redirect:/adminSubject/subjects";
        }
        return "redirect:/404";
    }


    @GetMapping("/subjects")
    public String getSubjectsList(Model model) {
        List<SubjectEntity> subjectEntities = subjectService.getList();
        model.addAttribute("subjects", subjectEntities);
        return "admin/subjectPageForAdmin";
    }

    @GetMapping("/deleteSubject/{id}")
    public String getSubjectsList(@PathVariable int id) {
        boolean delete = subjectService.delete(id);
        if (delete){
            return "redirect:/adminSubject/subjects";
        }
        return "redirect:/404";
    }

    @GetMapping("/get/{id}")
    public String getById(@PathVariable("id") int id, Model model) {
        SubjectEntity byId = subjectService.getById(id);
        if (byId !=null){
            model.addAttribute("subject", subjectService.getList());
            return "redirect:/subject/list";
        }
        return "redirect:/404";
    }

    @PostMapping("/editSubject/{id}")
    public String editSubject(
            @PathVariable int id,
            @ModelAttribute SubjectRequestDTO newTitle
    ){
        subjectService.editSubject(id,newTitle);
        return "redirect:/adminSubject/subjects";
    }
}
