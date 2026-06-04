package com.ust.pos.api.cart;

import com.ust.pos.api.BaseController;
import com.ust.pos.cart.service.CartService;
import com.ust.pos.dto.CartDto;
import com.ust.pos.dto.CartEntryDto;
import com.ust.pos.dto.PaginationDto;
import com.ust.pos.dto.WsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartControllerApi extends BaseController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public CartDto addCart(@RequestBody CartDto cartDto){
        return cartService.save(cartDto);
    }

    @PostMapping("/getCart")
    public CartDto getCart(@RequestBody CartDto cartDto){
        return cartService.findByIdentifier(cartDto.getIdentifier());
    }

    @PostMapping("addToCart")
    public CartDto addToCart(@RequestBody CartEntryDto cartEntryDto){
        return cartService.recalculate(cartEntryDto.getCart());
    }

    @PostMapping("/delete")
    public boolean deleteCart(@RequestBody CartDto cartDto){
        return cartService.delete(cartDto.getIdentifier());
    }

    @PostMapping("/list")
    public WsDto<CartDto> list(@RequestBody PaginationDto paginationDto){
        Pageable pageable = getPageable(paginationDto.getPage(), paginationDto.getSizePerPage(), paginationDto.getSortDirection(), paginationDto.getSortField());
        return cartService.findAll(pageable);
    }
}
