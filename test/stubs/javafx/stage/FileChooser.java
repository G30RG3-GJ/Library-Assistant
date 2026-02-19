package javafx.stage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class FileChooser {
    public void setTitle(String title) {}
    public List<ExtensionFilter> getExtensionFilters() { return new ArrayList<>(); }
    public File showSaveDialog(Window ownerWindow) { return new File("dummy.pdf"); }
    public static class ExtensionFilter {
        public ExtensionFilter(String description, String... extensions) {}
    }
}
