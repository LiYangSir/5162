package com.hrbeu5162.web;


import com.hrbeu5162.po.Comment;
import com.hrbeu5162.po.User;
import com.hrbeu5162.service.BlogService;
import com.hrbeu5162.service.CommentService;
import com.hrbeu5162.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }


    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {
        Long blogId = comment.getBlog().getId();
        User user = blogService.getBlog(blogId).getUser();
        String email = user.getEmail();
        comment.setBlog(blogService.getBlog(blogId));
        if (email.equals(comment.getEmail())) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
            comment.setNickName(user.getNickName());
        } else {
            comment.setAvatar(avatar);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }


}
