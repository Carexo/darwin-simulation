package presenter.boxes;

import javafx.scene.control.Label;

public class AxisLabel extends Label {
    public AxisLabel(String text, int cellSize) {
        super(text);
        double fontSize = cellSize * 0.6;

        this.setStyle("-fx-font-size: " + fontSize + "px;");
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        this.getStyleClass().add("axis-label");
    }
}
