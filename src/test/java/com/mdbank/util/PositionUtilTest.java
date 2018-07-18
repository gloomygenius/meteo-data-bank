package com.mdbank.util;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PositionUtilTest {
    @Test
    public void transformLatToIndex() throws Exception {
        int index = PositionUtil.transformLatToIndex(-90);
        assertThat(index, is(0));

        index = PositionUtil.transformLatToIndex(0);
        assertThat(index, is(180));

        index = PositionUtil.transformLatToIndex(90);
        assertThat(index, is(360));

        index = PositionUtil.transformLatToIndex(75.3);
        assertThat(index, is(331));

        index = PositionUtil.transformLatToIndex(75.2);
        assertThat(index, is(330));
    }

    @Test(expected = IllegalArgumentException.class)
    public void transformLonToIndex() throws Exception {
        int minIndex = PositionUtil.transformLonToIndex(-180);
        assertThat(minIndex, is(0));

        int maxIndex = PositionUtil.transformLonToIndex(180);
        assertThat(maxIndex, is(576));

        int index = PositionUtil.transformLonToIndex(0);
        assertThat(index, is(288));

        PositionUtil.transformLonToIndex(190);
    }

}