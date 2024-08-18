package org.sebastiandev.azureescapehotel.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.sebastiandev.azureescapehotel.config.security.user.HotelUserDetails;
import org.sebastiandev.azureescapehotel.exception.UserAlreadyExistsException;
import org.sebastiandev.azureescapehotel.model.User;
import org.sebastiandev.azureescapehotel.request.LoginRequest;
import org.sebastiandev.azureescapehotel.service.IUserService;
import org.sebastiandev.azureescapehotel.config.security.jwt.JwtUtils;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IUserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");

        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/auth/register-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Registration successful!"));

        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    public void testRegisterUser_Conflict() throws Exception {
        User user = new User();
        user.setEmail("existing@example.com");
        user.setPassword("password123");

        when(userService.registerUser(any(User.class)))
                .thenThrow(new UserAlreadyExistsException("existing@example.com already exists"));

        mockMvc.perform(post("/auth/register-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"existing@example.com\", \"password\": \"password123\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("existing@example.com already exists"));

        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    public void testAuthenticateUser_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        Authentication authentication = mock(Authentication.class);
        doReturn(authentication).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        doReturn("mockJwtToken").when(jwtUtils).generateJwtTokenForUser(any(Authentication.class));

        HotelUserDetails userDetails = mock(HotelUserDetails.class);
        doReturn(userDetails).when(authentication).getPrincipal();

        // Corrigindo o tipo de retorno para Long
        doReturn(1L).when(userDetails).getId(); // Use um valor Long, como 1L
        doReturn("test@example.com").when(userDetails).getEmail();

        // Criando a lista de GrantedAuthority
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        doReturn(authorities).when(userDetails).getAuthorities();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L)) // Certifique-se de que o tipo esperado aqui também seja Long
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.token").value("mockJwtToken"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateJwtTokenForUser(any(Authentication.class));
    }
}