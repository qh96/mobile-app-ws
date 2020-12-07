package com.appsdeveloperblog.app.ws.service.impl;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService; // 如果autowired, userRepo就是真的了，我们需要mock的

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    UserRepository userRepository; // fake就可以了

    String userId = "218390";
    String encrytedPassword = "12urdiaj";

    UserEntity userEntity;

    @BeforeEach
    void Setup(){
        MockitoAnnotations.initMocks(this);
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Hao");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(encrytedPassword);
    }

    @Test
    void getUser() {
        // 我们要测试这个函数里面的逻辑，所以userRepo.find*** 就不需要测试，他不是这个函数的逻辑
        // 如果要测试所以userRepo.里面的函数，就写userRepo的测试用例
        // 当我们使用userRepo的时候，我们默认他已经是测试过的



        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = userService.getUser("test@test.comhdiasjd912u93");
        // 我们其实要测试的是他能不能返回一个userDto这个object, 其实就是测试BeanUtils
        // Surgey : Regarding the BeanUtils.copy() being the essence of the getUser function.
        assertNotNull(userDto);
        assertEquals("Hao", userDto.getFirstName());

    }

    @Test
    final void testGetUser_UsernameNotFoundException(){
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class,
                () -> {
                    userService.getUser("test@test.com");
                }
                );

    }

    @Test
    final void testCreateUser(){
        /*
        * Instead of mocking so many objects while writing unit test cases for createUser
        *  in userserviceimplTest , my suggestion would be to divide the createUser
        * method into chunks and write unit test for each one.
        * so that its easy to test small chunks.
        * */

        // 这些when就是需要mock的
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn("1u29e8dja");
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encrytedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        // Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDto.class));
        // amazonSES 是一个 Mock 的， 以前在Impl里是Autowired

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setType("shipping");
        addressDTO.setCity("Vancouver");
        addressDTO.setCountry("Canada");
        addressDTO.setPostalCode("ABC123");


        List<AddressDTO> addressDTOList = new ArrayList<>();
        addressDTOList.add(addressDTO);

        UserDto userDto = new UserDto();
        userDto.setAddresses(addressDTOList);
        UserDto storedUserDetails = userService.createUser(userDto);

        assertNotNull(storedUserDetails);
        assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());

    }

}