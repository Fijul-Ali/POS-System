package com.ust.pos.price;

import com.ust.pos.dto.PriceDto;
import com.ust.pos.price.service.PriceService;
import com.ust.pos.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/price")
public class PriceController {
    public static final String REDIRECT_PRICE_LIST = "redirect:/price/list";
    @Autowired
    PriceService priceService;
    @Autowired
    ProductService productService;

    @GetMapping("/list")
    public String home(Model model, @ModelAttribute PriceDto priceDto, Pageable pageable) {
        model.addAttribute("prices", priceService.findAll(pageable));
        return "price/list";
    }

    @GetMapping("/add")
    public String addPrice(@ModelAttribute PriceDto priceDto, Model model) {
        model.addAttribute("products", productService.findAllActive());
        return "price/add";
    }

    @PostMapping("/add")
    public String doAddPrice(RedirectAttributes ra, Model model, @ModelAttribute PriceDto priceDto) {
        model.addAttribute("prices", priceDto);
        priceService.save(priceDto);
        ra.addFlashAttribute("message", priceDto.getMessage());
        ra.addFlashAttribute("success", priceDto.isSuccess());
        return REDIRECT_PRICE_LIST;
    }

    @GetMapping("/delete")
    public String deletePrice(@RequestParam String identifier) {
        priceService.delete(identifier);
        return REDIRECT_PRICE_LIST;
    }

    @GetMapping("/get")
    public String update(@RequestParam String identifier, Model model, @ModelAttribute PriceDto priceDto, Pageable pageable) {
        model.addAttribute("products", productService.findAllActive());
        PriceDto priceDto1 = priceService.findByIdentifier(identifier);
        model.addAttribute("priceDto", priceDto1);
        return "price/price";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute PriceDto priceDto) {
        priceService.update(priceDto);
        return REDIRECT_PRICE_LIST;
    }
}
