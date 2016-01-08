package com.bkosarzycki.util.csv;

/**
 * Signals that wrong File mode was given as an argument to open() method
 *
 * @see CSVReaderWriter
 */
public class CSVReaderWriterUnknownFileModeException extends Exception {
    public CSVReaderWriterUnknownFileModeException() {
        super("Unknown file mode for CSVReaderWriter");
    }

    public CSVReaderWriterUnknownFileModeException(String fileName) {
        super("Unknown file mode for CSVReaderWriter, file: " + fileName);
    }
}
