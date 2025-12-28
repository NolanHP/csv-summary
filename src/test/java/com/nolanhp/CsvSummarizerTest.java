package com.nolanhp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.io.IOException;

public class CsvSummarizerTest {
    private static Path writeTempCsv(String content) throws IOException{
        Path file = Files.createTempFile("test",".csv");
        Files.writeString(file, content);
        return file;
    }

    @Test
    void testMean() throws IOException{
        Path file = writeTempCsv("age\n10\n20\n30");
        Map<String, ColumnStats> map = CsvSummarizer.summarize(file, ",");
        assertEquals(20.0, map.get("age").mean());

    } 

    @Test
    void testNulls() throws IOException{
        Path file = writeTempCsv("name,score,age\nalice,100,20\nbob,,23");
        Map<String, ColumnStats> map = CsvSummarizer.summarize(file, ",");
        assertEquals(1,map.get("score").nullCount);

    }

    @Test
    void testNonNumeric() throws IOException{
        Path file = writeTempCsv("name,score,age\nalice,100,20\nbob,abc,23");
        Map<String, ColumnStats> map = CsvSummarizer.summarize(file, ",");
        assertFalse(map.get("score").isNumeric);
    }

    
    @Test
    void testEmpty() throws IOException{
        Path file = writeTempCsv("");
        Map<String, ColumnStats> map = CsvSummarizer.summarize(file, ",");
        assertEquals(0, map.size());
    }

    @Test
    void customDelimiter() throws IOException{
        Path file = writeTempCsv("age;name;height");
        Map<String, ColumnStats> map = CsvSummarizer.summarize(file, ";");
        assertEquals("name", map.get("name").name);
    }
}
