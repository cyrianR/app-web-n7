package fr.n7.spring_boot_api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.n7.spring_boot_api.model.ERole;
import fr.n7.spring_boot_api.model.Role;
import fr.n7.spring_boot_api.model.User;
import fr.n7.spring_boot_api.payload.request.PasswordUpdateRequest;
import fr.n7.spring_boot_api.payload.response.MessageResponse;
import fr.n7.spring_boot_api.payload.response.RoleUpdateResponse;
import fr.n7.spring_boot_api.payload.response.UserResponse;
import fr.n7.spring_boot_api.repository.RoleRepository;
import fr.n7.spring_boot_api.repository.UserRepository;
import fr.n7.spring_boot_api.security.services.UserDetailsImpl;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class UserController {
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Get all users with optional filter by username
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(required = false) String username) {
        try {
            List<UserResponse> users = new ArrayList<UserResponse>();

            if (username == null) {
                userRepository.findAll().forEach(u -> users.add(userToUserResponse(u)));
            } else {
                userRepository.findByUsernameContaining(username).forEach(u -> users.add(userToUserResponse(u)));
            }

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get user by ID
    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return new ResponseEntity<>(userToUserResponse(user.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update user roles by ID
    @PutMapping("/user/role/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUserRoles(@PathVariable("id") long id, @RequestBody Set<String> roleNames) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            User newUser = user.get();
            Set<Role> roles = roleNames.stream()
                .map(roleName -> {
                    Role role = roleRepository.findByName(ERole.valueOf(roleName))
                            .orElseThrow(() -> new RuntimeException("Error: Role " + roleName + " is not found."));
                    return role;
                })
                .collect(Collectors.toSet());
            newUser.setRoles(roles);

            RoleUpdateResponse roleUpdateResponse = new RoleUpdateResponse(
                newUser.getId(),
                newUser.getUsername(),
                roles.stream().map(Role::getName).collect(Collectors.toList())
            );

            // Send WebSocket notification
            messagingTemplate.convertAndSend("/topic/roles", roleUpdateResponse);

            return new ResponseEntity<>(userToUserResponse(userRepository.save(newUser)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete user by ID
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user/username")
    public ResponseEntity<?> updateUsername(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");

            // Check if the username is null or empty
            if (username == null || username.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Username cannot be empty"));
            }

            // Check if the username is already taken
            if (userRepository.existsByUsername(username)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Email is already in use!"));
            }

            // Retrieve the currently authenticated user
            Optional<User> user = userRepository.findById(userDetails.getId());
            if (user.isPresent()) {
                User currentUser = user.get();
                currentUser.setUsername(username);
                userRepository.save(currentUser);

                return ResponseEntity.ok(new MessageResponse("Username updated successfully!"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An error occurred while updating the username"));
        }
    }

    @PutMapping("/user/email")
    public ResponseEntity<?> updateEmail(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");

            // Check if the email is null or empty
            if (email == null || email.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Email cannot be empty"));
            }
        
            // Check if the email is already taken
            if (userRepository.existsByEmail(email)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Email is already in use!"));
            }
        
            // Retrieve the currently authenticated user
            Optional<User> user = userRepository.findById(userDetails.getId());
            if (user.isPresent()) {
                User currentUser = user.get();
                currentUser.setEmail(email);
                userRepository.save(currentUser);
            
                return ResponseEntity.ok(new MessageResponse("Email updated successfully!"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An error occurred while updating the email"));
        }
    }

    @PutMapping("/user/password")
    public ResponseEntity<?> updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        try {
            String newPassword = passwordUpdateRequest.getNewPassword();
            String oldPassword = passwordUpdateRequest.getOldPassword();

            // Retrieve the currently authenticated user
            Optional<User> user = userRepository.findById(userDetails.getId());
            if (user.isPresent()) {
                User currentUser = user.get();

                // Check if the old password matches the stored password
                if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Old password is incorrect"));
                }

                // Encode the new password and update it
                currentUser.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(currentUser);
            
                return ResponseEntity.ok(new MessageResponse("Password updated successfully!"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An error occurred while updating the password"));
        }
    }


    private UserResponse userToUserResponse(User user) {
        Set<String> roles = user.getRoles().stream()
            .map(role -> role.getName().name())
            .collect(Collectors.toSet());
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), roles);
    }
}
