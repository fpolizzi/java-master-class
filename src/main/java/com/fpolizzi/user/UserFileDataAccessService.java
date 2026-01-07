package com.fpolizzi.user;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

/**
 * Created by fpolizzi on 06.01.26
 */

public class UserFileDataAccessService implements UserDao {

    @Override
    public User[] getUsers() {
        File file = new File("src/main/java/com/fpolizzi/users.csv");

        User[] users = new User[6];

        // read example
        try (Scanner scanner = new Scanner(file)) {
            int index = 0;
            while (scanner.hasNext()) {
                String[] split = scanner.nextLine().split(",");
                users[index] = new User(UUID.fromString(split[0]), split[1], split[2]);
                index++;
            }
            return users;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}