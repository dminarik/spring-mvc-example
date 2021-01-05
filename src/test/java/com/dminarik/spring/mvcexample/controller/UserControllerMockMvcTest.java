package com.dminarik.spring.mvcexample.controller;

import com.dminarik.spring.mvcexample.data.UserData;
import com.dminarik.spring.mvcexample.service.user.UserAlreadyExistsException;
import com.dminarik.spring.mvcexample.service.user.UserNotFoundException;
import com.dminarik.spring.mvcexample.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockUserService;

    @Test
    public void testCreateUser() throws Exception {
        mockMvc.perform(post("/user")
                .header("Content-Type", "application/json")
                .content("{ \"username\" : \"test\" }"))
                .andExpect(status().isCreated());

        doThrow(UserAlreadyExistsException.class)
                .when(mockUserService).createUser(any());

        mockMvc.perform(post("/user")
                .header("Content-Type", "application/json")
                .content("{ \"username\" : \"test\" }"))
                .andExpect(status().isConflict());
    }

    @Test
    public void testGetUser() throws Exception {
        UserData testUser = new UserData();
        testUser.setUsername("test");
        when(mockUserService.findUser("test"))
                .thenReturn(testUser);

        mockMvc.perform(get("/user/test")
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test"));

        doThrow(UserNotFoundException.class)
                .when(mockUserService).findUser("unknown");

        mockMvc.perform(get("/user/unknown")
                .header("Content-Type", "application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/user/test")
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk());

        doThrow(UserNotFoundException.class)
                .when(mockUserService).deleteUser("unknown");

        mockMvc.perform(delete("/user/unknown")
                .header("Content-Type", "application/json"))
                .andExpect(status().isNotFound());
    }

}
