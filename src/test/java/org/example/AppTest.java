package org.example;

import org.example.db.Component;
import org.example.io.CsvImporter;
import org.example.networking.Wikipedia;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testComponentGetterMethods() {
        Component c = new Component("Ryzen 7", "AMD", 399.99, "Black", "CPU");

        assertEquals("Ryzen 7", c.getName());
        assertEquals("AMD", c.getBrand());
        assertEquals(399.99, c.getPrice());
        assertEquals("Black", c.getColor());
        assertEquals("CPU", c.getType());
    }

    @Test
    public void testWikipediaInformation() {
        try {
            String info = Wikipedia.getWikipediaInformation("CPU");
            assertNotNull(info);
            assertTrue(info.length() > 0);
        } catch (IOException e) {
            fail("IOException should not have occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCsvImporterHandlesMissingFile() {
        assertDoesNotThrow(() ->
                CsvImporter.importComponentsFromCsv("non_existent_file.csv")
        );
    }

    // Optional test for future enhancement if you extract logic
    @Test
    public void testWikipediaInvalidTerm() {
        try {
            String result = Wikipedia.getWikipediaInformation("asdfsadf123!@#");
            assertNull(result); // It should return null or a default message on failure
        } catch (IOException e) {
            // Acceptable if thrown
            assertTrue(e.getMessage().contains("Failed to fetch data"));
        }
    }
}
