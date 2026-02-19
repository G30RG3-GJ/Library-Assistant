package javafx.collections;

import java.util.ArrayList;

public class FXCollections {
    public static <E> ObservableList<E> observableArrayList() {
        return new ObservableArrayListStub<>();
    }

    private static class ObservableArrayListStub<E> extends ArrayList<E> implements ObservableList<E> {
    }
}
