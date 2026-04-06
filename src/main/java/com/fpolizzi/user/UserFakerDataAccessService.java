package com.fpolizzi.user;

import java.util.List;

/**
 * Created by fpolizzi on 06.04.26
 */
public class UserFakerDataAccessService implements UserDao {

    @Override
    public List<User> getUsers() {
        return List.of();
    }
}
