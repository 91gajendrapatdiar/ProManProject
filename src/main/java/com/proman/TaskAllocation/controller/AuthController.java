package com.proman.TaskAllocation.controller;

import com.proman.TaskAllocation.config.JwtProvider;
import com.proman.TaskAllocation.model.User;
import com.proman.TaskAllocation.repository.SubscriptionService;
import com.proman.TaskAllocation.repository.UserRepository;
import com.proman.TaskAllocation.request.LoginRequest;
import com.proman.TaskAllocation.response.AuthResponse;
import com.proman.TaskAllocation.services.CustomeUserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomeUserDetailsImpl customUserDetails;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) {
        // Find user by email
        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            // Throw an exception with an HTTP status code
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists with another account");
        }

        // Create a new User object and set its properties
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setFullName(user.getFullName());

        // Save the user to the repository
        User savedUser = userRepository.save(newUser);

        subscriptionService.createSubscription(savedUser);

        // Generating the authentication in the signup
        Authentication authentication = authenticate(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication); // Use injected JwtProvider

        AuthResponse response = new AuthResponse();
        response.setMessage("signup success");
        response.setJwt(jwt);

        // Return the created user
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication); // Use injected JwtProvider

        AuthResponse response = new AuthResponse();
        response.setMessage("signin success");
        response.setJwt(jwt);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }
}
