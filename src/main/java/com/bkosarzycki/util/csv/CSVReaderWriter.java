package com.bkosarzycki.util.csv;

import java.io.*;
import java.util.logging.Logger;

/**
 * Used for buffered reading/writing of csv files.
 *
 * <pre>
 *     CSVReaderWriter csvRW = new CSVReaderWriter();
 *     csvRW.open("fileName.csv", CSVReaderWriter.Mode.Read);
 *     String[] result = csvRW.read();
 *     csvRW.close();
 * </pre>
 *
 */
public class CSVReaderWriter {
    private static Logger LOGGER = Logger.getLogger(CSVReaderWriter.class.getName());
    private BufferedReader _bufferedReader = null;
    private BufferedWriter _bufferedWriter = null;
    private char _delimiter = '\t';
    private String _encoding = "UTF-8";
    private boolean _skipHeaderLine = false;
    private boolean _headerLineSkipped = false;

    public enum Mode { Read, Write }

    /**
     * Creates a csv reader with default encoding and delimiters.
     */
    public CSVReaderWriter() {}

    /**
     * Creates a csv reader with a specific delimiter.
     *
     * @param delimiter  delimiter character used to separate columns
     */
    public CSVReaderWriter(char delimiter) {
        _delimiter = delimiter;
    }

    /**
     * Creates a csv reader with a specific encoding.
     *
     * @param encoding  character encoding used in the entire document (e.g. 'UTF-8' or 'ISO-8859-1')
     */
    public CSVReaderWriter(String encoding) {
        _encoding = encoding;
    }

    /**
     * Opens the csv file for reading or writing
     *
     * @param fileName  path to csv file
     * @param mode  Mode.Read or Mode.Write
     * @throws CSVReaderWriterUnknownFileModeException  wrong File mode given as an argument
     * @throws IOException  error reading the file
     */
    public void open(String fileName, Mode mode) throws CSVReaderWriterUnknownFileModeException, IOException {
        if (mode == Mode.Read) {
            _bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), _encoding));
        } else if (mode == Mode.Write) {
            FileWriter fileWriter = new FileWriter(fileName);
            _bufferedWriter = new BufferedWriter(fileWriter);
        } else {
            throw new CSVReaderWriterUnknownFileModeException(fileName);
        }
    }

    /**
     * Writes a single line to the csv file.
     *
     * @param columns  consequent columns in the csv file
     * @throws IOException
     */
    public void write(String... columns) throws IOException {
        String outPut = "";

        for (int i = 0; i < columns.length; i++) {
            outPut += columns[i];
            if ((columns.length - 1) != i)
                outPut += _delimiter;
        }

        writeLine(outPut);
    }

    /**
     * Reads a single line from the csv file.
     *
     * @return data read, null if nothing was read
     */
    public String[] read() {
        String line = readLine();
        if (_skipHeaderLine && !_headerLineSkipped) {
            line = readLine();
            _headerLineSkipped = true;
        }
        if (line == null || line.isEmpty())
            return null;
        String[] splitLine = line.split(String.valueOf(_delimiter));
        for (int i = 0; i < splitLine.length; i++)
            splitLine[i] = splitLine[i].replaceAll("[\uFEFF-\uFFFF]", "").trim();

        return splitLine;
    }

    /**
     * Closes all the buffers and enables object disposal. Use this method to prevent memory leaks.
     */
    public void close() {
        try {
            if (_bufferedReader != null)
                _bufferedReader.close();
            if (_bufferedWriter != null)
                _bufferedWriter.close();
        } catch (Exception exc) {
            LOGGER.severe("Error closing buffered reader/writer");
        } finally {
            _bufferedWriter = null;
            _bufferedReader = null;
        }
    }

    /**
     * Sets a specific delimiter for csv file.
     *
     * @param delimiter  delimiter character used to separate columns
     */
    public void setDelimiter(char delimiter) {
        this._delimiter = delimiter;
    }

    /**
     * Sets specific encoding for csv file.
     *
     * @param encoding  character encoding used in the entire document (e.g. 'UTF-8' or 'ISO-8859-1')
     */
    public void setEncoding(String encoding) {
        this._encoding = encoding;
    }

    /**
     * Tells CSVReaderWriter to skip the first line of the file.
     *
     * @param skipHeader  true if first line should be omitted
     */
    public void skipHeader(boolean skipHeader) {
        _skipHeaderLine = skipHeader;
    }

    private void writeLine(String line) throws IOException {
        _bufferedWriter.write(line + "\r\n");
    }

    private String readLine() {
        try {
            return _bufferedReader.readLine();
        } catch (IOException e) {
            LOGGER.warning("IOException in reading line");
            return null;
        }
    }
}
