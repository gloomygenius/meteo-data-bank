package com.mdbank.model.validation;

import com.mdbank.model.Position;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class LatitudeValidatorTest {
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Если широта не входит в диапазон [-90..90], то значение не валидно
     */
    @Test
    public void testPositionIsNotValidIfLatitudeOutOfRange() {
        Position position = new Position(null, 100, 0, 0);
        Set<ConstraintViolation<Position>> violations = validator.validate(position);
        assertThat(violations, hasSize(1));
    }
}
