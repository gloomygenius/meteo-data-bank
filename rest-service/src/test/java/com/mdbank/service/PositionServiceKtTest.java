package com.mdbank.service;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class PositionServiceKtTest {
//    @Test
//    public void transformLatToIndex() throws Exception {
//        int index = PositionServiceKt.transformLatToIndex(-90);
//        assertThat(index, is(0));
//
//        index = PositionServiceKt.transformLatToIndex(0);
//        assertThat(index, is(180));
//
//        index = PositionServiceKt.transformLatToIndex(90);
//        assertThat(index, is(360));
//
//        index = PositionServiceKt.transformLatToIndex(75.3);
//        assertThat(index, is(331));
//
//        index = PositionServiceKt.transformLatToIndex(75.2);
//        assertThat(index, is(330));
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void transformLonToIndex() throws Exception {
//        int minIndex = PositionServiceKt.transformLonToIndex(-180);
//        assertThat(minIndex, is(0));
//
//        int maxIndex = PositionServiceKt.transformLonToIndex(180);
//        assertThat(maxIndex, is(576));
//
//        int index = PositionServiceKt.transformLonToIndex(0);
//        assertThat(index, is(288));
//
//        PositionServiceKt.transformLonToIndex(190);
//    }
//
//    @Test
//    public void testBiDirectionalConvert() {
//        int index1 = PositionServiceKt.transformLonToIndex(-180);
//        double lon1 = PositionServiceKt.transformIndexToLon(index1);
//        assertEquals(-180, lon1, 0.000000001);
//
//        int index2 = PositionServiceKt.transformLonToIndex(30);
//        double lon2 = PositionServiceKt.transformIndexToLon(index2);
//        assertEquals(30, lon2, 0.000000001);
//
//        int index3 = PositionServiceKt.transformLonToIndex(31);
//        double lon3 = PositionServiceKt.transformIndexToLon(index3);
//        assertNotEquals(-180, lon3);
//
//        int latIndex1 = PositionServiceKt.transformLatToIndex(-90);
//        double lat1 = PositionServiceKt.transformIndexToLat(latIndex1);
//        assertEquals(-90, lat1, 0.000000001);
//
//        int latIndex2 = PositionServiceKt.transformLatToIndex(20);
//        double lat2 = PositionServiceKt.transformIndexToLat(latIndex2);
//        assertEquals(20, lat2, 0.000000001);
//    }
}