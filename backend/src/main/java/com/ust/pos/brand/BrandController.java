package com.ust.pos.brand;

import com.ust.pos.brand.service.BrandService;
import com.ust.pos.dto.BrandDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/brand")
public class BrandController {
    public static final String BRANDS = "brands";
    public static final String REDIRECT_BRAND_LIST = "redirect:/brand/list";
    @Autowired
    BrandService brandService;

    @GetMapping("/list")
    public String home(Model model, @ModelAttribute BrandDto brandDto, Pageable pageable) {
        model.addAttribute(BRANDS, brandService.findAll(pageable));
        return "brand/list";
    }

    @GetMapping("/add")
    public String addWarehouse(@ModelAttribute BrandDto brandDto, Model model, Pageable pageable) {
        model.addAttribute(BRANDS, brandService.findAll(pageable));
        return "brand/add";
    }

    @PostMapping("/add")
    public String doAddWarehouse(RedirectAttributes ra, Model model, @ModelAttribute BrandDto brandDto) {
        model.addAttribute(BRANDS, brandDto);
        brandService.save(brandDto);
        ra.addFlashAttribute("message", brandDto.getMessage());
        ra.addFlashAttribute("success", brandDto.isSuccess());
        return REDIRECT_BRAND_LIST;
    }

    @GetMapping("/delete")
    public String deleteWarehouse(@RequestParam String identifier) {
        brandService.delete(identifier);
        return REDIRECT_BRAND_LIST;
    }

    @GetMapping("/get")
    public String update(@RequestParam String identifier, Model model, @ModelAttribute BrandDto brandDto, Pageable pageable) {
        BrandDto brandDto1 = brandService.findByIdentifier(identifier);
        model.addAttribute(BRANDS, brandService.findAll(pageable));
        model.addAttribute("brandDto", brandDto1);
        return "brand/brand";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute BrandDto brandDto) {
        brandService.update(brandDto);
        return REDIRECT_BRAND_LIST;
    }

    @PostMapping("/toggleStatus")
    @ResponseBody
    public String toggleStatus(@RequestParam String identifier, boolean status) {
        brandService.changeBrandStatus(identifier, status);
        return "success";
    }
}
