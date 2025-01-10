package presenter;


import engine.SimulationEngine;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import model.Configuration;
import model.Simulation;
import model.elements.Vector2D;
import model.elements.WorldElement;
import model.map.AbstractWorldMap;
import model.map.EarthMap;
import model.map.MapChangeListener;
import model.map.WorldMap;
import model.util.ConsoleMapDisplay;

import java.util.List;

public class SimulationPresenter implements MapChangeListener {
    @FXML
    public Label infoMove;

    @FXML
    public GridPane mapGrid;

    private AbstractWorldMap map;
    private SimulationEngine engine;

    public void setWorldMap(AbstractWorldMap map) {
        this.map = map;
    }

    public void drawMap() {
        xyLabel();
        xLabel();
        yLabel();
        drawElements();
    }

    public void xyLabel() {
        mapGrid.getColumnConstraints().add(new ColumnConstraints(30));
        mapGrid.getRowConstraints().add(new RowConstraints(30));
        Label label = new Label("y/x");
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);
    }

    private void xLabel() {
        for (int i = 0; i < map.getWidth(); i++) {
            Label label = new Label(Integer.toString(i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(30));
            mapGrid.add(label, i + 1, 0);
        }
    }

    private void yLabel() {
        for (int i = 0; i < map.getHeight(); i++) {
            Label label = new Label(Integer.toString(i ));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getRowConstraints().add(new RowConstraints(30));
            mapGrid.add(label, 0, i + 1);
        }
    }

    private void drawElements() {
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j =0; j < map.getHeight() - 1; j++) {
                Vector2D position = new Vector2D(i, j);
                if (map.isOccupied(position)) {
                    WorldElement element = map.objectsAt(position).toList().getFirst();
                    mapGrid.add(new Label(element.toString()), i + 1, j + 1);
                } else {
                    mapGrid.add(new Label(" "), i + 1, j + 1);
                }

                GridPane.setHalignment(mapGrid.getChildren().getLast(), HPos.CENTER);
            }
        }
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst()); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @Override
    public void mapChanged(WorldMap map, String message) {
        Platform.runLater(() -> {
            clearGrid();
            drawMap();
            infoMove.setText(message);
        });
    }


    public void onSimulationStartClicked(ActionEvent actionEvent) {
        Configuration config = new Configuration();
        EarthMap map = new EarthMap(config);

        Simulation simulation = new Simulation(map, config);

        setWorldMap(map);

        map.subscribe(new ConsoleMapDisplay());

        map.subscribe(this);

        engine = new SimulationEngine(List.of(simulation));

        new Thread(engine::runAsyncInThreadPool).start();
    }
}
