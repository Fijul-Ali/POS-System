package com.ust.pos.customer;

import com.ust.pos.api.BaseController;
import com.ust.pos.customer.service.CustomerService;
import com.ust.pos.dto.CustomerDto;
import com.ust.pos.node.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {

    public static final String REDIRECT_ROLE_LIST = "redirect:/customer/list";

    @Autowired
    private CustomerService customerService;
    @Autowired
    private NodeService nodeService;

    @GetMapping("/list")
    public String home(Model model, Pageable pageable) {
        model.addAttribute("customers", customerService.findAll(pageable));
        return "customer/list";
    }

    @GetMapping("/add")
    public String add(Model model, @ModelAttribute CustomerDto customerDto) {
        return "customer/add";
    }

    @PostMapping("/add")
    public String addPost(Model model, @ModelAttribute CustomerDto customerDto) {
        CustomerDto response = customerService.save(customerDto);
        if (!response.isSuccess()) {
            model.addAttribute("message", response.getMessage());
            return "redirect:/customer/add?error=true";
        }
        return REDIRECT_ROLE_LIST;
    }

    @GetMapping("/get")
    public String update(Model model, @RequestParam String phoneNo) {
        CustomerDto response = customerService.findByAddress(phoneNo);
        model.addAttribute("customerDto", response);

        return "customer/customer";
    }

    @PostMapping("/update")
    public String updatePost(Model model, @ModelAttribute CustomerDto userDto) {
        CustomerDto response = customerService.update(userDto);
        if (!response.isSuccess()) {
            model.addAttribute("message", response.getMessage());
        }
        return REDIRECT_ROLE_LIST;
    }

    @GetMapping("/delete")
    public String delete(@RequestParam String phoneNo) {
        customerService.delete(phoneNo);
        return REDIRECT_ROLE_LIST;
    }
}
