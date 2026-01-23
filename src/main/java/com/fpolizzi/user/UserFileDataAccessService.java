package com.fpolizzi.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Created by fpolizzi on 06.01.26
 */

public class UserFileDataAccessService implements UserDao {

    @Override
    public List<User> getUsers() {
        File file = new File("src/main/java/com/fpolizzi/users.csv");

        List<User> users = new ArrayList<>();

        // read example
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String[] split = scanner.nextLine().split(",");
                users.add(new User(UUID.fromString(split[0]), split[1], split[2]));
            }
            return users;
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e.getMessage());
            return users;
        }
    }
}