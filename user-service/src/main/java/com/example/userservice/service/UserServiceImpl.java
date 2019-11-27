package com.example.userservice.service;

import com.example.userservice.exception.ErrorResponse;
import com.example.userservice.exception.LoginException;
import com.example.userservice.model.User;
import com.example.userservice.model.UserRole;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.responseobject.JwtResponse;
import com.example.userservice.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    @Qualifier("encoder")
    PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    JwtUtil jwtUtil;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public JwtResponse signup(User user) throws LoginException {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        UserRole userRole = userRoleService.getUserRoleByName("ROLE_USER");
        if (userRole == null) {
            throw new LoginException("Error: no role of 'ROLE_USER' found in the database. Please create a new 'ROLE_USER' role");
        } else {
            user.addUserRole(userRole);
        }

        User savedUser = userRepository.save(user);
        if (savedUser == null) {
            throw new LoginException("Database error: unable to save user");
        } else {
            UserDetails userDetails = loadUserByUsername(savedUser.getUsername());
            logger.info("Created new user: " + savedUser.getUsername());
            return new JwtResponse(jwtUtil.generateToken(userDetails), savedUser.getUsername());
        }
    }

    @Override
    public JwtResponse login(User user) throws Exception {
        User foundUser = userRepository.findByEmail(user.getEmail());

        if (foundUser == null) {
            throw new LoginException("Could not find an account for that username/password");
        } else if (!bCryptPasswordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            throw new LoginException("Password incorrect. Please try again.");
        } else {
            UserDetails userDetails = loadUserByUsername(foundUser.getUsername());
            return new JwtResponse(jwtUtil.generateToken(userDetails), foundUser.getUsername());
        }
    }

    public UserDetails loadUserByUsername(String username) {
        User user = getUser(username);

        if (user == null) {
            throw new UsernameNotFoundException("Database error: user not found");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                true, true, true, true, getGrantedAuthorities(user));
    }

    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<UserRole> userRoles = user.getUserRoles();
        for (UserRole userRole : userRoles) {
            authorities.add(new SimpleGrantedAuthority(userRole.getName()));
        }
        return authorities;
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }
}
