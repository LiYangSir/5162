package com.hrbeu5162.web;

import com.hrbeu5162.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @Autowired
    private UserService userService;

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("users", userService.listUser());
        return "about";
    }
}
