package com.davidlowe.submarinekata.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@NoArgsConstructor
@Component
public class ScannedSonarDataDb
{
    private final Map<ScannerKey, ScannerData> scannerKey2ScannerData = new java.util.HashMap<>();


    public ScannerData getScannerKey2ScannerData(@NonNull ScannerKey scannerKey)
    {
        return scannerKey2ScannerData.get(scannerKey);
    }

    public ScannerData getScannerKey2ScannerData(int horizontalLocation, int depthLocation)
    {
        return getScannerKey2ScannerData(new ScannerKey(horizontalLocation, depthLocation));
    }

    public ScannerData getScannerKey2ScannerData(double horizontalLocation, double depthLocation)
    {
        return getScannerKey2ScannerData(new ScannerKey((int) Math.round(horizontalLocation), (int) Math.round(depthLocation)));
    }


    public void setScannerKey2ScannerData(@NonNull ScannerKey scannerKey, @NonNull ScannerData scannerData)
    {
        scannerKey2ScannerData.put(scannerKey, scannerData);
    }

    public void setScannerKey2ScannerData(int horizontalLocation, int depthLocation, @NonNull ScannerData scannerData)
    {
        scannerKey2ScannerData.put(new ScannerKey(horizontalLocation, depthLocation), scannerData);
    }


    public void clear()
    {
        scannerKey2ScannerData.clear();
    }


    public void loadData(@NonNull File jsonFile)
            throws IOException, IllegalArgumentException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonFile);

        // If the JSON is a simple object, you can directly convert it to a Map
        if (rootNode.isObject())
        {
            Map<String, Object> jsonMap = objectMapper.readValue(jsonFile, Map.class);
            jsonMap.forEach((key, value) ->
            {
                String cleanedString = key.replace("(", "").replace(")", "");

                // 2. Split the string by comma
                String[] parts = cleanedString.split(",");

                // Ensure there are exactly two parts
                if (parts.length != 2)
                {
                    throw new IllegalArgumentException("Invalid format: " + key);
                }

                // 3. Trim whitespace and convert to integers
                int horizontalPosition = Integer.parseInt(parts[0].trim());
                int depthPosition = Integer.parseInt(parts[1].trim());

                final ArrayList<String> valueList = (ArrayList<String>) value;
                String scannerData = "";
                for (int idx = 0; idx < valueList.size(); idx++)
                {
                    scannerData += valueList.get(idx);
                }

                scannerKey2ScannerData.put(new ScannerKey(horizontalPosition, depthPosition), new ScannerData(scannerData));
            });
        }
        else
        {
            System.out.println("JSON is not a simple object or contains nested structures.");
            // You would need to iterate and handle nested objects/arrays if present
            // Example for iterating through top-level fields:
            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
            while (fields.hasNext())
            {
                Map.Entry<String, JsonNode> field = fields.next();
                System.out.println("Key: " + field.getKey() + ", Value: " + field.getValue().asText());
            }
        }
    }
}
