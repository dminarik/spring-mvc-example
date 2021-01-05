package com.dminarik.spring.mvcexample.data;

import java.util.Objects;

public class UserData {

    private String username;

    public UserData() {
    }

    public UserData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return Objects.equals(username, userData.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
