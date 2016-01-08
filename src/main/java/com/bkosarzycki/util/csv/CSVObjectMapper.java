package com.bkosarzycki.util.csv;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 *
 * Simple csv->POJO mapper. Reads data row by row and maps each to the object T.
 * Column names do not be in order but their names need to match object fields (spaces and letter size do not matter).
 *
 * @param <T> row data will be mapped to object of this class
 */
public class CSVObjectMapper<T>  {

    CSVReaderWriter _readerWriter;
    String[] _columnNames;
    Field[] _columnMapping;

    /**
     * Creates mapper opens the file and tries to map columns with object fields.
     *
     * @param fileName  csv file path
     * @param pClass
     * @throws CSVReaderWriterUnknownFileModeException
     * @throws IOException
     * @throws CSVCannotMapColumnsException  if columns do not match mapper is not created.
     */
    public CSVObjectMapper(String fileName, Class<T> pClass) throws CSVReaderWriterUnknownFileModeException, IOException, CSVCannotMapColumnsException {
        _readerWriter = new CSVReaderWriter();
        _readerWriter.skipHeader(false);
        _readerWriter.open(fileName, CSVReaderWriter.Mode.Read);
        _columnNames = _readerWriter.read();
        if (_columnNames == null || _columnNames.length == 0)
            throw new CSVCannotMapColumnsException();

        _columnMapping = new Field[_columnNames.length];
        for (int i = 0; i < _columnNames.length; i++)
            _columnMapping[i] = simpleNameMatch(_columnNames[i], pClass);

        allPojoFieldsFoundCheck(pClass);
    }


    /**
     * Reads a single row from the csv file and maps it to the object of class T.
     *
     * @param pClass
     * @return object with row data
     */
    public T read(Class<T> pClass) {
        String[] values = _readerWriter.read();
        if (values == null || values.length == 0)
            return null;

        try {
            T object = pClass.newInstance();
            int _counter = 0;
            for(Field field : _columnMapping) {
                if (field == null) {
                    _counter++;
                    continue;
                }
                Field df = object.getClass().getDeclaredField(field.getName());
                df.setAccessible(true);
                df.set(object, values[_counter++]);
            }

            return object;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Closes csv file.
     */
    public void close() {
        _readerWriter.close();
    }

    /**
     * Simplified matching of fields&columns (POJO<->CSV). Letter size and spaces in names are irrelevant.
     * Order of the columns in the csv file is also irrelevant in the matching process.
     *
     * @param name
     * @param pClass
     * @return
     */
    private Field simpleNameMatch(String name, Class<T> pClass) {
        Field[] fields = pClass.getDeclaredFields();
        for (Field f : fields)
            if (name.trim().toLowerCase().replace(" ", "").equals(f.getName().toLowerCase().trim()))
                return f;

        return null;
    }

    /**
     * Checks whether all fields from the POJO class are successfully mapped to csv columns.
     *
     * @param pClass
     * @throws CSVCannotMapColumnsException
     */
    private void allPojoFieldsFoundCheck(Class<T> pClass) throws CSVCannotMapColumnsException {
        for (Field classField : pClass.getDeclaredFields()){
            Field foundField = null;
            for (Field mappedField : _columnMapping)
                if (mappedField != null && mappedField.equals(classField))
                    foundField = mappedField;
            if (foundField == null)
                throw new CSVCannotMapColumnsException();
        }
    }
}
