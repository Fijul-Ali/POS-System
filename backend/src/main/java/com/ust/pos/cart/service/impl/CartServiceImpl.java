package com.ust.pos.cart.service.impl;

import com.ust.pos.cart.service.CartService;
import com.ust.pos.cartentry.service.CartEntryService;
import com.ust.pos.dto.CartDto;
import com.ust.pos.dto.CartEntryDto;
import com.ust.pos.dto.UserDto;
import com.ust.pos.dto.WsDto;
import com.ust.pos.model.Cart;
import com.ust.pos.model.CartRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CartEntryService cartEntryService;

    @Override
    public CartDto save(CartDto cartDto) {
        String identifier = cartDto.getIdentifier();
        if (cartRepository.existsByIdentifier(identifier)) {
            cartDto.setMessage("Already exists");
            cartDto.setSuccess(false);
            return cartDto;
        }
        Cart cart = modelMapper.map(cartDto, Cart.class);
        cartRepository.save(cart);
        return cartDto;
    }

    @Override
    public CartDto recalculate(String cart) {
        List<CartEntryDto> cartEntries = cartEntryService.findAllCarts(cart);
        Cart cartModel = cartRepository.findByIdentifier(cart);
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        BigDecimal totalMrp = BigDecimal.ZERO;

        for(CartEntryDto cartEntryDto: cartEntries){
            totalPrice = totalPrice.add(cartEntryDto.getTotalPrice());
            totalDiscount = totalDiscount.add(cartEntryDto.getDiscount());
            totalMrp = totalMrp.add(cartEntryDto.getTotalMrp());
        }

        cartModel.setTotalDiscount(totalDiscount);
        cartModel.setTotalPrice(totalPrice);
        cartModel.setOriginalPrice(totalMrp);
        cartRepository.save(cartModel);
        CartDto cartDto = modelMapper.map(cartModel, CartDto.class);
        Type listType = new TypeToken<List<CartDto>>() {
        }.getType();
        cartDto.setEntryDtoList(modelMapper.map(cartEntries, listType));
        return cartDto;
    }

    @Override
    public CartDto findByIdentifier(String identifier) {
        Cart cart = cartRepository.findByIdentifier(identifier);
        CartDto cartDto = modelMapper.map(cart, CartDto.class);
        cartDto.setEntryDtoList(cartEntryService.findAllCarts(cartDto.getIdentifier()));
        return cartDto;
    }

    @Override
    public WsDto<CartDto> findAll(Pageable pageable) {
        Type listType = new TypeToken<List<CartDto>>() {
        }.getType();
        Page<Cart> cartPage = cartRepository.findAll(pageable);
        WsDto<CartDto> cartWsDto = new WsDto<>();
        cartWsDto.setDtoList(modelMapper.map(cartPage.getContent(), listType));
        cartWsDto.setTotalRecords(cartPage.getTotalElements());
        cartWsDto.setTotalPages(cartPage.getTotalPages());
        cartWsDto.setSizePerPage(pageable.getPageSize());
        cartWsDto.setPage(pageable.getPageNumber());

        return cartWsDto;
    }

    @Override
    public boolean delete(String identifier) {
        cartRepository.deleteByIdentifier(identifier);
        return true;
    }

    @Override
    public CartDto update(CartDto cartDto) {
        Cart cart = cartRepository.findByIdentifier(cartDto.getIdentifier());
        modelMapper.map(cartDto, cart);
        cartRepository.save(cart);
        return cartDto;
    }

    @Override
    public List<CartDto> findAllActive() {
        Type listType = new TypeToken<List<CartDto>>() {
        }.getType();
        return modelMapper.map(cartRepository.findByStatus(true), listType);
    }

    @Override
    public CartDto changeCartStatus(String identifier, boolean status) {
        Cart cart = cartRepository.findByIdentifier(identifier);

        if (cart != null) {
            cart.setStatus(status);
            cartRepository.save(cart);
        }
        return modelMapper.map(cart, CartDto.class);
    }
}
