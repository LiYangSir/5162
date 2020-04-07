package com.hrbeu5162.web.admin;

import com.hrbeu5162.po.Blog;
import com.hrbeu5162.po.User;
import com.hrbeu5162.po.WebBasic;
import com.hrbeu5162.service.BlogService;
import com.hrbeu5162.service.TagService;
import com.hrbeu5162.service.TypeService;
import com.hrbeu5162.service.UserService;
import com.hrbeu5162.util.SaveAndGetPicUtils;
import com.hrbeu5162.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT = "redirect:/admin/blogs";

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model, HttpSession session) {
        model.addAttribute("types", typeService.listType());
        blog.setUserId(userService.findUserByUsername((String)session.getAttribute("user")).getId());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model, HttpSession session) {
        blog.setUserId(userService.findUserByUsername((String)session.getAttribute("user")).getId());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }

    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    @GetMapping("/blogs/{id}/input")
    public String input(@PathVariable Long id, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        return INPUT;
    }

    @PostMapping("/blogsetting")
    public String postBasic() {
        return "redirect:/admin/blogsetting";
    }

    @GetMapping("/blogsetting")
    public String getBasic() {
        return "admin/blog-setting";
    }

    @PostMapping("/blogs")
    public String post(@RequestParam("firstPictures") MultipartFile srcFile, Blog blog ,RedirectAttributes attributes, HttpSession session) {
        blog.setUser(userService.findUserByUsername((String)session.getAttribute("user")));
        if (srcFile.isEmpty()){
            if (blog.getFirst()==null || blog.getFirst().equals(""))
                blog.setFirst("https://picsum.photos/800/500");
        }else {
            String src = SaveAndGetPicUtils.save(srcFile, uploadPath + blog.getUser().getUserName());
            blog.setFirst(src);
        }
        blog.setType(typeService.getType(blog.getType().getId()));
//        blogService.saveBlog(blog);
        Blog b;
        if (blog.getId() == null)
            b = blogService.saveBlog(blog);
        else
            b = blogService.updateBlog(blog.getId(), blog);
        blog.setTags(tagService.listTag(blog.getTagIds()));
        blogService.saveBlog(blog);
        if (b == null)
            attributes.addFlashAttribute("message", "操作失败");
        else
            attributes.addFlashAttribute("message", "操作成功");
        return REDIRECT;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, Blog blog, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT;
    }
}
