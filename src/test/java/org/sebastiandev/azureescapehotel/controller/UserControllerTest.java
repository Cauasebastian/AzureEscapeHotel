package org.sebastiandev.azureescapehotel.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sebastiandev.azureescapehotel.controller.UserController;
import org.sebastiandev.azureescapehotel.model.User;
import org.sebastiandev.azureescapehotel.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.sebastiandev.azureescapehotel.exception.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsers_AdminRole_Success() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.getUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getUsers();

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService, times(1)).getUsers();
    }

    @Test
    void testGetUserByEmail_ValidEmail_Success() {
        String email = "test@example.com";
        User user = new User();
        when(userService.getUser(email)).thenReturn(user);

        ResponseEntity<?> response = userController.getUserByEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).getUser(email);
    }

    @Test
    void testGetUserByEmail_InvalidEmail_ThrowsException() {
        String email = "invalid@example.com";
        when(userService.getUser(email)).thenThrow(new UsernameNotFoundException("User not found"));

        ResponseEntity<?> response = userController.getUserByEmail(email);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).getUser(email);
    }
}
