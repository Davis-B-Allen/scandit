package com.example.authservice.service;

import com.example.authservice.model.User;
import com.example.authservice.model.UserRole;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.util.JwtResponse;
import com.example.authservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
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

    @Override
    public JwtResponse signup(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        UserRole userRole = userRoleService.getUserRoleByName("ROLE_USER");
        if (userRole != null) user.addUserRole(userRole);

        User savedUser = userRepository.save(user);
        if (savedUser != null) {
            UserDetails userDetails = loadUserByUsername(savedUser.getUsername());
            return new JwtResponse(jwtUtil.generateToken(userDetails), savedUser.getUsername());
        }
        return null; // TODO: throw some more sensible exception
    }

    @Override
    public JwtResponse login(User user) {
        User foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser != null && bCryptPasswordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            UserDetails userDetails = loadUserByUsername(foundUser.getUsername());
            return new JwtResponse(jwtUtil.generateToken(userDetails), foundUser.getUsername());
        }
        return null; // TODO: throw some more sensible exception
    }

    public UserDetails loadUserByUsername(String username) {
        User user = getUser(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
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
