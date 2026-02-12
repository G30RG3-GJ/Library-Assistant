package library.assistant.export.pdf;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import library.assistant.export.pdf.ListToPDF.Orientation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ListToPDFTest {

    private ListToPDF listToPDF;
    private File tempFile;
    private List<List> data;

    @Before
    public void setUp() {
        listToPDF = new ListToPDF();
        data = new ArrayList<>();
        data.add(Arrays.asList("Header 1", "Header 2"));
        data.add(Arrays.asList("Row 1 Col 1", "Row 1 Col 2"));
        tempFile = new File("test_output.pdf");
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @After
    public void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void testDoPrintToPdf_Success() {
        boolean result = listToPDF.doPrintToPdf(data, tempFile, Orientation.PORTRAIT);

        Assert.assertTrue("Method should return true on success", result);
        Assert.assertTrue("Output file should exist", tempFile.exists());
        Assert.assertTrue("Output file should not be empty", tempFile.length() > 0);
    }

    @Test
    public void testDoPrintToPdf_NullFile() {
        boolean result = listToPDF.doPrintToPdf(data, null, Orientation.PORTRAIT);
        Assert.assertFalse("Method should return false when file is null", result);
    }
}
