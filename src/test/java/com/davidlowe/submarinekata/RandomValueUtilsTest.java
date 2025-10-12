package com.davidlowe.submarinekata;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomValueUtilsTest
{

    @Test
    void randomPositiveSinglePrecisionDouble()
    {
        val randomPositiveSinglePrecisionDouble = RandomValueUtils.randomPositiveSinglePrecisionDouble();
        assertTrue(randomPositiveSinglePrecisionDouble > 0);
        assertEquals(Math.round(randomPositiveSinglePrecisionDouble * 10.0) / 10.0, randomPositiveSinglePrecisionDouble);
    }

    @Test
    void randomNegativeSinglePrecisionDouble()
    {
        val randomNegativeSinglePrecisionDouble = RandomValueUtils.randomNegativeSinglePrecisionDouble();
        assertTrue(randomNegativeSinglePrecisionDouble < 0);
        assertEquals(Math.round(randomNegativeSinglePrecisionDouble * 10.0) / 10.0, randomNegativeSinglePrecisionDouble);
    }
}