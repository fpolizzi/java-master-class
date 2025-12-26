package com.polizzi.user;

import java.util.UUID;

/**
 * Created by fpolizzi on 26.12.25
 */
public class UserDao {

    private static final User[] users;

    static {
        users = new User[]{
                new User(UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3"), "James", "Meyers"),
                new User(UUID.fromString("2ea85178-fada-4279-9d5e-eea627049fa2"), "Jamila", "Dialo"),
                new User(UUID.fromString("576590ff-57a1-4df3-8430-79980eb42343"), "Alex", "Gore"),
                new User(UUID.fromString("9d818235-ce3b-40e8-b74a-3674985c6bcd"), "Curt", "Miller"),
                new User(UUID.fromString("87cb62d9-d262-4174-b1b2-957f9e2a1f40"), "Maria", "Beavers"),
                new User(UUID.fromString("48f685ee-e174-4055-b644-68b93cd5116f"), "Jeff", "Thomson"),
        };
    }

    public User[] getUsers() {
        return users;
    }
}
