package CSV.AddressProcessing;

import CSV.AddressProcessing.model.SampleCSVRow;
import CSV.AddressProcessing.model.SampleCSVRow2;
import com.bkosarzycki.util.csv.CSVCannotMapColumnsException;
import com.bkosarzycki.util.csv.CSVObjectMapper;
import org.junit.Test;
import static junit.framework.TestCase.assertEquals;

public class CSVObjectMapperTest {

    private static final String CSV_2COLUMNS_TAB_SEP_PATH = "src/test/resources/sample_csv_tab_2_columns.csv";
    private static final String CSV_3COLUMNS_TAB_SEP_PATH = "src/test/resources/sample_csv_tab_3_columns.csv";
    private static final String CSV_2COLUMNS_TAB_SEP_INVERTED_COLS_PATH = "src/test/resources/sample_csv_tab_2_columns_inverted.csv";
    private static final String CSV_2COLUMNS_TAB_SEP_NO_HEADER_PATH = "src/test/resources/sample_csv_tab_2_columns_no_header.csv";

    @Test
    public void readOneColumn() throws Exception {
        CSVObjectMapper<SampleCSVRow> mapper = new CSVObjectMapper(CSV_2COLUMNS_TAB_SEP_PATH, SampleCSVRow.class);
        SampleCSVRow row = mapper.read(SampleCSVRow.class);
        mapper.close();
        assertEquals("chris@contoso.com", row.getUserName());
        assertEquals("Chris", row.getFirstName());
    }

    @Test
    public void readTooManyRows() throws Exception {
        CSVObjectMapper<SampleCSVRow> mapper = new CSVObjectMapper(CSV_2COLUMNS_TAB_SEP_PATH, SampleCSVRow.class);
        for (int i = 0; i < 10; i++) {
            SampleCSVRow row = mapper.read(SampleCSVRow.class);
            if (i == 1)
                assertEquals("Ben", row.getFirstName());
            else if (i == 2)
                assertEquals("david@contoso.com", row.getUserName());
            else if (i == 9)
                assertEquals(null, row);
        }

        mapper.close();
    }

    @Test
    public void readInvertedColumns() throws Exception {
        CSVObjectMapper<SampleCSVRow> mapper = new CSVObjectMapper(CSV_2COLUMNS_TAB_SEP_INVERTED_COLS_PATH, SampleCSVRow.class);
        SampleCSVRow row = mapper.read(SampleCSVRow.class);
        mapper.close();
        assertEquals("chris@contoso.com", row.getUserName());
        assertEquals("Chris", row.getFirstName());
    }

    @Test
    public void readFirstAndLastColumnSkippingTheMiddleOne() throws Exception {
        CSVObjectMapper<SampleCSVRow2> mapper = new CSVObjectMapper(CSV_3COLUMNS_TAB_SEP_PATH, SampleCSVRow2.class);
        SampleCSVRow2 row = mapper.read(SampleCSVRow2.class);
        mapper.close();
        assertEquals("chris@contoso.com", row.getUserName());
        assertEquals("Green", row.getLastName());
    }

    @Test(expected=CSVCannotMapColumnsException.class)
    public void readFileWithoutHeader() throws Exception {
        CSVObjectMapper<SampleCSVRow> mapper = new CSVObjectMapper(CSV_2COLUMNS_TAB_SEP_NO_HEADER_PATH, SampleCSVRow.class);
        mapper.close();
    }
}
