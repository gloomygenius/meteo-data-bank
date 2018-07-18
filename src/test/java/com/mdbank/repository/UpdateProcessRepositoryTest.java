package com.mdbank.repository;

import com.mdbank.Application;
import com.mdbank.model.UpdateProcess;
import com.mdbank.model.dto.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:/application-test.properties")
@ContextConfiguration(classes = Application.class)
public class UpdateProcessRepositoryTest {
    @Autowired
    UpdateProcessRepository processRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void testSave() {
        //noinspection ConstantConditions
        UpdateProcess updateProcess = new UpdateProcess(userRepository.findById(1L).get());
        updateProcess.setStatus(Status.PENDING);
        updateProcess.setDescription("Update some data");
        UpdateProcess save = processRepository.save(updateProcess);
        assertThat(save.getDescription(), is("Update some data"));
        System.out.println(updateProcess.toString());
    }
}