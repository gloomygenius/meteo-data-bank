package com.mdbank.repository;

import com.mdbank.Application;
import com.mdbank.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:/application-test.properties")
@ContextConfiguration(classes = Application.class)
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void testGetByEmail() {
        Optional<User> byEmail = userRepository.findByEmailIgnoreCase("user@mail.com");
        assertTrue(byEmail.isPresent());
    }
}