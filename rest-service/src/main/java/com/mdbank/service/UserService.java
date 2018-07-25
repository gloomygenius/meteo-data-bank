package com.mdbank.service;

import com.mdbank.model.User;
import com.mdbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getAuthorizedUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .map(UserDetails.class::cast)
                .map(UserDetails::getUsername)
                .flatMap(this::getUserByEmail)
                .orElseThrow(() -> new IllegalStateException("Ошибка определения текущего авторизованного пользователя"));
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    public User addNewUser(User user) {
        return (user != null && this.getUserByEmail(user.getEmail()) == null) ? userRepository.save(user) : user;
    }
}
