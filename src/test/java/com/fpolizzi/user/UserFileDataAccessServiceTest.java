package com.fpolizzi.user;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserFileDataAccessServiceTest {

    private final UserFileDataAccessService underTest = new UserFileDataAccessService();

    @Test
    void getUsers_shouldReturn6Users() {
        List<User> result = underTest.getUsers();

        assertThat(result).hasSize(6);
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
    void getUsers_shouldContainUserWithFirstKnownId() {
        UUID expectedFirstId = UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3");

        List<User> result = underTest.getUsers();

        assertThat(result)
                .extracting(User::getId)
                .contains(expectedFirstId);
    }
}
