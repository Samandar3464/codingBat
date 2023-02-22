package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.entity.SubjectEntity;
import uz.pdp.spring_boot_security_web.model.dto.SubjectRequestDTO;
import uz.pdp.spring_boot_security_web.service.SubjectService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("/list")
    public String getSubjectList(Model model) {
        model.addAttribute("subjects", subjectService.getList());
        return "index";
    }

    @ResponseBody
    @GetMapping("/{title}")
    public String getByTitle(@PathVariable("title") String title,Model model) {
        SubjectEntity byTitle = subjectService.getByTitle(title);
        if (byTitle!=null){
            model.addAttribute("subject", subjectService.getList());
            return "redirect:/subject/list";
        }
        return "redirect:/404";
    }



//    @ResponseBody
//    @PostMapping("/add")
//    public String addSubject(@ModelAttribute SubjectRequestDTO subjectRequestDTO, Model model) {
//        SubjectEntity add = subjectService.add(subjectRequestDTO);
//        if (add != null) {
//            model.addAttribute("subjects",subjectService.getList());
//            return "redirect:/subject/list";
//        }
//        return "redirect:/404";
//    }
//    @ResponseBody
//    @GetMapping("/get/{id}")
//    public String getById(@PathVariable("id") int id, Model model) {
//        SubjectEntity byId = subjectService.getById(id);
//        if (byId !=null){
//            model.addAttribute("subject", subjectService.getList());
//            return "redirect:/subject/list";
//        }
//        return "redirect:/404";
//    }

//    @ResponseBody
//    @GetMapping("/delete/{id}")
//    public String getDeleteById(@PathVariable("id") int id, Model model) {
//        boolean delete = subjectService.delete(id);
//        if (delete){
//            model.addAttribute("subject", subjectService.getList());
//            return "redirect:/subject/list";
//        }
//        return "redirect:/404";
//    }
}
