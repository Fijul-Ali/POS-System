package com.ust.pos.product;

import com.ust.pos.brand.service.BrandService;
import com.ust.pos.category.service.CategoryService;
import com.ust.pos.dto.ProductDto;
import com.ust.pos.models.service.ModelsService;
import com.ust.pos.product.service.ProductService;
import com.ust.pos.unit.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/product")
public class ProductController {
    public static final String REDIRECT_PRODUCT_LIST = "redirect:/product/list";
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    UnitService unitService;
    @Autowired
    BrandService brandService;
    @Autowired
    ModelsService modelsService;

    @GetMapping("/list")
    public String home(Model model, @ModelAttribute ProductDto productDto, Pageable pageable) {
        model.addAttribute("products", productService.findAll(pageable));
        return "product/list";
    }

    @GetMapping("/add")
    public String addProduct(@ModelAttribute ProductDto productDto, Model model) {
        model.addAttribute("categories", categoryService.findAllActiveChilds());
        model.addAttribute("units", unitService.findAllActive());
        model.addAttribute("brands", brandService.findAllActive());
        model.addAttribute("models", modelsService.findAllActive());
        return "product/add";
    }

    @PostMapping("/add")
    public String doAddProduct(RedirectAttributes ra, Model model, @ModelAttribute ProductDto productDto) {
        model.addAttribute("products", productDto);
        productService.save(productDto);
        ra.addFlashAttribute("message", productDto.getMessage());
        ra.addFlashAttribute("success", productDto.isSuccess());
        return REDIRECT_PRODUCT_LIST;
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam String identifier) {
        productService.delete(identifier);
        return REDIRECT_PRODUCT_LIST;
    }

    @GetMapping("/get")
    public String update(@RequestParam String identifier, Model model, @ModelAttribute ProductDto productDto) {
        model.addAttribute("categories", categoryService.findAllActiveChilds());
        model.addAttribute("units", unitService.findAllActive());
        model.addAttribute("brands", brandService.findAllActive());
        model.addAttribute("models", modelsService.findAllActive());
        ProductDto productDto1 = productService.findByIdentifier(identifier);
        model.addAttribute("productDto", productDto1);
        return "product/product";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute ProductDto productDto) {
        productService.update(productDto);
        return REDIRECT_PRODUCT_LIST;
    }

    @PostMapping("/toggle")
    @ResponseBody
    public String toggle(@RequestParam String identifier, boolean status) {
        productService.updateStatus(identifier, status);
        return "success";
    }
}
