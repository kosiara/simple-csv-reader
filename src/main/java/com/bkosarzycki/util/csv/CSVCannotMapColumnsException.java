package com.bkosarzycki.util.csv;

/**
 * Signals that columns cannot be mapped
 * @see CSVObjectMapper cannot find the header or that columns do not match.
 */
public class CSVCannotMapColumnsException extends Exception {
    public CSVCannotMapColumnsException() {
        super("Cannot map columns");
    }
}
