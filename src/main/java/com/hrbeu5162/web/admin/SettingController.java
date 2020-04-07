package com.hrbeu5162.web.admin;

import com.hrbeu5162.po.User;
import com.hrbeu5162.service.UserService;
import com.hrbeu5162.util.MD5Utils;
import com.hrbeu5162.util.SaveAndGetPicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class SettingController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private UserService userService;

    @GetMapping("/setting")
    public String changeMessage(Model model, HttpSession session) {
        User user = userService.findUserByUsername((String)session.getAttribute("user"));
        model.addAttribute("setting", user);
        model.addAttribute("countBlog", userService.countBlogByUser(user.getId()));
        return "admin/setting";
    }

    @PostMapping("/setting")
    public String postMessage(@Valid User user, RedirectAttributes attributes){
        User u = userService.updateUserMessage(user.getId(), user);
        attributes.addFlashAttribute("setting", u);
        if (u == null) {
            attributes.addFlashAttribute("message", "保存失败");
        }else {
            attributes.addFlashAttribute("message", "保存成功");
        }
        return "redirect:/admin/setting";
    }

    @PostMapping("/setting/password")
    public String postPassword(@RequestParam String oldPassword, @RequestParam String newPassword, Model model, HttpSession session) {
        User user = userService.findUserByUsername((String)session.getAttribute("user"));
        String oldMD5Password = MD5Utils.code(oldPassword);
        if (user.getPassword().equals(oldMD5Password)) {
            user.setPassword(MD5Utils.code(newPassword));
            userService.save(user);
            model.addAttribute("message", "密码修改成功");
            model.addAttribute("setting", user);
        }else{
            model.addAttribute("setting", user);
            model.addAttribute("message", "密码错误");
        }
        return "admin/setting";
    }

    @PostMapping("/setting/avatar")
    public String changeAvatar(@RequestParam("userAvatar") MultipartFile srcFile, HttpSession session) {
        User user = userService.findUserByUsername((String)session.getAttribute("user"));
        if (srcFile.isEmpty()){
            if (user.getAvatar().equals("") || user.getAvatar()==null)
                user.setAvatar("https://picsum.photos/500/800");
        }else {
            String src = SaveAndGetPicUtils.saveAvatar(srcFile, uploadPath + user.getUserName());
            user.setAvatar(src);
            userService.save(user);
        }
        return "redirect:/admin/setting";
    }
}
