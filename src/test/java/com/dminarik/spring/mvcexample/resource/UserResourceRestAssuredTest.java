package com.dminarik.spring.mvcexample.resource;

import com.dminarik.spring.mvcexample.data.UserData;
import com.dminarik.spring.mvcexample.service.user.UserAlreadyExistsException;
import com.dminarik.spring.mvcexample.service.user.UserNotFoundException;
import com.dminarik.spring.mvcexample.service.user.UserService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResourceRestAssuredTest {

    @LocalServerPort
    private int port;

    @MockBean
    private UserService mockUserService;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    public void testCreateUser() {
        given()
                .body("{ \"username\" : \"test\" }")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/user")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());

        doThrow(UserAlreadyExistsException.class)
                .when(mockUserService).createUser(any());

        given()
                .body("{ \"username\" : \"test\" }")
                .contentType(ContentType.JSON)
                .when()
                .post("/user")
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode());
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
                .statusCode(Response.Status.OK.getStatusCode());

        doThrow(UserNotFoundException.class)
                .when(mockUserService).findUser("unknown");

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/user/unknown")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testDeleteUser() throws Exception {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/user/test")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

        doThrow(UserNotFoundException.class)
                .when(mockUserService).deleteUser("unknown");

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/user/unknown")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

}
