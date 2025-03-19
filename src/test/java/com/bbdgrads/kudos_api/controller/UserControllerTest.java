package com.bbdgrads.kudos_api.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bbdgrads.kudos_api.controllers.UserController;
import com.bbdgrads.kudos_api.model.Log;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.repository.UserRepository;
import com.bbdgrads.kudos_api.service.AuthService;
import com.bbdgrads.kudos_api.service.LogService;
import com.bbdgrads.kudos_api.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties") 
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private LogService logService;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setGoogleToken("testGoogleToken");

    }

    @Test
    public void testCreateUser() throws Exception {

        when(userService.save(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/user/create")
                .param("name", "john doe")
                .param("googleToken", "gtoken12345"))
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.username").value("john doe"))
                .andExpect(
                        jsonPath("$.googleToken").value("gtoken12345"));
        // check user?
        // Optional<User> entity = userRepository.findById(0L);
        // assertTrue(entity.isPresent());
    }

    // @Test
    // public void testGetUser() throws Exception {

    //     when(userService.findByUserId(any(Long.class))).thenReturn(testUser);

    //     mockMvc.perform(get("/user/1"))
    //             .andExpect(status().isOk());

    // }

    // @Test
    // public void testDeleteUser() throws Exception {

    //     doNothing().when(userService).delete(any(Long.class));

    //     mockMvc.perform(delete("/user/1"))
    //             .andExpect(status().isOk());

    // }

}
