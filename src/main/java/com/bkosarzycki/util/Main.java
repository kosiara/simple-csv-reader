package com.bkosarzycki.util;

import com.bkosarzycki.util.csv.CSVReaderWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Tests
 * @see CSVReaderWriter under heavy load. Main entry point of the app.
 */
public class Main {
    private static Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final String CSV_LARGE_OUTPUT = "output/large_output.csv";
    private static final int NUMBER_OF_ROWS = 10000000;
    private static final int RUN_GC_EVERY_ROWS = 4000;
    private static final int NO_MB_WARNING = 50;

    public static void main(String... args) {
        System.out.println("Run tests for CSV reader/writer from Junit!  ./gradlew test");

        System.out.println("Pressing enter will generate 2.4 GB of data");

        BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
        try {
            buffer.readLine();
        } catch (IOException e) {
            LOGGER.severe("Error reading line from console");
        }

        File cFile = new File(CSV_LARGE_OUTPUT);
        File parentDir = new File(cFile.getParent());
        if (cFile.exists())
            cFile.delete();
        parentDir.mkdirs();

        long used  = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Used [MB]: " + used/1024/1024);

        CSVReaderWriter csvRW = new CSVReaderWriter();

        try {
            csvRW.open(CSV_LARGE_OUTPUT, CSVReaderWriter.Mode.Write);
            for (int i = 0; i < NUMBER_OF_ROWS; i++) { //generate 2.4 GB OF DATA
                csvRW.write("User Name", "First Name", "Last Name");
                csvRW.write("test1@gmail.com", "Alex", "AAA");
                csvRW.write("test2@gmail.com", "John", "BBB");
                csvRW.write("test3@gmail.com", "John", "CCC");
                csvRW.write("test4@gmail.com", "John", "DDD");
                csvRW.write("test5@gmail.com", "John", "EEE");
                csvRW.write("test6@gmail.com", "John", "FFF");
                csvRW.write("test7@gmail.com", "John", "GGG");
                csvRW.write("test8@gmail.com", "John", "HHH");
                if (i % RUN_GC_EVERY_ROWS == 0)
                    System.gc(); //hint: ONLY for test purposes - deteriorates performance significantly
                if (i % 1000 == 0) {
                    long usedMem  = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                    System.out.println("w: " + String.format("%3.2f", i*100.0/NUMBER_OF_ROWS) + "% done" + ", Used [MB]: " + usedMem/1024/1024);
                    if (usedMem/1024/1024 > NO_MB_WARNING )
                        LOGGER.warning("Using too much memory!!!!");
                }
            }
            csvRW.close();

            csvRW = new CSVReaderWriter();
            csvRW.open(CSV_LARGE_OUTPUT, CSVReaderWriter.Mode.Read);
            for (int i = 0; i < NUMBER_OF_ROWS; i++) {
                String data[] = csvRW.read();

                if (i % RUN_GC_EVERY_ROWS == 0)
                    System.gc(); //hint: ONLY for test purposes - deteriorates performance significantly
                if (i % 1000 == 0) {
                    long usedMem  = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                    System.out.println("r: " + String.format("%3.2f", i*100.0/NUMBER_OF_ROWS) + "% done" + ", Used [MB]: " + usedMem/1024/1024);
                    if (usedMem/1024/1024 > NO_MB_WARNING )
                        LOGGER.warning("Using too much memory!!!!");
                }
            }
            csvRW.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File(CSV_LARGE_OUTPUT);
        if (file.exists())
            file.delete();
    }
}
