package com.skillmatch.auth.service;

import com.skillmatch.auth.dto.request.LoginRequest;
import com.skillmatch.auth.dto.request.RegisterRequest;
import com.skillmatch.auth.dto.response.AuthResponse;
import com.skillmatch.auth.dto.response.CurrentUserResponse;
import com.skillmatch.auth.entity.User;
import com.skillmatch.auth.enums.UserRole;
import com.skillmatch.auth.exception.DuplicateEmailException;
import com.skillmatch.auth.exception.InvalidCredentialsException;
import com.skillmatch.auth.repository.UserRepository;
import com.skillmatch.auth.security.CustomUserDetails;
import com.skillmatch.auth.security.JwtService;
import com.skillmatch.auth.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("Sara");
        user.setLastName("Ali");
        user.setEmail("sara@example.com");
        user.setPassword("hashed-password");
        user.setRole(UserRole.JOB_SEEKER);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void register_shouldReturnAuthResponse_whenRequestIsValid() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Sara");
        request.setLastName("Ali");
        request.setEmail("Sara@example.com");
        request.setPassword("StrongPass123");
        request.setRole(UserRole.JOB_SEEKER);

        when(userRepository.existsByEmail("sara@example.com")).thenReturn(false);
        when(passwordEncoder.encode("StrongPass123")).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn("mock-jwt");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("mock-jwt", response.getToken());
        assertEquals("sara@example.com", response.getEmail());
        assertEquals(UserRole.JOB_SEEKER, response.getRole());

        verify(userRepository).existsByEmail("sara@example.com");
        verify(passwordEncoder).encode("StrongPass123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowDuplicateEmailException_whenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Sara");
        request.setLastName("Ali");
        request.setEmail("sara@example.com");
        request.setPassword("StrongPass123");
        request.setRole(UserRole.JOB_SEEKER);

        when(userRepository.existsByEmail("sara@example.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> authService.register(request));

        verify(userRepository).existsByEmail("sara@example.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_shouldReturnAuthResponse_whenCredentialsAreValid() {
        LoginRequest request = new LoginRequest();
        request.setEmail("Sara@example.com");
        request.setPassword("StrongPass123");

        when(userRepository.findByEmail("sara@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any())).thenReturn("mock-jwt");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mock-jwt", response.getToken());
        assertEquals("sara@example.com", response.getEmail());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail("sara@example.com");
    }

    @Test
    void login_shouldThrowInvalidCredentialsException_whenAuthenticationFails() {
        LoginRequest request = new LoginRequest();
        request.setEmail("sara@example.com");
        request.setPassword("wrong-password");

        doThrow(new RuntimeException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void getCurrentUser_shouldReturnCurrentUserResponse_whenAuthenticated() {
        SecurityContext securityContext = mock(SecurityContext.class);
        var authentication = mock(org.springframework.security.core.Authentication.class);
        var principal = new CustomUserDetails(user);

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        CurrentUserResponse response = authService.getCurrentUser();

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("sara@example.com", response.getEmail());
    }
}