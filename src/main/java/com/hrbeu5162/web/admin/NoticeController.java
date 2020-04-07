package com.hrbeu5162.web.admin;


import com.hrbeu5162.po.Notice;
import com.hrbeu5162.po.Tag;
import com.hrbeu5162.po.User;
import com.hrbeu5162.service.NoticeService;
import com.hrbeu5162.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/admin")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UserService userService;

    @PostMapping("/notices")  //新增提交
    public String notices(Notice notice, RedirectAttributes attributes, HttpSession session) {
        Notice b;
        User user = userService.findUserByUsername((String) session.getAttribute("user"));
        notice.setUser(user);
        b = noticeService.saveNotice(notice);
        if (b == null)
            attributes.addFlashAttribute("message", "操作失败");
        else
            attributes.addFlashAttribute("message", "操作成功");
        return "redirect:/admin/notices";
    }

    @PostMapping("/notices/{id}")    //编辑提交
    public String editNotices(Notice notice, RedirectAttributes attributes, HttpSession session) {
        Notice b;
        b = noticeService.updateNotice(notice.getId(), notice);
        if (b == null)
            attributes.addFlashAttribute("message", "操作失败");
        else
            attributes.addFlashAttribute("message", "操作成功");
        return "redirect:/admin/notices";
    }

    @GetMapping("/notices/{id}/input")  //编辑
    public String editNotice(@PathVariable Long id, Model model) {
        model.addAttribute("notice", noticeService.getNotice(id));
        return "admin/notices-input";
    }

    @GetMapping("/notices")  //分页显示
    public String notice(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         Model model) {
        model.addAttribute("page", noticeService.listNotice(pageable));
        return "admin/notices";
    }

    @GetMapping("/notices/input")  //新增打开
    public String noticeInput(Model model) {
        model.addAttribute("notice", new Notice());
        return "admin/notices-input";
    }

    @GetMapping("/notices/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        noticeService.deleteNotice(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/notices";
    }
}
