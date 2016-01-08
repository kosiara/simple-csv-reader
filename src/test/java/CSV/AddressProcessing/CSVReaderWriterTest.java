package CSV.AddressProcessing;

import com.bkosarzycki.util.csv.CSVReaderWriter;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class CSVReaderWriterTest {

    private static final String CSV_2COLUMNS_TAB_SEP_PATH = "src/test/resources/sample_csv_tab_2_columns.csv";
    private static final String CSV_14COLUMNS_TAB_SEP_PATH = "src/test/resources/sample_csv_tab.csv";
    private static final String CSV_14COLUMNS_COMMA_SEP_PATH = "src/test/resources/sample_csv_comma.csv";
    private static final String CSV_14COLUMNS_TAB_SEP_ISO_88591_PATH = "src/test/resources/sample_csv_tab_iso_88591.csv";
    private static final String CSV_TEST_WRITE_2COLUMNS_TAB_SEP_PATH = "src/test/resources/write_test_csv_tab_2_columns.csv";
    private static final String CSV_TEST_WRITE_READ_2COLUMNS_TAB_SEP_PATH = "src/test/resources/write_read_test_csv_tab_2_columns.csv";

    @Test(expected = FileNotFoundException.class)
    public void readNonExistentFile() throws Exception {
        CSVReaderWriter csvReaderWriter = new CSVReaderWriter();
        csvReaderWriter.open("non_existent.csv", CSVReaderWriter.Mode.Read);
        csvReaderWriter.close();
    }

    @Test
    public void readOneColumn() throws Exception {
        CSVReaderWriter csvReaderWriter = new CSVReaderWriter();
        csvReaderWriter.open(CSV_2COLUMNS_TAB_SEP_PATH, CSVReaderWriter.Mode.Read);
        String[] result = csvReaderWriter.read();
        csvReaderWriter.close();

        assertEquals("User Name", result[0]);
    }

    @Test
    public void readTwoColumns() throws Exception {
        CSVReaderWriter csvRW = new CSVReaderWriter();
        csvRW.open(CSV_2COLUMNS_TAB_SEP_PATH, CSVReaderWriter.Mode.Read);
        String[] result = csvRW.read();
        csvRW.close();

        assertEquals("User Name", result[0]);
        assertEquals("First Name", result[1]);
    }

    @Test
    public void readThreeColumns() throws Exception {
        CSVReaderWriter csvReaderWriter = new CSVReaderWriter();
        csvReaderWriter.open(CSV_14COLUMNS_TAB_SEP_PATH, CSVReaderWriter.Mode.Read);
        String[] result = csvReaderWriter.read();
        csvReaderWriter.close();

        assertEquals("User Name", result[0]);
        assertEquals("First Name", result[1]);
        assertEquals("Last Name", result[2]);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void readNonExistentColumn() throws Exception {
        CSVReaderWriter csvReaderWriter = new CSVReaderWriter();
        csvReaderWriter.open(CSV_2COLUMNS_TAB_SEP_PATH, CSVReaderWriter.Mode.Read);
        String[] result = csvReaderWriter.read();
        csvReaderWriter.close();

        assertEquals("User Name", result[0]);
        assertEquals("First Name", result[1]);
        assertEquals(null, result[2]);
    }

    @Test
    public void readManyLines() throws Exception {
        CSVReaderWriter csvReaderWriter = new CSVReaderWriter();
        csvReaderWriter.open(CSV_2COLUMNS_TAB_SEP_PATH, CSVReaderWriter.Mode.Read);
        String[] result; int counter = 0;
        while ((result = csvReaderWriter.read()) != null) {
            switch (counter) {
                case 0:  assertEquals("User Name", result[0]);
                    break;
                case 3:  assertEquals("david@contoso.com", result[0]);
                    break;
                case 5:  assertEquals("melissa@contoso.com", result[0]);
                    break;
            }
            counter++;
        }
        assertEquals(counter, 6); //note: csv reader should stop reading at the last line with data even though file has some more empty lines
        csvReaderWriter.close();
    }

    @Test
    public void readTooManyLines() throws Exception {
        CSVReaderWriter csvReaderWriter = new CSVReaderWriter();
        csvReaderWriter.open(CSV_2COLUMNS_TAB_SEP_PATH, CSVReaderWriter.Mode.Read);
        String[] result;
        for (int i = 0; i < 10; i++)
            result = csvReaderWriter.read();
        csvReaderWriter.close();
    }

    @Test
    public void readDataSeparatedWithComma() throws Exception {
        CSVReaderWriter csvReaderWriter = new CSVReaderWriter(',');
        csvReaderWriter.open(CSV_14COLUMNS_COMMA_SEP_PATH, CSVReaderWriter.Mode.Read);
        String[] result; int counter = 0;
        while ((result = csvReaderWriter.read()) != null) {
            switch (counter) {
                case 0:  assertEquals("User Name", result[0]);
                         assertEquals("First Name", result[1]);
                    break;
                case 3:  assertEquals("david@contoso.com", result[0]);
                         assertEquals("David", result[1]);
                    break;
                case 5:  assertEquals("melissa@contoso.com", result[0]);
                         assertEquals("Melissa", result[1]);
                    break;
            }
            counter++;
        }
        assertEquals(counter, 6); //note: csv reader should stop reading at the last line with data even though file has some more empty lines
        csvReaderWriter.close();
    }

    @Test
    public void readDataInOtherCharset() throws Exception {
        CSVReaderWriter csvReaderWriter = new CSVReaderWriter("ISO-8859-1");
        csvReaderWriter.open(CSV_14COLUMNS_TAB_SEP_ISO_88591_PATH, CSVReaderWriter.Mode.Read);
        String[] result; int counter = 0;
        while ((result = csvReaderWriter.read()) != null) {
            switch (counter) {
                case 0:  assertEquals("User Name", result[0]);
                    assertEquals("First Name", result[1]);
                    break;
                case 3:  assertEquals("david@contoso.com", result[0]);
                    assertEquals("David", result[1]);
                    break;
                case 5:  assertEquals("melissa@contoso.com", result[0]);
                    assertEquals("Melissa", result[1]);
                    break;
            }
            counter++;
        }
        csvReaderWriter.close();
    }

    @Test
    public void readColumnsSkippingHeader() throws Exception {
        CSVReaderWriter csvReaderWriter = new CSVReaderWriter();
        csvReaderWriter.skipHeader(true);
        csvReaderWriter.open(CSV_14COLUMNS_TAB_SEP_PATH, CSVReaderWriter.Mode.Read);
        String[] result = csvReaderWriter.read();
        csvReaderWriter.close();

        assertEquals("chris@contoso.com", result[0]);
        assertEquals("Chris", result[1]);
        assertEquals("Green", result[2]);
    }

    @Test
    public void writeTwoColumns() throws Exception {
        File file = new File(CSV_TEST_WRITE_2COLUMNS_TAB_SEP_PATH);
        if (file.exists())
            file.delete();

        CSVReaderWriter csvRW = new CSVReaderWriter();
        csvRW.open(CSV_TEST_WRITE_2COLUMNS_TAB_SEP_PATH, CSVReaderWriter.Mode.Write);
        csvRW.write("User Name", "First Name");
        csvRW.write("test1@gmail.com", "Alex");
        csvRW.write("test2@gmail.com", "John");
        csvRW.close();

        assertTrue(file.exists());
        if (file.exists())
            file.delete();
    }

    @Test
    public void writeReadTwoColumns() throws Exception {
        File file = new File(CSV_TEST_WRITE_READ_2COLUMNS_TAB_SEP_PATH);
        if (file.exists())
            file.delete();

        CSVReaderWriter csvRW = new CSVReaderWriter();
        csvRW.open(CSV_TEST_WRITE_READ_2COLUMNS_TAB_SEP_PATH, CSVReaderWriter.Mode.Write);
        csvRW.write("User Name", "First Name");
        csvRW.write("test1@gmail.com", "Alex");
        csvRW.write("test2@gmail.com", "John");
        csvRW.close();
        csvRW = null;

        assertTrue(file.exists());

        CSVReaderWriter csvRW2 = new CSVReaderWriter();
        csvRW2.open(CSV_TEST_WRITE_READ_2COLUMNS_TAB_SEP_PATH, CSVReaderWriter.Mode.Read);
        String[] result; int counter = 0;
        while ((result = csvRW2.read()) != null) {
            switch (counter) {
                case 0:  assertEquals("User Name", result[0]);
                    break;
                case 2:  assertEquals("test2@gmail.com", result[0]);
                    break;
            }
            counter++;
        }
        csvRW2.close();
        if (file.exists())
            file.delete();
    }

    @Test
    public void writeInTwoThreads() throws Exception {
        final File file = new File(new StringBuffer(CSV_TEST_WRITE_READ_2COLUMNS_TAB_SEP_PATH).insert(CSV_TEST_WRITE_READ_2COLUMNS_TAB_SEP_PATH.length() - 4, "_thread1").toString());
        final File file2 = new File(new StringBuffer(CSV_TEST_WRITE_READ_2COLUMNS_TAB_SEP_PATH).insert(CSV_TEST_WRITE_READ_2COLUMNS_TAB_SEP_PATH.length() - 4, "_thread2").toString());
        if (file.exists())
            file.delete();
        if (file2.exists())
            file2.delete();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CSVReaderWriter csvRW = new CSVReaderWriter();
                    csvRW.open(file.getAbsolutePath(), CSVReaderWriter.Mode.Write);
                    csvRW.write("User Name", "First Name");
                    csvRW.write("test1_thread1@gmail.com", "Alex");
                    csvRW.write("test2_thread1@gmail.com", "John");
                    csvRW.close();
                    csvRW = null;
                    assertTrue(file.exists());

                    checkLineContent(file.getAbsolutePath(), new String[]{"User Name", "test1_thread1@gmail.com", "test2_thread1@gmail.com"});

                    if (file.exists())
                        file.delete();
                } catch (Exception e) {
                    Thread t = Thread.currentThread();
                    t.getUncaughtExceptionHandler().uncaughtException(t, e);
                }
            }
        }).run();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CSVReaderWriter csvRW = new CSVReaderWriter();
                    csvRW.open(file2.getAbsolutePath(), CSVReaderWriter.Mode.Write);
                    csvRW.write("User Name", "First Name");
                    csvRW.write("test1_thread2@gmail.com", "Alex");
                    csvRW.write("test2_thread2@gmail.com", "John");
                    csvRW.close();
                    csvRW = null;
                    assertTrue(file2.exists());

                    checkLineContent(file2.getAbsolutePath(), new String[]{"User Name", "test1_thread2@gmail.com", "test2_thread2@gmail.com"});
                    if (file2.exists())
                        file2.delete();
                } catch (Exception e) {
                    Thread t = Thread.currentThread();
                    t.getUncaughtExceptionHandler().uncaughtException(t, e);
                }
            }
        }).run();
    }

    @Test
    public void readWithNewAPI() throws Exception {
        CSVReaderWriter csvReaderWriter = new CSVReaderWriter();
        csvReaderWriter.open(CSV_2COLUMNS_TAB_SEP_PATH, CSVReaderWriter.Mode.Read);
        String[] read; int counter = 0;
        while ((read = csvReaderWriter.read()) != null) {
            switch (counter) {
                case 0:  assertEquals("User Name", read[0]);
                    break;
                case 3:  assertEquals("david@contoso.com", read[0]);
                    break;
                case 5:  assertEquals("melissa@contoso.com", read[0]);
                    break;
            }
            counter++;
        }
        assertEquals(6, counter);
        csvReaderWriter.close();
    }

    @Test
    public void readTooManyLinesWithNewAPI() throws Exception {
        CSVReaderWriter csvReaderWriter = new CSVReaderWriter();
        csvReaderWriter.open(CSV_2COLUMNS_TAB_SEP_PATH, CSVReaderWriter.Mode.Read);
        String[] read;
        for (int i = 0; i < 10; i++)
            csvReaderWriter.read();
        csvReaderWriter.close();
    }

    private void checkLineContent(String absolutePath, String[] exptectedArr) throws Exception {
        CSVReaderWriter csvRW = new CSVReaderWriter();
        csvRW.open(absolutePath, CSVReaderWriter.Mode.Read);
        String[] result = new String[1];
        int counter = 0;
        while ((result = csvRW.read()) != null) {
            assertEquals(exptectedArr[counter], result[0]);
            counter++;
        }
        csvRW.close();
    }
}
