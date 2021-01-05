package com.dminarik.spring.mvcexample.service.user;

import com.dminarik.spring.mvcexample.data.UserData;

public interface UserService {

    void createUser(UserData user);

    UserData findUser(String username);

    void deleteUser(String username);

}
