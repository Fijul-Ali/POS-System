package com.ust.pos.api.order;

import com.ust.pos.api.BaseController;
import com.ust.pos.dto.OrderDto;
import com.ust.pos.dto.PaginationDto;
import com.ust.pos.dto.WsDto;
import com.ust.pos.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderControllerApi extends BaseController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/list")
    public WsDto<OrderDto> list(@RequestBody PaginationDto paginationDto) {
        Pageable pageable = getPageable(paginationDto.getPage(), paginationDto.getSizePerPage(),
                paginationDto.getSortDirection(), paginationDto.getSortField());
        return orderService.findAll(pageable);
    }

    @GetMapping("/get")
    public OrderDto get(@RequestParam String identifier) {
        return orderService.findByIdentifier(identifier);
    }

    @GetMapping("/delete")
    public boolean delete(@RequestParam String identifier) {
        try {
            orderService.delete(identifier);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @PostMapping("/place")
    public OrderDto placeOrder(@RequestBody OrderDto orderDto) {
        return orderService.placeOrder(orderDto.getCart());
    }

    @PostMapping("/cancel")
    public OrderDto cancel(@RequestBody OrderDto orderDto) {
        return orderService.cancel(orderDto.getIdentifier());
    }

}