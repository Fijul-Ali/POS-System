package com.ust.pos.api.cart;

import com.ust.pos.api.BaseController;
import com.ust.pos.cartentry.service.CartEntryService;
import com.ust.pos.dto.CartDto;
import com.ust.pos.dto.CartEntryDto;
import com.ust.pos.dto.PaginationDto;
import com.ust.pos.dto.WsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cartEntry")
public class CartEntryControllerApi extends BaseController {

    @Autowired
    private CartEntryService cartEntryService;

    @PostMapping("/addEntry")
    public CartEntryDto addPost(@RequestBody CartEntryDto cartEntryDto) {
        return cartEntryService.save(cartEntryDto);
    }

    @PostMapping("delete")
    public boolean deleteEntry(@RequestBody CartEntryDto cartEntryDto){
        return cartEntryService.delete(cartEntryDto.getIdentifier());
    }

    @PostMapping("/list")
    public WsDto<CartEntryDto> list(@RequestBody PaginationDto paginationDto){
        Pageable pageable = getPageable(paginationDto.getPage(), paginationDto.getSizePerPage(), paginationDto.getSortDirection(), paginationDto.getSortField());
        return cartEntryService.findAll(pageable);
    }
}
