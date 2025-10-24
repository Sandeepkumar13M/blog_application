package com.blog_application.restController;

import com.blog_application.dto.responseDto.UserResponseDto;
import com.blog_application.model.User;
import com.blog_application.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserRestController(CustomUserDetailsService userDetailsService,
                              AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user,
                                          @RequestParam("confirmPassword") String confirmPassword) {

        Map<String, String> response = new HashMap<>();

        if (!user.getPassword().equals(confirmPassword)) {
            response.put("error", "Passwords do not match");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (userDetailsService.userExists(user.getEmail())) {
            response.put("error", "Email already registered");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User savedUser = userDetailsService.saveUser(user);
        UserResponseDto userDto = convertToDto(savedUser);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Return user details on successful authentication
            User user = userDetailsService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Authentication successful");
            response.put("user", convertToDto(user));

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Authentication failed: " + e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getUserProfile(Authentication authentication) {
        String email = authentication.getName();
        User user = userDetailsService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new ResponseEntity<>(convertToDto(user), HttpStatus.OK);
    }

    private UserResponseDto convertToDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}