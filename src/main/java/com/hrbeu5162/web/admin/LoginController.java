package com.hrbeu5162.web.admin;

import com.hrbeu5162.po.User;
import com.hrbeu5162.service.UserService;
import com.hrbeu5162.vo.NavQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.nio.channels.SeekableByteChannel;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String loginPage() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes, Model model) {
        User user = userService.checkUser(username, password);
        if (user != null) {
            user.setPassword(null);  //密码不进行传送
            NavQuery navQuery = new NavQuery(username, user.getAvatar(), user.isTeacher(), user.isRoot());
            session.setAttribute("user", username);
            session.setAttribute("navQuery", navQuery);
//            session.setAttribute("avatar",user.getAvatar());
//            session.setAttribute("teacher",user.isTeacher());
//            session.setAttribute("root",user.isRoot());
            return "admin/index";
        } else {
            attributes.addFlashAttribute("messages", "用户名或者密码错误");
            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
