package com.ust.pos;

import com.ust.pos.dto.UserDto;
import com.ust.pos.model.User;
import com.ust.pos.model.UserRepository;
import com.ust.pos.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    /* ===================== FIND BY USERNAME ===================== */

    @Test
    void findByUserNameTest() {
        User user = new User();
        user.setUsername("admin@test.com");

        UserDto userDto = new UserDto();
        userDto.setUsername("admin@test.com");

        Mockito.when(userRepository.findByUsername("admin@test.com")).thenReturn(user);
        Mockito.when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto response = userService.findByUserName("admin@test.com");

        Assertions.assertEquals("admin@test.com", response.getUsername());
    }

    /* ===================== SAVE ===================== */

    @Test
    void saveTest() {
        UserDto userDto = new UserDto();
        userDto.setUsername("admin@test.com");
        userDto.setPassword("123456");

        Mockito.when(userRepository.findByUsername("admin@test.com")).thenReturn(null);

        User user = new User();
        Mockito.when(modelMapper.map(userDto, User.class)).thenReturn(user);
        Mockito.when(passwordEncoder.encode("123456")).thenReturn("hashedpassword");

        UserDto response = userService.save(userDto);

        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void saveTestFailure() {
        UserDto userDto = new UserDto();
        userDto.setUsername("admin@test.com");

        User existingUser = new User();
        Mockito.when(userRepository.findByUsername("admin@test.com")).thenReturn(existingUser);

        UserDto response = userService.save(userDto);

        Assertions.assertFalse(response.isSuccess());
    }

    /* ===================== UPDATE ===================== */

    @Test
    void updateTest() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("admin@test.com");

        User user = new User();
        user.setUsername("admin@test.com");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto response = userService.update(userDto);

        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void updateTestDuplicateUsername() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("new@test.com");

        User user = new User();
        user.setUsername("old@test.com");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByUsername("new@test.com")).thenReturn(true);

        UserDto response = userService.update(userDto);

        Assertions.assertNotNull(response.getMessage());
    }

    @Test
    void updateTestNotFound() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserDto response = userService.update(userDto);

        Assertions.assertTrue(response.isSuccess());
    }

    /* ===================== DELETE ===================== */

    @Test
    void deleteTest() {
        boolean response = userService.delete("admin@test.com");
        Assertions.assertTrue(response);
    }

    /* ===================== FIND ALL ===================== */

    @Test
    void findAllTest() {
        User user = new User();
        UserDto userDto = new UserDto();

        List<User> users = List.of(user);
        List<UserDto> userDtos = List.of(userDto);

        Page<User> userPage = new PageImpl<>(users, PageRequest.of(0, 2), users.size());

        Pageable pageable = PageRequest.of(0, 50, Sort.by(new ArrayList<>()));
        Mockito.when(userRepository.findAll(pageable)).thenReturn(userPage);
        Mockito.when(modelMapper.map(Mockito.eq(users), Mockito.any(java.lang.reflect.Type.class))).thenReturn(userDtos);
        List<UserDto> response = userService.findAll(pageable);

        Assertions.assertEquals(1, response.size());
    }

    /* ===================== UPDATE STATUS ===================== */

    @Test
    void updateStatusTest() {
        User user = new User();
        user.setUsername("admin@test.com");

        UserDto userDto = new UserDto();
        userDto.setStatus(true);

        Mockito.when(userRepository.findByUsername("admin@test.com")).thenReturn(user);
        Mockito.when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto response = userService.updateStatus("admin@test.com", true);

        Assertions.assertTrue(response.isStatus());
    }

    /* ===================== FIND ALL ACTIVE ===================== */

    @Test
    void findAllActiveTest() {
        User user = new User();
        UserDto userDto = new UserDto();

        List<User> users = List.of(user);
        List<UserDto> userDtos = List.of(userDto);

        Mockito.when(userRepository.findByStatus(true)).thenReturn(users);
        Mockito.when(modelMapper.map(Mockito.eq(users), Mockito.any(java.lang.reflect.Type.class))).thenReturn(userDtos);

        List<UserDto> response = userService.findAllActive();

        Assertions.assertEquals(1, response.size());
    }
}