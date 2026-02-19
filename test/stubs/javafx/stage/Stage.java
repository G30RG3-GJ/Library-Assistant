package javafx.stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;
public class Stage extends Window {
    private List<Image> icons = new ArrayList<>();
    public Stage(StageStyle style) {}
    public Stage() {}
    public void setTitle(String title) {}
    public void setScene(Scene scene) {}
    public void show() {}
    public List<Image> getIcons() { return icons; }
}
