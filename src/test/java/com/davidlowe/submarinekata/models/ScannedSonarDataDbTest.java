package com.davidlowe.submarinekata.models;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class ScannedSonarDataDbTest
{

    @Test
    void loadData() throws IOException
    {
        ScannedSonarDataDb db = new ScannedSonarDataDb();
        File jsonFile = new File("C:\\workspaces\\SubmarineKata\\docs\\scanner-data.json");
        db.loadData(jsonFile);
    }
}