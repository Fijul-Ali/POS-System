package com.ust.pos.category;

import com.ust.pos.category.service.CategoryService;
import com.ust.pos.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/category")
public class CategoryController {
    public static final String CATEGORIES = "categories";
    public static final String REDIRECT_CATEGORY_LIST = "redirect:/category/list";
    @Autowired
    CategoryService categoryService;

    @GetMapping("/list")
    public String home(Model model, @ModelAttribute CategoryDto categoryDto, Pageable pageable) {
        model.addAttribute(CATEGORIES, categoryService.findAll(pageable));
        return "category/list";
    }

    @GetMapping("/add")
    public String addWarehouse(@ModelAttribute CategoryDto categoryDto, Model model) {
        model.addAttribute(CATEGORIES, categoryService.findAllActive());
        return "category/add";
    }

    @PostMapping("/add")
    public String doAddWarehouse(RedirectAttributes ra, Model model, @ModelAttribute CategoryDto categoryDto) {
        model.addAttribute(CATEGORIES, categoryDto);
        categoryService.save(categoryDto);
        ra.addFlashAttribute("message", categoryDto.getMessage());
        ra.addFlashAttribute("success", categoryDto.isSuccess());
        return REDIRECT_CATEGORY_LIST;
    }

    @GetMapping("/delete")
    public String deleteWarehouse(@RequestParam String identifier) {
        categoryService.delete(identifier);
        return REDIRECT_CATEGORY_LIST;
    }

    @GetMapping("/get")
    public String update(@RequestParam String identifier, Model model, @ModelAttribute CategoryDto categoryDto) {
        CategoryDto categoryDto1 = categoryService.findByIdentifier(identifier);
        model.addAttribute(CATEGORIES, categoryService.findAllActive());
        model.addAttribute("categoryDto", categoryDto1);
        return "category/category";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute CategoryDto categoryDto) {
        categoryService.update(categoryDto);
        return REDIRECT_CATEGORY_LIST;
    }

    @PostMapping("/toggle")
    @ResponseBody
    public String toggle(@RequestParam String identifier, boolean status) {
        categoryService.updateStatus(identifier, status);
        return "success";
    }
}
