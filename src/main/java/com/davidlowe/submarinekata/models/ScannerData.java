package com.davidlowe.submarinekata.models;


import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.ApplicationContextException;

import java.util.Iterator;
import java.util.NoSuchElementException;

@Slf4j
@Getter
public class ScannerData
{
    public static final int X_Y_MAX_SIZE = 3;
    // Since the sub is centered for these, both of the following
    // values need to be odd.
    public static final int ARRAY_ROW_MAX_SIZE = X_Y_MAX_SIZE;
    public static final int ARRAY_COL_MAX_SIZE = X_Y_MAX_SIZE;

    private String data;

    public ScannerData()
    {
        // Your IDE will give warning that this is always false; however,
        // please leave it in to fail early if the dimensions are changed
        // in a future update and are accidentally not odd.
        if (ARRAY_ROW_MAX_SIZE % 1 == 1 || ARRAY_COL_MAX_SIZE % 1 == 1)
            throw new ApplicationContextException("Array dimensions must be odd");

        data = " ".repeat(ARRAY_ROW_MAX_SIZE * ARRAY_COL_MAX_SIZE);
    }

    @SneakyThrows
    public ScannerData(@NonNull String data)
    {
        // Our data CAN be all whitespace if the data is all clear (no features).
        if (data.length() != ARRAY_ROW_MAX_SIZE * ARRAY_COL_MAX_SIZE)
        {
            val msg = "Invalid data length: %d".formatted(data.length());
            log.warn(msg);
            throw new IllegalArgumentException(msg);
        }
        this.data = data;
    }

    public static int getGridSize()
    {
        return X_Y_MAX_SIZE;
    }

    public Iterator<String> getRowIterator()
    {
        return new RowIterator();
    }

    public Iterator<String> columnIterator()
    {
        return new ColumnIterator();
    }

    public String getRow(int rowNbr)
            throws IllegalArgumentException
    {
        if (rowNbr < 0 || rowNbr >= ARRAY_ROW_MAX_SIZE)
        {
            val msg = "Invalid row number: %d".formatted(rowNbr);
            log.warn(msg);
            throw new IllegalArgumentException(msg);
        }

        final int startIdx = rowNbr * ARRAY_COL_MAX_SIZE;
        return data.substring(startIdx, startIdx + ARRAY_COL_MAX_SIZE);
    }

    public String getColumn(int colNbr)
            throws IllegalArgumentException
    {
        if (colNbr < 0 || colNbr >= ARRAY_COL_MAX_SIZE)
        {
            val msg = "Invalid column number: %d".formatted(colNbr);
            log.warn(msg);
            throw new IllegalArgumentException(msg);
        }

        StringBuilder col = new StringBuilder();
        for (int idx = colNbr; idx < ARRAY_ROW_MAX_SIZE * ARRAY_COL_MAX_SIZE; idx += ARRAY_ROW_MAX_SIZE)
        {
            col.append(data.charAt(idx));
        }

        return col.toString();
    }

    private class RowIterator implements Iterator<String>
    {
        private int currentIndex = 0;

        @Override
        public boolean hasNext()
        {
            return currentIndex < X_Y_MAX_SIZE;
        }

        @Override
        public String next()
        {
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }
            return getRow(currentIndex++);
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Remove operation is not supported.");
        }
    }

    private class ColumnIterator implements Iterator<String>
    {
        private int currentIndex = 0;

        @Override
        public boolean hasNext()
        {
            return currentIndex < X_Y_MAX_SIZE;
        }

        @Override
        public String next()
        {
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }
            return getColumn(currentIndex++);
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Remove operation is not supported.");
        }
    }

}
