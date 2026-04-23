package com.fpolizzi.user;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserFakerDataAccessServiceTest {

    private final UserFakerDataAccessService underTest = new UserFakerDataAccessService();

    @Test
    void getUsers_shouldReturn20Users() {
        List<User> result = underTest.getUsers();

        assertThat(result).hasSize(20);
    }

    @Test
    void getUsers_shouldReturnUsersWithNonNullIds() {
        List<User> result = underTest.getUsers();

        assertThat(result).allSatisfy(user ->
                assertThat(user.getId()).isNotNull()
        );
    }

    @Test
    void getUsers_shouldReturnUsersWithDistinctIds() {
        List<User> result = underTest.getUsers();

        assertThat(result)
                .extracting(User::getId)
                .doesNotHaveDuplicates();
    }

    @Test
    void getUsers_shouldReturnUsersWithNonBlankNames() {
        List<User> result = underTest.getUsers();

        assertThat(result).allSatisfy(user -> {
            assertThat(user.getFirstName()).isNotBlank();
            assertThat(user.getLastName()).isNotBlank();
        });
    }

    @Test
    void getUsers_shouldReturnDifferentUsersOnEachCall() {
        List<User> firstCall = underTest.getUsers();
        List<User> secondCall = underTest.getUsers();

        assertThat(firstCall).isNotEqualTo(secondCall);
    }
}
