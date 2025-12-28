package com.nolanhp;

import java.util.LinkedHashMap;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.*;

public class CsvSummarizer {

    public static Map<String, ColumnStats> summarize(Path file, String delimiter) throws IOException {
        Map<String, ColumnStats> map = new LinkedHashMap<>(); 

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return map;
            }

            String[] headers = headerLine.split(delimiter);
            for (String h: headers) {
                ColumnStats stats = new ColumnStats();
                stats.name = h.trim();
                map.put(h.trim(), stats);
            }

            
            String line;
            while((line = reader.readLine()) !=null) {
                if(line.isBlank()){
                    continue;
                }
                String[] values = line.split((delimiter));
                for(int h = 0; h< headers.length; h++){
                    String val = h < values.length ? values[h] : "";
                    ColumnStats stats = map.get(headers[h].trim());
                    stats.update(val);
                }
            }
        }
        return map;
    }

    public static void printReport(Map<String, ColumnStats> map){
        for(ColumnStats s: map.values()){
            System.out.println("Column: " + s.name);
            System.out.printf("count=%d%n", s.count);
            System.out.printf("null count=%d%n", s.nullCount);
            System.out.printf("null percentage = %.2f%n", s.nullRate()*100);

            if(s.isNumeric){
                System.out.printf("min = %.2f%n", s.min);
                System.out.printf("max = %.2f%n", s.max);
                System.out.printf("mean = %.2f%n", s.mean());
            } else {
                System.out.println("Contains non-numeric value(s)");
            }
        }
    }

    public static void main(String[] args) throws IOException{
        String delimiter = ",";
        if(args.length==2){
            delimiter = args[1];
        }
        printReport(summarize(Path.of(args[0]), delimiter));

    }
}
