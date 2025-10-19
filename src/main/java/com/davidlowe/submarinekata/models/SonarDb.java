package com.davidlowe.submarinekata.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

/**
 * SonarDb represents a map of all areas scanned by a submarine sonar.
 */
@Slf4j
@Getter
public class SonarDb
{
    /**
     * Map will contain the data for the submarine's sonar scan
     * Since we have allowed for a negative horizontal position we CANNOT use
     * a simple String to save all the map data. In particular, what would the
     * first character of a String represent? Horizontal position 0 or -1 or 1??
     * Here, we have a map of (x,y) coordinates pointing to a Character that represents
     * map data.
     * We have NO idea how large this can get, so this data structure will ONLY contain
     * map data (any (x,y) that does not have data is NOT inserted as a null Character,
     * or anything else).
     */
    private final Map<Integer, Map<Integer, Character>> depth2HorizontalLocation2SonarData;

    private Integer minHorizontalLocation = null;
    private Integer minDepth = null;

    private Integer maxHorizontalLocation = null;
    private Integer maxDepth = null;

    public SonarDb()
    {
        // Using TreeMap to ensure we can correctly retrieve data to generate a map
        // in order from the smallest positions to the largest (HashMap itself doesn't guarantee this).
        depth2HorizontalLocation2SonarData = new TreeMap<>();
    }


    /**
     * Inserts the specified map data into the sonar db.
     *
     * @param horizontalPosition Where on the X-axis the mapData is going to be placed.
     * @param depth              Where on the Y-axis the mapData is going to be placed.
     * @param mapData            The actual data to be placed.
     */
    public void addScannedData(@NonNull Integer horizontalPosition, @NonNull Integer depth, char mapData)
    {
        if (!depth2HorizontalLocation2SonarData.containsKey(depth))
        {
            depth2HorizontalLocation2SonarData.put(depth, new TreeMap<>());
        }

        val horizontalLocation2MapData = depth2HorizontalLocation2SonarData.get(depth);

        if (!horizontalLocation2MapData.containsKey(horizontalPosition))
        {
            horizontalLocation2MapData.put(horizontalPosition, mapData);
        }

        // Update the min/max horizontal/depth positions if necessary.
        if (minHorizontalLocation == null || horizontalPosition < minHorizontalLocation)
            minHorizontalLocation = horizontalPosition;
        if (minDepth == null || depth < minDepth)
            minDepth = depth;
        if (maxHorizontalLocation == null || horizontalPosition > maxHorizontalLocation)
            maxHorizontalLocation = horizontalPosition;
        if (maxDepth == null || depth > maxDepth)
            maxDepth = depth;
    }

    /**
     * Gets an entire depth's worth of horizontal map data.
     *
     * @param depth Depth of interest.
     *
     * @return String the full horizontal size of the map, with any X coordinates
     * that do not have data set to spaces.
     */
    public String getHorizontalMapDataForDepth(int depth)
    {
        // -5 to -1 would be 6 spaces.
        // -1 to 0 would be 2 spaces.
        // -1 to 1 would be 3 spaces.
        val totalSpaces = Math.abs(minHorizontalLocation) + Math.abs(maxHorizontalLocation)
                + (minHorizontalLocation < 0 && maxHorizontalLocation >= 0 ? 1 : 0);

        val mapRow = new StringBuilder(" ".repeat(totalSpaces));

        // If depth requested is not in our map at all, we'll return a string of spaces
        // for each available horizontal position.
        val horizontalLocation2MapData = depth2HorizontalLocation2SonarData.get(depth);
        if (depth >= minDepth && depth <= maxDepth && horizontalLocation2MapData != null)
        {
            // We simply go from minHorizontalLocation to maxHorizontalLocation and return the
            // map data at that (x,y) position.
            for (int outputIdx = 0, x = minHorizontalLocation; x <= maxHorizontalLocation; ++outputIdx, ++x)
            {
                val mapDataForHorizontalPosition = horizontalLocation2MapData.get(x);
                if (mapDataForHorizontalPosition != null)
                    mapRow.setCharAt(outputIdx, mapDataForHorizontalPosition);
            }
        }

        return mapRow.toString();
    }


    public Iterator<String> getDepthIterator()
    {
        return new SonarDb.DepthIterator();
    }


    private class DepthIterator implements Iterator<String>
    {
        private int currentIndex = 0;

        @Override
        public boolean hasNext()
        {
            return currentIndex < depth2HorizontalLocation2SonarData.size();
        }

        @Override
        public String next()
        {
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }
            return getHorizontalMapDataForDepth(currentIndex++);
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Remove operation is not supported.");
        }
    }

}
