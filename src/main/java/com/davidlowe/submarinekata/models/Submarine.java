package com.davidlowe.submarinekata.models;


import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * The Submarine class represents the submarine itself.
 * Its current location is updated via event processing of CommandAvailableEvent objects.
 */
@Getter
@RequiredArgsConstructor
@Component
public class Submarine
{
    private final Location currentLocation;
    private final SonarDb sonarDb = new SonarDb();

    private final ScannedSonarDataDb scannedSonarDataDb = new ScannedSonarDataDb();

    /**
     * Processes a CommandAvailableEvent, which could change the submarine's current location.
     *
     * @param event Event containing a Command that was received.
     */
    @EventListener
    public void commandAvailable(@NonNull CommandAvailableEvent event)
    {
        if (currentLocation.processCommand(event.getCommand()))
        {
            // Command changed the submarine's location, so we fill in our map using
            // the scanned sonar data.
            val scannerData = scannedSonarDataDb.getScannerKey2ScannerData(currentLocation.getHorizontalLocation(), currentLocation.getDepth());
            if (scannerData != null)
            {
                // subCenterLocation will contain sub's vertical and horizontal position at the center of the ScannerData.
                val subCenterLocation = ScannerData.getGridSize() / 2;

                // Sub is centered in the grid, so the starting real world depth is
                // above the sub by subCenterLocation.
                // Example:
                //    Grid of 3 means the sub's center location is 3 / 2 = 1 for mapping purposes (which is ints for (x,y))
                var realWorldDepth = ((int) currentLocation.getDepth()) - subCenterLocation;

                val rowItor = scannerData.getRowIterator();
                while (rowItor.hasNext())
                {
                    val row = rowItor.next();

                    for (int idx = 0, realWorldHorizontalPos = ((int) currentLocation.getHorizontalLocation()) - subCenterLocation;
                         idx < ScannerData.getGridSize();
                         ++idx, ++realWorldHorizontalPos)
                    {
                        sonarDb.addScannedData(realWorldHorizontalPos, realWorldDepth, row.charAt(idx));
                    }

                    ++realWorldDepth;
                }
            }
        }
    }

    public void printMap()
    {
        val depthItor = sonarDb.getDepthIterator();
        while (depthItor.hasNext())
        {
            System.out.println(depthItor.next());
        }
    }
}
