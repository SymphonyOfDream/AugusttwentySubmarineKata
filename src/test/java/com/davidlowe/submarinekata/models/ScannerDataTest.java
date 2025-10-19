package com.davidlowe.submarinekata.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScannerDataTest
{
    final static String DEFAULT_TEST_DATA = "123456789";

    @Test
    void getRow_firstRow()
    {
        ScannerData scannerData = new ScannerData(DEFAULT_TEST_DATA);

        String firstRow = scannerData.getRow(0);
        assertEquals("123", firstRow);
    }

    @Test
    void getRow_secondRow()
    {
        ScannerData scannerData = new ScannerData(DEFAULT_TEST_DATA);

        String firstRow = scannerData.getRow(1);
        assertEquals("456", firstRow);
    }

    @Test
    void getRow_thirdRow()
    {
        ScannerData scannerData = new ScannerData(DEFAULT_TEST_DATA);

        String firstRow = scannerData.getRow(2);
        assertEquals("789", firstRow);
    }

    @Test
    void getRow_invalidRow_tooLarge()
    {
        ScannerData scannerData = new ScannerData(DEFAULT_TEST_DATA);


        assertThrows(IllegalArgumentException.class, () ->
        {
            scannerData.getRow(ScannerData.ARRAY_ROW_MAX_SIZE);
        });
    }

    @Test
    void getRow_invalidRow_negative()
    {
        ScannerData scannerData = new ScannerData(DEFAULT_TEST_DATA);


        assertThrows(IllegalArgumentException.class, () ->
        {
            scannerData.getRow(-1);
        });
    }
}