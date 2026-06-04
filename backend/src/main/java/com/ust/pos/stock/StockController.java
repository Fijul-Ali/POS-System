package com.ust.pos.stock;

import com.ust.pos.dto.StockDto;
import com.ust.pos.dto.WarehouseDto;
import com.ust.pos.product.service.ProductService;
import com.ust.pos.stock.service.StockService;
import com.ust.pos.warehouse.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/stock")
public class StockController {
    public static final String PRODUCT = "product";
    public static final String WAREHOUSES = "warehouses";
    public static final String REDIRECT_STOCK_LIST = "redirect:/stock/list";
    @Autowired
    StockService stockService;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    ProductService productService;

    @GetMapping("/list")
    public String home(Model model, @ModelAttribute StockDto stockDto, Pageable pageable) {
        model.addAttribute(PRODUCT, productService.findAll(pageable));
        model.addAttribute(WAREHOUSES, warehouseService.findAll(pageable));
        model.addAttribute("stocks", stockService.findAll(pageable));
        return "stock/list";
    }

    @GetMapping("/add")
    public String addStock(@ModelAttribute StockDto stockDto, Model model, WarehouseDto warehouseDto) {
        model.addAttribute(PRODUCT, productService.findAllActive());
        model.addAttribute(WAREHOUSES, warehouseService.findAllActive());
        return "stock/add";
    }

    @PostMapping("/add")
    public String doAddStock(RedirectAttributes ra, Model model, @ModelAttribute StockDto stockDto) {
        model.addAttribute("stocks", stockDto);
        stockService.save(stockDto);
        ra.addFlashAttribute("message", stockDto.getMessage());
        ra.addFlashAttribute("success", stockDto.isSuccess());
        return REDIRECT_STOCK_LIST;
    }

    @GetMapping("/delete")
    public String deleteStock(@RequestParam String identifier) {
        stockService.delete(identifier);
        return REDIRECT_STOCK_LIST;
    }

    @GetMapping("/get")
    public String update(@RequestParam String identifier, Model model, @ModelAttribute StockDto stockDto) {
        model.addAttribute(PRODUCT, productService.findAllActive());
        model.addAttribute(WAREHOUSES, warehouseService.findAllActive());
        StockDto stockDto1 = stockService.findByIdentifier(identifier);
        model.addAttribute("stockDto", stockDto1);
        return "stock/stock";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute StockDto stockDto) {
        stockService.update(stockDto);
        return REDIRECT_STOCK_LIST;
    }
}
