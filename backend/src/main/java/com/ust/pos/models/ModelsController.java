package com.ust.pos.models;

import com.ust.pos.brand.service.BrandService;
import com.ust.pos.dto.ModelsDto;
import com.ust.pos.models.service.ModelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/models")
public class ModelsController {
    public static final String MODELS = "models";
    public static final String REDIRECT_MODELS_LIST = "redirect:/models/list";
    @Autowired
    ModelsService modelsService;
    @Autowired
    BrandService brandService;

    @GetMapping("/list")
    public String home(Model model, @ModelAttribute ModelsDto modelsDto, Pageable pageable) {
        model.addAttribute(MODELS, modelsService.findAll(pageable));
        return "models/list";
    }

    @GetMapping("/add")
    public String addWarehouse(@ModelAttribute ModelsDto modelsDto, Model model, Pageable pageable) {
        model.addAttribute("brands", brandService.findAllActive());
        model.addAttribute(MODELS, modelsService.findAll(pageable));
        return "models/add";
    }

    @PostMapping("/add")
    public String doAddWarehouse(RedirectAttributes ra, Model model, @ModelAttribute ModelsDto modelsDto) {
        model.addAttribute(MODELS, modelsDto);
        modelsService.save(modelsDto);
        ra.addFlashAttribute("message", modelsDto.getMessage());
        ra.addFlashAttribute("success", modelsDto.isSuccess());
        return REDIRECT_MODELS_LIST;
    }

    @GetMapping("/delete")
    public String deleteWarehouse(@RequestParam String identifier) {
        modelsService.delete(identifier);
        return REDIRECT_MODELS_LIST;
    }

    @GetMapping("/get")
    public String update(@RequestParam String identifier, Model model, @ModelAttribute ModelsDto modelsDto, Pageable pageable) {
        ModelsDto modelsDto1 = modelsService.findByIdentifier(identifier);
        model.addAttribute("brands", brandService.findAllActive());
        model.addAttribute(MODELS, modelsService.findAll(pageable));
        model.addAttribute("modelsDto", modelsDto1);
        return "models/model";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute ModelsDto modelsDto) {
        modelsService.update(modelsDto);
        return REDIRECT_MODELS_LIST;
    }

    @PostMapping("/toggle")
    @ResponseBody
    public String toggle(@RequestParam String identifier, boolean status) {
        modelsService.updateStatus(identifier, status);
        return "success";
    }
}
