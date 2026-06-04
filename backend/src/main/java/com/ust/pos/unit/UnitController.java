package com.ust.pos.unit;

import com.ust.pos.dto.UnitDto;
import com.ust.pos.unit.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/unit")
public class UnitController {
    public static final String UNITS = "units";
    public static final String REDIRECT_UNIT_LIST = "redirect:/unit/list";
    @Autowired
    UnitService unitService;

    @GetMapping("/list")
    public String home(Model model, @ModelAttribute UnitDto unitDto, Pageable pageable) {
        model.addAttribute(UNITS, unitService.findAll(pageable));
        return "unit/list";
    }

    @GetMapping("/add")
    public String addWarehouse(@ModelAttribute UnitDto unitDto, Model model, Pageable pageable) {
        model.addAttribute(UNITS, unitService.findAll(pageable));
        return "unit/add";
    }

    @PostMapping("/add")
    public String doAddWarehouse(RedirectAttributes ra, Model model, @ModelAttribute UnitDto unitDto) {
        model.addAttribute(UNITS, unitDto);
        unitService.save(unitDto);
        ra.addFlashAttribute("message", unitDto.getMessage());
        ra.addFlashAttribute("success", unitDto.isSuccess());
        return REDIRECT_UNIT_LIST;
    }

    @GetMapping("/delete")
    public String deleteWarehouse(@RequestParam String identifier) {
        unitService.delete(identifier);
        return REDIRECT_UNIT_LIST;
    }

    @GetMapping("/get")
    public String update(@RequestParam String identifier, Model model, @ModelAttribute UnitDto unitDto, Pageable pageable) {
        UnitDto unitDto1 = unitService.findByIdentifier(identifier);
        model.addAttribute(UNITS, unitService.findAll(pageable));
        model.addAttribute("unitDto", unitDto1);
        return "unit/unit";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute UnitDto unitDto) {
        unitService.update(unitDto);
        return REDIRECT_UNIT_LIST;
    }

    @PostMapping("/toggle")
    @ResponseBody
    public String toggle(@RequestParam String identifier, boolean status) {
        unitService.updateStatus(identifier, status);
        return "success";
    }
}
