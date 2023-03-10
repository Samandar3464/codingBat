package uz.pdp.spring_boot_security_web.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.entity.SubjectEntity;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.model.dto.SubjectRequestDTO;
import uz.pdp.spring_boot_security_web.repository.SubjectRepository;
import uz.pdp.spring_boot_security_web.service.SubjectService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adminSubject")
public class AdminControllerUpSubject {
    private final SubjectService subjectService;
    private final SubjectRepository subjectRepository;

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('ADD') or hasRole('SUPER_ADMIN')")
    @PostMapping("/addSubject")
    public String addSubject(@ModelAttribute SubjectRequestDTO subjectRequestDTO) {
        SubjectEntity add = subjectService.add(subjectRequestDTO);
        if (add != null) {
            return "redirect:/adminSubject/subjects";
        }
        return "redirect:/404";
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('READ') or hasRole('SUPER_ADMIN')")
    @GetMapping("/subjects")
    public String getSubjectsList(Model model) {
        List<SubjectEntity> subjectEntities = subjectService.getList();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();
        model.addAttribute("users", user);
        model.addAttribute("subjects", subjectEntities);

        return "admin/subjectPageForAdmin";
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('DELETE') or hasRole('SUPER_ADMIN')")
    @GetMapping("/deleteSubject/{id}")
    public String getSubjectsList(@PathVariable("id") int id) {
        List<SubjectEntity> all = subjectRepository.findAll();
        boolean delete = subjectService.delete(id);
        if (delete){
            return "redirect:/adminSubject/subjects";
        }
        return "redirect:/404";
    }


    @PreAuthorize("hasRole('ADMIN') and hasAuthority('GET') or hasRole('SUPER_ADMIN')")
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
            @ModelAttribute SubjectRequestDTO newName
    ){
        subjectService.editSubject(id,newName);
        return "redirect:/adminSubject/subjects";
    }
}
