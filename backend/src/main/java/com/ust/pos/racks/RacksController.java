package com.ust.pos.racks;

import com.ust.pos.dto.RacksDto;
import com.ust.pos.racks.service.RacksService;
import com.ust.pos.shelfs.service.ShelfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/racks")
public class RacksController {
    public static final String RACKS = "racks";
    public static final String REDIRECT_RACKS_LIST = "redirect:/racks/list";
    @Autowired
    RacksService racksService;
    @Autowired
    ShelfsService shelfsService;

    @GetMapping("/list")
    public String home(Model model, @ModelAttribute RacksDto racksDto, Pageable pageable) {
        model.addAttribute(RACKS, racksService.findAll(pageable));
        return "racks/list";
    }

    @GetMapping("/add")
    public String addWarehouse(@ModelAttribute RacksDto racksDto, Model model, Pageable pageable) {
        model.addAttribute("shelfs", shelfsService.findAllActive());
        model.addAttribute(RACKS, racksService.findAll(pageable));
        return "racks/add";
    }

    @PostMapping("/add")
    public String doAddWarehouse(RedirectAttributes ra, Model model, @ModelAttribute RacksDto racksDto) {
        model.addAttribute(RACKS, racksDto);
        racksService.save(racksDto);
        ra.addFlashAttribute("message", racksDto.getMessage());
        ra.addFlashAttribute("success", racksDto.isSuccess());
        return REDIRECT_RACKS_LIST;
    }

    @GetMapping("/delete")
    public String deleteWarehouse(@RequestParam String identifier) {
        racksService.delete(identifier);
        return REDIRECT_RACKS_LIST;
    }

    @GetMapping("/get")
    public String update(@RequestParam String identifier, Model model, @ModelAttribute RacksDto racksDto, Pageable pageable) {
        RacksDto racksDto1 = racksService.findByIdentifier(identifier);
        model.addAttribute("shelfs", shelfsService.findAllActive());
        model.addAttribute(RACKS, racksService.findAll(pageable));
        model.addAttribute("racksDto", racksDto1);
        return "racks/racks";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute RacksDto racksDto) {
        racksService.update(racksDto);
        return REDIRECT_RACKS_LIST;
    }

    @PostMapping("/toggle")
    @ResponseBody
    public String toggle(@RequestParam String identifier, boolean status) {
        racksService.changeStatus(identifier, status);
        return "success";
    }
}
