package com.ust.pos.user.service.impl;

import com.ust.pos.dto.UserDto;
import com.ust.pos.dto.WsDto;
import com.ust.pos.model.User;
import com.ust.pos.model.UserRepository;
import com.ust.pos.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto findByUserName(String username) {
        return modelMapper.map(userRepository.findByUsername(username), UserDto.class);
    }

    @Override
    public UserDto save(UserDto userDto) {
        String username = userDto.getUsername();
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            userDto.setMessage("User with username/email - " + userDto.getUsername() + " already exists");
            userDto.setSuccess(false);
            return userDto;
        }
        // Temporary manual mapping test inside UserServiceImpl.java -> save()
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setPhoneNo(userDto.getPhoneNo());
        user.setRoles(userDto.getRoles());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
        return userDto;
    }

    @Override
    public UserDto update(UserDto userDto) {
        Optional<User> userOptional = userRepository.findById(userDto.getId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.getUsername().equalsIgnoreCase(userDto.getUsername()) && userRepository.existsByUsername(userDto.getUsername())) {
                userDto.setMessage("User with username/email - " + userDto.getUsername() + " already exists");
                return userDto;
            }
            modelMapper.map(userDto, user);
            userRepository.save(user);
        }
        return userDto;
    }

    @Override
    public boolean delete(String username) {
        userRepository.deleteByUsername(username);
        return true;
    }

    @Override
    public WsDto<UserDto> findAll(Pageable pageable) {
        Type listType = new TypeToken<List<UserDto>>() {
        }.getType();
        Page<User> userPage = userRepository.findAll(pageable);
        WsDto<UserDto> userWsDto = new WsDto<>();
        userWsDto.setDtoList(modelMapper.map(userPage.getContent(), listType));
        userWsDto.setTotalRecords(userPage.getTotalElements());
        userWsDto.setTotalPages(userPage.getTotalPages());
        userWsDto.setSizePerPage(pageable.getPageSize());
        userWsDto.setPage(pageable.getPageNumber());

        return userWsDto;
    }

    @Override
    public UserDto updateStatus(String username, boolean status) {
        User user = userRepository.findByUsername(username);
        user.setStatus(status);
        userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> findAllActive() {
        Type listType = new TypeToken<List<UserDto>>() {
        }.getType();
        return modelMapper.map(userRepository.findByStatus(true), listType);
    }
}
