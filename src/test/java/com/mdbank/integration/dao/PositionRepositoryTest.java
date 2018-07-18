package com.mdbank.integration.dao;

import com.mdbank.Application;
import com.mdbank.repository.PositionRepository;
import com.mdbank.model.Position;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:/application-test.properties")
@ContextConfiguration(classes = Application.class)
public class PositionRepositoryTest {
    @Autowired
    PositionRepository positionRepository;

    @Test
    public void testSavePosition(){
        Position position = new Position(24, 50);
        Position save = positionRepository.save(position);
        assertNotNull(save.getId());
    }
}
