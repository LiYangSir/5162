package com.hrbeu5162.web.admin;

import com.hrbeu5162.po.Type;
import com.hrbeu5162.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.DESC)
                                     Pageable pageable, Model model) {  //必须调用domain
        model.addAttribute("page", typeService.listType(pageable));
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model) {
        model.addAttribute("type", new Type());
        return "admin/type-input";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("type", typeService.getType(id));
        return "admin/type-input";
    }

    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes) {
        if (typeService.getTypeByName(type.getName()) != null){  //处理重复
            result.rejectValue("name", "nameError", "不能重复添加分类");  //name需要和type保持一致
        }

        if (result.hasErrors()) { //处理空的情况
            return "admin/type-input";
        }

        Type t = typeService.saveType(type);
        if (t == null) {
            attributes.addFlashAttribute("message", "操作失败");
        }else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/types";
    }

    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {

        if (typeService.getTypeByName(type.getName()) != null){
            result.rejectValue("name", "nameError", "不能重复添加分类");  //name需要和type保持一致
        }

        if (result.hasErrors()) { //处理空的情况
            return "admin/type-input";
        }

        Type t = typeService.updateType(id, type);
        if (t == null) {
            attributes.addFlashAttribute("message", "更新失败");
        }else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/types";
    }
}