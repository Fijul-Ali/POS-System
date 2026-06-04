package com.ust.pos;

import com.ust.pos.brand.service.BrandService;
import com.ust.pos.node.service.NodeService;
import com.ust.pos.shelfs.service.ShelfsService;
import com.ust.pos.warehouse.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    public static final String NODES = "nodes";
    @Autowired
    BrandService brandService;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    ShelfsService shelfsService;
    @Autowired
    private NodeService nodeService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute(NODES, nodeService.getNodesForRoles());

        List<?> brands = brandService.findAllForHome();
        model.addAttribute("brandCount", brands != null ? brands.size() : 0);

        List<?> warehouses = warehouseService.findAllActive();
        model.addAttribute("warehouseCount", warehouses != null ? warehouses.size() : 0);

        List<?> shelfs = shelfsService.findAllForHome();
        model.addAttribute("shelfCount", shelfs != null ? shelfs.size() : 0);

        if (model.getAttribute(NODES) != null) {
            List<?> nodes = (List<?>) model.getAttribute(NODES);
            model.addAttribute("moduleCount", nodes != null ? nodes.size() : 0);
        } else {
            model.addAttribute("moduleCount", 0);
        }

        return "home";
    }
}
