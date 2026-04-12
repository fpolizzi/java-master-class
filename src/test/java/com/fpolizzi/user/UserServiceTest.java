package com.fpolizzi.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {

    private List<User> stubbedUsers;
    private UserService underTest;

    @BeforeEach
    void setUp() {
        UserDao userDao = () -> stubbedUsers;
        underTest = new UserService(userDao);
    }

    @Test
    void getUsers_shouldReturnAllUsers() {
        stubbedUsers = List.of(
                new User(UUID.randomUUID(), "Alice", "Smith"),
                new User(UUID.randomUUID(), "Bob", "Jones")
        );

        List<User> result = underTest.getUsers();

        assertThat(result).hasSize(2);
    }

    @Test
    void getUsers_shouldReturnEmptyList_whenNoUsers() {
        stubbedUsers = Collections.emptyList();

        List<User> result = underTest.getUsers();

        assertThat(result).isEmpty();
    }

    @Test
    void getUserById_shouldReturnUser_whenIdMatches() {
        UUID id = UUID.randomUUID();
        User expected = new User(id, "Alice", "Smith");
        stubbedUsers = List.of(expected, new User(UUID.randomUUID(),
                "Bob", "Jones"));

        User result = underTest.getUserById(id);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getUserById_shouldReturnNull_whenIdNotFound() {
        stubbedUsers = List.of(new User(UUID.randomUUID(),
                "Alice", "Smith"));

        User result = underTest.getUserById(UUID.randomUUID());

        assertThat(result).isNull();
    }

    @Test
    void getUserById_shouldReturnNull_whenListIsEmpty() {
        stubbedUsers = Collections.emptyList();

        User result = underTest.getUserById(UUID.randomUUID());

        assertThat(result).isNull();
    }
}
