package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.service.UserService;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
//    @ResponseBody
    @PostMapping("/add")
    public String addUser(
            @ModelAttribute UserRegisterDTO userRegisterDTO
    ) {
        UserEntity isSuccess = userService.add(userRegisterDTO);
        if (isSuccess!=null){
            return "Verify account ";
        }else{
            return "redirect:/register";
        }
    }
    @GetMapping("/verify/{code}")
    public String verify(@PathVariable("code") String code){
        if (userService.enableUser(code)) {
            return "redirect:/";
        }
        return "redirect:/register";
    }
}
