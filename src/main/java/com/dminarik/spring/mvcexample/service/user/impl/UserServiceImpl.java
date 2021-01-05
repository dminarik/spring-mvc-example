package com.dminarik.spring.mvcexample.service.user.impl;

import com.dminarik.spring.mvcexample.data.UserData;
import com.dminarik.spring.mvcexample.service.user.UserAlreadyExistsException;
import com.dminarik.spring.mvcexample.service.user.UserNotFoundException;
import com.dminarik.spring.mvcexample.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final Set<UserData> users = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void createUser(UserData user) {
        boolean userAdded = users.add(user);
        if (!userAdded) {
            throw new UserAlreadyExistsException();
        }
    }

    @Override
    public UserData findUser(String username) {
        return users.stream()
                .filter(userData -> userData.getUsername().equals(username))
                .findFirst()
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void deleteUser(String username) {
        boolean userRemoved = users.removeIf(userData -> userData.getUsername().equals(username));

        if (!userRemoved) {
            throw new UserNotFoundException();
        }
    }

}
