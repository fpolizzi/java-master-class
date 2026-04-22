package com.fpolizzi.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceMockitoTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService underTest;

    @Test
    void getUsers_shouldReturnAllUsers() {
        List<User> users = List.of(
                new User(UUID.randomUUID(), "Alice", "Smith"),
                new User(UUID.randomUUID(), "Bob", "Jones")
        );
        when(userDao.getUsers()).thenReturn(users);

        List<User> result = underTest.getUsers();

        assertThat(result).containsExactlyInAnyOrder(users.toArray(new User[0]));
    }

    @Test
    void getUsers_shouldReturnEmptyList_whenNoUsers() {
        when(userDao.getUsers()).thenReturn(Collections.emptyList());

        List<User> result = underTest.getUsers();

        assertThat(result).isEmpty();
    }

    @Test
    void getUserById_shouldReturnUser_whenIdMatches() {
        UUID id = UUID.randomUUID();
        User expected = new User(id, "Alice", "Smith");
        when(userDao.getUsers()).thenReturn(List.of(expected, new User(UUID.randomUUID(), "Bob", "Jones")));

        Optional<User> result = underTest.getUserById(id);

        assertThat(result).contains(expected);
    }

    @Test
    void getUserById_shouldReturnEmpty_whenIdNotFound() {
        when(userDao.getUsers()).thenReturn(List.of(new User(UUID.randomUUID(), "Alice", "Smith")));

        Optional<User> result = underTest.getUserById(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    @Test
    void getUserById_shouldReturnEmpty_whenListIsEmpty() {
        when(userDao.getUsers()).thenReturn(Collections.emptyList());

        Optional<User> result = underTest.getUserById(UUID.randomUUID());

        assertThat(result).isEmpty();
    }
}
