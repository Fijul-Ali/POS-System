package com.ust.pos.cartentry.service.impl;

import com.ust.pos.cartentry.service.CartEntryService;
import com.ust.pos.dto.CartEntryDto;
import com.ust.pos.dto.WsDto;
import com.ust.pos.model.CartEntry;
import com.ust.pos.model.CartEntryRepository;
import com.ust.pos.model.Price;
import com.ust.pos.model.PriceRepository;
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
public class CartEntryServiceImpl implements CartEntryService {
    @Autowired
    CartEntryRepository cartEntryRepository;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PriceRepository priceRepository;

    @Override
    public CartEntryDto save(CartEntryDto cartEntryDto) {
        Price price = priceRepository.findByProduct(cartEntryDto.getProduct());
        String identifier = cartEntryDto.getProduct()+"-"+cartEntryDto.getCart();
        CartEntry cartEntry = cartEntryRepository.findByIdentifier(identifier);
        if(cartEntry==null){
            cartEntry = new CartEntry();
        }
        BigDecimal qty = cartEntryDto.getQuantity();
        BigDecimal quantity = cartEntry.getQuantity();
        qty = quantity.add(qty);
        cartEntryDto.setQuantity(qty);
        BigDecimal totalPrice = price.getSellingPrice().multiply(qty);
        BigDecimal discount = price.getMrp().subtract(price.getSellingPrice());
        BigDecimal totalMrp = price.getMrp().multiply(qty);
        cartEntryDto.setDiscount(discount.multiply(qty));
        cartEntryDto.setTotalMrp(totalMrp);
        cartEntryDto.setTotalPrice(totalPrice);
        cartEntryDto.setIdentifier(cartEntryDto.getProduct()+"-"+cartEntryDto.getCart());
        cartEntryDto.setPrice(price.getSellingPrice());
        cartEntryDto.setMrp(price.getMrp());
        cartEntryDto.setCostPrice(price.getCostPrice());
        modelMapper.map(cartEntryDto, cartEntry);
        cartEntryRepository.save(cartEntry);
        return cartEntryDto;
    }

    @Override
    public List<CartEntryDto> findAllCarts(String cart) {
        List<CartEntry> cartEntryList = cartEntryRepository.findByCart(cart);
        Type listType = new TypeToken<List<CartEntryDto>>() {
        }.getType();
        return modelMapper.map(cartEntryList, listType);
    }

    @Override
    public WsDto<CartEntryDto> findAll(Pageable pageable) {
        Type listType = new TypeToken<List<CartEntryDto>>() {
        }.getType();
        Page<CartEntry> cartEntryPage = cartEntryRepository.findAll(pageable);
        WsDto<CartEntryDto> cartEntryWsDto = new WsDto<>();
        cartEntryWsDto.setDtoList(modelMapper.map(cartEntryPage.getContent(), listType));
        cartEntryWsDto.setTotalRecords(cartEntryPage.getTotalElements());
        cartEntryWsDto.setTotalPages(cartEntryPage.getTotalPages());
        cartEntryWsDto.setSizePerPage(pageable.getPageSize());
        cartEntryWsDto.setPage(pageable.getPageNumber());

        return cartEntryWsDto;
    }

    @Override
    public boolean delete(String identifier) {
        cartEntryRepository.deleteByIdentifier(identifier);
        return true;
    }

    @Override
    public CartEntryDto findByIdentifier(String identifier) {
        CartEntry cartEntry = cartEntryRepository.findByIdentifier(identifier);
        return modelMapper.map(cartEntry, CartEntryDto.class);
    }

    @Override
    public CartEntryDto update(CartEntryDto cartEntryDto) {
        CartEntry cartEntry = cartEntryRepository.findByIdentifier(cartEntryDto.getIdentifier());
        modelMapper.map(cartEntryDto, cartEntry);
        cartEntryRepository.save(cartEntry);
        return cartEntryDto;
    }

    @Override
    public List<CartEntryDto> findAllActive() {
        Type listType = new TypeToken<List<CartEntryDto>>() {
        }.getType();
        return modelMapper.map(cartEntryRepository.findByStatus(true), listType);
    }

    @Override
    public CartEntryDto changeCartEntryStatus(String identifier, boolean status) {
        CartEntry cartEntry = cartEntryRepository.findByIdentifier(identifier);

        if (cartEntry != null) {
            cartEntry.setStatus(status);
            cartEntryRepository.save(cartEntry);
        }
        return modelMapper.map(cartEntry, CartEntryDto.class);
    }

}
