package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;


    public AdminController(UserService userService) {
        this.userService = userService;

    }


    @GetMapping
    public String getAllUsers(Model model) {

        model.addAttribute("users", userService.getAllUsers());

        return "users";

    }

    @PostMapping("/add")
    public String addUsers(@ModelAttribute("user") User user,
                           @RequestParam(value = "roleIds", required = false) List<Long> roleIds){
        userService.saveUser(user, roleIds);

        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUsers(@RequestParam("id") Long id){

        userService.deleteUser(id);

        return "redirect:/admin";

    }

    @PostMapping("/update")
    public String updateUsers(@ModelAttribute("user") User user,
                              @RequestParam(value = "roleIds", required = false)
                              List<Long> roleIds){

        userService.updateUser(user, roleIds);

        return "redirect:/admin";
    }
}
