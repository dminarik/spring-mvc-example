package com.dminarik.spring.mvcexample.controller;

import com.dminarik.spring.mvcexample.data.UserData;
import com.dminarik.spring.mvcexample.service.user.UserAlreadyExistsException;
import com.dminarik.spring.mvcexample.service.user.UserNotFoundException;
import com.dminarik.spring.mvcexample.service.user.UserService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserControllerRestAssuredTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService mockUserService;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void testCreateUser() throws Exception {
        given()
                .body("{ \"username\" : \"test\" }")
                .header("Content-Type", "application/json")
                .when()
                .post("/user")
                .then()
                .status(HttpStatus.CREATED);

        doThrow(UserAlreadyExistsException.class)
                .when(mockUserService).createUser(any());

        given()
                .body("{ \"username\" : \"test\" }")
                .contentType(ContentType.JSON)
                .when()
                .post("/user")
                .then()
                .status(HttpStatus.CONFLICT);
    }

    @Test
    public void testGetUser() throws Exception {
        UserData testUser = new UserData();
        testUser.setUsername("test");
        when(mockUserService.findUser("test"))
                .thenReturn(testUser);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/user/test")
                .then()
                .body("username", equalTo("test"))
                .status(HttpStatus.OK);

        doThrow(UserNotFoundException.class)
                .when(mockUserService).findUser("unknown");

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/user/unknown")
                .then()
                .status(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteUser() throws Exception {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/user/test")
                .then()
                .status(HttpStatus.OK);

        doThrow(UserNotFoundException.class)
                .when(mockUserService).deleteUser("unknown");

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/user/unknown")
                .then()
                .status(HttpStatus.NOT_FOUND);
    }

}
