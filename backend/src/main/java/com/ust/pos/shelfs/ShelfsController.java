package com.ust.pos.shelfs;

import com.ust.pos.dto.ShelfsDto;
import com.ust.pos.shelfs.service.ShelfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/shelfs")
public class ShelfsController {
    public static final String SHELFS = "shelfs";
    public static final String REDIRECT_SHELFS_LIST = "redirect:/shelfs/list";
    @Autowired
    ShelfsService shelfsService;

    @GetMapping("/list")
    public String home(Model model, @ModelAttribute ShelfsDto shelfsDto, Pageable pageable) {
        model.addAttribute(SHELFS, shelfsService.findAll(pageable));
        return "shelfs/list";
    }

    @GetMapping("/add")
    public String addWarehouse(@ModelAttribute ShelfsDto shelfsDto, Model model, Pageable pageable) {
        model.addAttribute(SHELFS, shelfsService.findAll(pageable));
        return "shelfs/add";
    }

    @PostMapping("/add")
    public String doAddWarehouse(RedirectAttributes ra, Model model, @ModelAttribute ShelfsDto shelfsDto) {
        model.addAttribute(SHELFS, shelfsDto);
        shelfsService.save(shelfsDto);
        ra.addFlashAttribute("message", shelfsDto.getMessage());
        ra.addFlashAttribute("success", shelfsDto.isSuccess());
        return REDIRECT_SHELFS_LIST;
    }

    @GetMapping("/delete")
    public String deleteWarehouse(@RequestParam String identifier) {
        shelfsService.delete(identifier);
        return REDIRECT_SHELFS_LIST;
    }

    @GetMapping("/get")
    public String update(@RequestParam String identifier, Model model, @ModelAttribute ShelfsDto shelfsDto, Pageable pageable) {
        ShelfsDto shelfsDto1 = shelfsService.findByIdentifier(identifier);
        model.addAttribute(SHELFS, shelfsService.findAll(pageable));
        model.addAttribute("shelfsDto", shelfsDto1);
        return "shelfs/shelfs";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute ShelfsDto shelfsDto) {
        shelfsService.update(shelfsDto);
        return REDIRECT_SHELFS_LIST;
    }

    @PostMapping("/toggle")
    @ResponseBody
    public String toggle(@RequestParam String identifier, boolean status) {
        shelfsService.statusUpdate(identifier, status);
        return "success";
    }
}
