package com.fpolizzi.user;

import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by fpolizzi on 06.04.26
 */
public class UserFakerDataAccessService implements UserDao {

    @Override
    public List<User> getUsers() {

        List<User> users = new ArrayList<>();

        Faker faker = new Faker();

        for (int i = 0; i < 20; i++) {

            User user = new User(
                    UUID.randomUUID(),
                    faker.name().firstName(),
                    faker.name().lastName()
            );

            users.add(user);
        }

        return users;
    }
}
