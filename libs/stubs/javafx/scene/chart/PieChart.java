package javafx.scene.chart;

public class PieChart {
    public static class Data {
        private String name;
        private double pieValue;

        public Data(String name, double pieValue) {
            this.name = name;
            this.pieValue = pieValue;
        }

        public String getName() {
            return name;
        }

        public double getPieValue() {
            return pieValue;
        }
    }
}
