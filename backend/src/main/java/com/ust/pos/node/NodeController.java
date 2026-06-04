package com.ust.pos.node;

import com.ust.pos.dto.NodeDto;
import com.ust.pos.dto.RoleDto;
import com.ust.pos.dto.WsDto;
import com.ust.pos.node.service.NodeService;
import com.ust.pos.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/node")
public class NodeController {

    public static final String REDIRECT_NODE_LIST = "redirect:/node/list";
    @Autowired
    private NodeService nodeService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public String home(Model model, Pageable pageable) {
        model.addAttribute("nodes", nodeService.findAll(pageable));
        return "node/list";
    }

    @GetMapping("/add")
    public String add(Model model, @ModelAttribute NodeDto nodeDto, Pageable pageable) {
        WsDto<RoleDto> roles = roleService.findAll(pageable);
        model.addAttribute("roles", roles);
        return "node/add";
    }

    @PostMapping("/add")
    public String addPost(Model model, @ModelAttribute NodeDto nodeDto) {
        NodeDto response = nodeService.save(nodeDto);
        if (!response.isSuccess()) {
            model.addAttribute("message", response.getMessage());
            return "redirect:/node/add?error=true";
        }
        return REDIRECT_NODE_LIST;
    }

    @GetMapping("/get")
    public String update(Model model, @RequestParam String identifier, @ModelAttribute NodeDto nodeDto) {
        NodeDto nodeDto1 = nodeService.findByIdentifier(identifier);
        model.addAttribute("node", nodeDto1);
        return "node/node";
    }

    @PostMapping("/update")
    public String updatePost(Model model, @ModelAttribute NodeDto nodeDto) {
        NodeDto response = nodeService.update(nodeDto);
        if (!response.isSuccess()) {
            model.addAttribute("message", response.getMessage());
            return "redirect:/node/get?error=true";
        }
        return REDIRECT_NODE_LIST;
    }

    @GetMapping("/delete")
    public String delete(Model model, @RequestParam String identifier) {
        nodeService.delete(identifier);
        return REDIRECT_NODE_LIST;
    }
}
