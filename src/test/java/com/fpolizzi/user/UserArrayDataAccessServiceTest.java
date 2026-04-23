package com.fpolizzi.user;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserArrayDataAccessServiceTest {

    private final UserArrayDataAccessService underTest = new UserArrayDataAccessService();

    @Test
    void getUsers_shouldReturnTwoUsers() {
        List<User> result = underTest.getUsers();

        assertThat(result).hasSize(2);
    }

    @Test
    void getUsers_shouldContainJamesMeyers() {
        UUID expectedId = UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3");

        List<User> result = underTest.getUsers();

        assertThat(result)
                .extracting(User::getId)
                .contains(expectedId);
    }

    @Test
    void getUsers_shouldContainJamilaDialo() {
        UUID expectedId = UUID.fromString("b10d126a-3608-4980-9f9c-aa179f5cebc3");

        List<User> result = underTest.getUsers();

        assertThat(result)
                .extracting(User::getId)
                .contains(expectedId);
    }

    @Test
    void getUsers_shouldReturnUsersWithNonNullIds() {
        List<User> result = underTest.getUsers();

        assertThat(result).allSatisfy(user ->
                assertThat(user.getId()).isNotNull()
        );
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
    void getUsers_shouldReturnSameDataOnMultipleCalls() {
        List<User> firstCall = underTest.getUsers();
        List<User> secondCall = underTest.getUsers();

        assertThat(firstCall).isEqualTo(secondCall);
    }
}
