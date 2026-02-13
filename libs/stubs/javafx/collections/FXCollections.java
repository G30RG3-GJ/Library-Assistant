package javafx.collections;

import java.util.ArrayList;

public class FXCollections {
    public static <E> ObservableList<E> observableArrayList() {
        return new ObservableListWrapper<>(new ArrayList<>());
    }

    private static class ObservableListWrapper<E> extends ArrayList<E> implements ObservableList<E> {
        public ObservableListWrapper(java.util.List<E> list) {
            super(list);
        }
    }
}
