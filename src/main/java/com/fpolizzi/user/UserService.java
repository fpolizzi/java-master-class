package com.fpolizzi.user;

import java.util.UUID;

/**
 * Created by fpolizzi on 26.12.25
 */

public class UserService {
    private final UserDao userDao = new UserFileDataAccessService();

    public User[] getUsers() {
        return userDao.getUsers();
    }

    public User getUserById(UUID id) {
        for (User user : getUsers()) {
            if (user != null && user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }
}