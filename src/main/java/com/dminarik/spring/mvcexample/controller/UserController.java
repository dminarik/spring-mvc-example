package com.dminarik.spring.mvcexample.controller;

import com.dminarik.spring.mvcexample.data.UserData;
import com.dminarik.spring.mvcexample.exceptions.ResourceAlreadyExistsException;
import com.dminarik.spring.mvcexample.exceptions.ResourceNotFoundException;
import com.dminarik.spring.mvcexample.service.user.UserAlreadyExistsException;
import com.dminarik.spring.mvcexample.service.user.UserNotFoundException;
import com.dminarik.spring.mvcexample.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = {"/user"})
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserData user) {
        try {
            userService.createUser(user);
        } catch (UserAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException();
        }
    }

    @GetMapping(path = {"/{username}"})
    public UserData findUser(@PathVariable String username) {
        try {
            return userService.findUser(username);
        } catch (UserNotFoundException e) {
            throw new ResourceNotFoundException();
        }
    }

    @DeleteMapping(path = {"/{username}"})
    public void deleteUser(@PathVariable String username) {
        try {
            userService.deleteUser(username);
        } catch (UserNotFoundException e) {
            throw new ResourceNotFoundException();
        }
    }

}
