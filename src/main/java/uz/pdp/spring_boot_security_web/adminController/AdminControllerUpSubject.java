package uz.pdp.spring_boot_security_web.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.entity.SubjectEntity;
import uz.pdp.spring_boot_security_web.model.dto.SubjectRequestDTO;
import uz.pdp.spring_boot_security_web.service.SubjectService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adminSubject")
public class AdminControllerUpSubject {
    private final SubjectService subjectService;
    @ResponseBody
    @PostMapping("/addSubject")
    public String addSubject(@ModelAttribute SubjectRequestDTO subjectRequestDTO) {
        SubjectEntity add = subjectService.add(subjectRequestDTO);
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
    @ResponseBody
    @GetMapping("/deleteSubject/{id}")
    public String getSubjectsList(@PathVariable("id") int id) {
        boolean delete = subjectService.delete(id);
        if (delete){
            return "redirect:/adminSubject/subjects";
        }
        return "redirect:/404";
    }

    @ResponseBody
    @GetMapping("/get/{id}")
    public String getById(@PathVariable("id") int id, Model model) {
        SubjectEntity byId = subjectService.getById(id);
        if (byId !=null){
            model.addAttribute("subject", subjectService.getList());
            return "redirect:/subject/list";
        }
        return "redirect:/404";
    }
}
