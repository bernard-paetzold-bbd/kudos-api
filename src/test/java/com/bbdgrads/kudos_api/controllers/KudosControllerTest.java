package com.bbdgrads.kudos_api.controllers;

import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.security.JwtAuthFilter;
import com.bbdgrads.kudos_api.service.JwtService;
import com.bbdgrads.kudos_api.service.KudoService;
import com.bbdgrads.kudos_api.service.KudoServiceImpl;
import com.bbdgrads.kudos_api.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class KudosControllerTest {

    @Mock
    private KudoServiceImpl kudoServiceImpl;

    @Mock
    private UserServiceImpl userServiceImpl;

    @Mock
    private User user;

    @Mock
    private JwtService jwtService;

    @Mock
    private JwtAuthFilter jwtAuthFilter;


    @InjectMocks
    KudoController kudoController;

    private final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

    private User user1;
    private User user2;
    private Kudo kudo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new User("JohnDoe", "googleID", false);
        user2 = new User("JaneDoe", "googleID2", false);
        kudo = new Kudo("message", user1, user2);
        mockHttpServletRequest.addHeader("Bearer ", user1);
    }


//    @Test
//    void createNewKudoIsSuccessful() throws AccessDeniedException {
//        Mockito.when(jwtService.getUserFromHeader(mockHttpServletRequest.getHeader("Bearer "))).thenReturn(user1);
//        ResponseEntity<Kudo> responseEntity = new ResponseEntity<>
//                (HttpStatus.CREATED);
//        assertEquals(responseEntity,
//                kudoController.createNewKudo(user1.getUsername(), kudo.getMessage(), mockHttpServletRequest));
//    }

    @Test
    void getKudosOfUsernameIsSuccessful() {
        ResponseEntity<?> responseEntity = new ResponseEntity<>
                (new ArrayList<>(), HttpStatus.OK);
        Mockito.when(userServiceImpl.findByUsername(user1.getUsername())).thenReturn(Optional.ofNullable(user1));
        assertEquals(responseEntity,
                kudoController.getKudosOfUsername(user1.getUsername()));
    }

//    @Test
//    public void getKudoByIdIsSuccessful() {
//        ResponseEntity<?> responseEntity = new ResponseEntity<>
//                (kudo, HttpStatus.OK);
//        Mockito.when(kudoServiceImpl.findByKudoId(kudo.getKudoId())).thenReturn(Optional.ofNullable(kudo));
//        assertEquals(responseEntity,
//                kudoController.getKudoById(kudo.getKudoId()));
//    }

    @Test
    void getKudoByIdNotFound() {
        ResponseEntity<?> responseEntity = new ResponseEntity<>
                ("Kudo not found.",HttpStatus.NOT_FOUND);
        Mockito.when(kudoServiceImpl.findByKudoId(kudo.getKudoId())).thenReturn(Optional.ofNullable(null));
        assertEquals(responseEntity,
                kudoController.getKudoById(kudo.getKudoId()));
    }

}
