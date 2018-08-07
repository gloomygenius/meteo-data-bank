package test.integration.repository;

import com.mdbank.Application;
import com.mdbank.model.User;
import com.mdbank.model.dto.Role;
import com.mdbank.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:/application-test.properties")
@ContextConfiguration(classes = Application.class)
public class UserRepositoryTest {
    @Autowired
   private UserRepository userRepository;

    @Test
    public void testGetByEmail() {
        User user = new User();
        user.setEmail("user@mail.com");
        user.setPassword("Password");
        user.setRoles(Collections.singletonList(Role.USER));
        userRepository.save(user);

        User byEmail = userRepository.findByEmailIgnoreCase("user@mail.com");
        assertNotNull(byEmail);
    }
}