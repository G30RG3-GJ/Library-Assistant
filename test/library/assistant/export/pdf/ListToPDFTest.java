package library.assistant.export.pdf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListToPDFTest {

    private ListToPDF listToPDF;
    private File tempFile;

    @Before
    public void setUp() {
        listToPDF = new ListToPDF();
    }

    @After
    public void tearDown() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void testDoPrintToPdf_HappyPath() throws IOException {
        // Arrange
        tempFile = File.createTempFile("test_list_to_pdf", ".pdf");
        // Ensure the file doesn't exist before the test (createTempFile creates it, so delete it first)
        tempFile.delete();

        List<List> data = new ArrayList<>();
        data.add(Arrays.asList("Header1", "Header2"));
        data.add(Arrays.asList("Row1Col1", "Row1Col2"));
        data.add(Arrays.asList("Row2Col1", "Row2Col2"));

        // Act
        boolean result = listToPDF.doPrintToPdf(data, tempFile, ListToPDF.Orientation.PORTRAIT);

        // Assert
        assertTrue("Expected doPrintToPdf to return true", result);
        assertTrue("Expected PDF file to exist", tempFile.exists());
        assertTrue("Expected PDF file to have content", tempFile.length() > 0);
    }

    @Test
    public void testDoPrintToPdf_NullFile() {
        // Arrange
        List<List> data = new ArrayList<>();

        // Act
        boolean result = listToPDF.doPrintToPdf(data, null, ListToPDF.Orientation.PORTRAIT);

        // Assert
        assertFalse("Expected doPrintToPdf to return false for null file", result);
    }

    @Test
    public void testDoPrintToPdf_Landscape() throws IOException {
        // Arrange
        tempFile = File.createTempFile("test_list_to_pdf_landscape", ".pdf");
        tempFile.delete();

        List<List> data = new ArrayList<>();
        data.add(Arrays.asList("Header1", "Header2"));

        // Act
        boolean result = listToPDF.doPrintToPdf(data, tempFile, ListToPDF.Orientation.LANDSCAPE);

        // Assert
        assertTrue("Expected doPrintToPdf to return true for landscape", result);
        assertTrue("Expected PDF file to exist", tempFile.exists());
        assertTrue("Expected PDF file to have content", tempFile.length() > 0);
    }

    @Test
    public void testDoPrintToPdf_InvalidFile() {
        // Arrange
        List<List> data = new ArrayList<>();
        // Using a directory as a file path should throw an IOException (or similar) when trying to write to it
        // Depending on OS, this might behave differently, but let's try a directory that exists
        File invalidFile = new File(System.getProperty("java.io.tmpdir"));

        // However, ListToPDF appends .pdf if it doesn't end with .pdf
        // So "/tmp" becomes "/tmp.pdf". If /tmp is a directory, /tmp.pdf might be writeable if permission allows.

        // Let's try a file path that is definitely invalid/unwritable.
        // Or simply mock the IOException by triggering it via invalid input if possible.
        // But since we can't easily mock internal File operations without PowerMock,
        // we'll rely on the behavior of `doc.save(saveLoc)`.

        // If we provide a path to a directory, `doc.save()` should fail.
        // But `doPrintToPdf` checks `if (!saveLoc.getName().endsWith(".pdf"))` and appends `.pdf`.

        // So if I pass `new File("/tmp")`, it becomes `/tmp.pdf`.

        // Let's try passing a file in a non-existent directory.
        File invalidDir = new File("non_existent_directory_12345");
        File fileInInvalidDir = new File(invalidDir, "test.pdf");

        // Act
        boolean result = listToPDF.doPrintToPdf(data, fileInInvalidDir, ListToPDF.Orientation.PORTRAIT);

        // Assert
        // This should trigger IOException in doc.save() because directory doesn't exist
        // And catch block calls AlertMaker (stub) and returns false.
        assertFalse("Expected doPrintToPdf to return false when IOException occurs", result);
    }
}
