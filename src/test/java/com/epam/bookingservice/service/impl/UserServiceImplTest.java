package com.epam.bookingservice.service.impl;

import com.epam.bookingservice.domain.Role;
import com.epam.bookingservice.domain.User;
import com.epam.bookingservice.entity.RoleEntity;
import com.epam.bookingservice.entity.UserEntity;
import com.epam.bookingservice.mapper.Mapper;
import com.epam.bookingservice.respository.UserRepository;
import com.epam.bookingservice.service.exception.UserAlreadyExistsException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encoded_password";
    private static final String USER_EMAIL = "user@email.com";

    private static final User USER = initUser();
    private static final UserEntity USER_ENTITY = initUserEntity();

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Mapper<UserEntity, User> userMapper;

    private UserServiceImpl userService;

    @Before
    public void injectMocks() {
        userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper);
    }

    @After
    public void resetMocks() {
        reset(userRepository, passwordEncoder, userMapper);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void userShouldRegister() {
        when(userMapper.mapEntityToDomain(eq(USER_ENTITY))).thenReturn(USER);
        when(userMapper.mapDomainToEntity(eq(USER))).thenReturn(USER_ENTITY);
        when(passwordEncoder.encode(eq(PASSWORD)))
                .thenReturn(ENCODED_PASSWORD);
        when(userRepository.findByEmail(eq(USER_EMAIL)))
                .thenReturn(Optional.empty());
        when(userRepository.save(eq(USER_ENTITY))).thenReturn(USER_ENTITY);

        User userToBeRegistered = User.builder()
                .password(PASSWORD)
                .email(USER_EMAIL)
                .build();

        User registeredUser = userService.register(userToBeRegistered);

        assertEquals(USER, registeredUser);
        verify(userRepository).findByEmail(eq(USER_EMAIL));
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    public void userShouldNotRegisterAsEmailIsAlreadyUsed() {
        when(userRepository.findByEmail(eq(USER_EMAIL))).thenReturn(Optional.of(USER_ENTITY));

        expectedException.expect(UserAlreadyExistsException.class);
        userService.register(USER);
    }

    private static User initUser() {
        return User.builder()
                .email(USER_EMAIL)
                .password(ENCODED_PASSWORD)
                .role(Role.CLIENT)
                .build();
    }

    private static UserEntity initUserEntity() {
        return UserEntity.builder()
                .email(USER_EMAIL)
                .password(ENCODED_PASSWORD)
                .role(RoleEntity.CLIENT)
                .build();
    }
}