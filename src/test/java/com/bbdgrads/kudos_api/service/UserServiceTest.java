package com.bbdgrads.kudos_api.service;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setUsername("john doe");
        user.setGoogleToken("johnGoogle1234");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testfindByUserId() {

        when(userRepository.findByUserId(any(Long.class))).thenReturn(Optional.of(user));

        var foundUser = userService.findByUserId(1L);

        assertNotNull(foundUser);
        assertFalse(foundUser.isEmpty());

        assertEquals(user.getUsername(), foundUser.get().getUsername());

        verify(userRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void testSaveUser() {

        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.save(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteUser() {

        when(userRepository.findByUserId(any(Long.class))).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(any(User.class));

        userService.delete(1L);

        verify(userRepository, times(1)).delete(user);
    }

}
