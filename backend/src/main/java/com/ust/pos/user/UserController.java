package com.ust.pos.user;

import com.ust.pos.dto.UserDto;
import com.ust.pos.node.service.NodeService;
import com.ust.pos.role.service.RoleService;
import com.ust.pos.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private NodeService nodeService;

    @GetMapping("/list")
    public String home(Model model, Pageable pageable) {
        model.addAttribute("users", userService.findAll(pageable));
        model.addAttribute("roles", roleService.findAll(pageable));
        model.addAttribute("nodes", nodeService.getNodesForRoles());
        return "user/list";
    }

    @GetMapping("/get")
    public String update(Model model, @RequestParam String username, @ModelAttribute UserDto userDto, Pageable pageable) {
        UserDto response = userService.findByUserName(username);
        model.addAttribute("roles", roleService.findAll(pageable));
        model.addAttribute("user", response);
        model.addAttribute("userDto", response);
        return "user/user";
    }

    @PostMapping("/update")
    public String updatePost(Model model, @ModelAttribute UserDto userDto) {
        UserDto response = userService.update(userDto);
        if (!response.isSuccess()) {
            model.addAttribute("message", response.getMessage());
            return "user/user";
        }
        return "redirect:/user/list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam String username, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String currentUsername = auth.getName();
            userService.delete(username);

            if (currentUsername.equals(username)) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
                return "redirect:/login?deleted=true";
            }
        }
        return "redirect:/user/list";
    }
}
