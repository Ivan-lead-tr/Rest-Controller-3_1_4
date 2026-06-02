package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;


    public AdminController(UserService userService) {
        this.userService = userService;

    }


    @GetMapping
    public ModelAndView getAllUsers(Principal principal) {
        ModelAndView mav = new ModelAndView("users");

        User user = userService.userByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        mav.addObject("user", user);
        mav.addObject("users", userService.getAllUsers());

        return mav;
    }

    @PostMapping("/add")
    public ModelAndView addUsers(@ModelAttribute("user") User user,
                           @RequestParam(value = "roleIds", required = false) Set<Long> roleIds){
        userService.saveUser(user, roleIds);

        return new ModelAndView("redirect:/admin");
    }

    @PostMapping("/delete")
    public ModelAndView deleteUsers(@RequestParam("id") Long id){

        userService.deleteUser(id);

        return new ModelAndView("redirect:/admin");

    }

    @PostMapping("/update")
    public ModelAndView updateUsers(@ModelAttribute("user") User user,
                              @RequestParam(value = "roleIds", required = false)
                              Set<Long> roleIds){

        userService.updateUser(user, roleIds);

        return new ModelAndView("redirect:/admin");
    }
}
