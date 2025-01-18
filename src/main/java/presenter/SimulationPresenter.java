package presenter;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import model.simulation.Simulation;
import model.elements.Vector2D;
import model.elements.WorldElement;
import model.elements.animal.AbstractAnimal;
import model.map.AbstractWorldMap;
import model.map.MapChangeListener;
import model.map.WorldMap;
import model.simulation.StatisticSimulation;

import java.util.List;


public class SimulationPresenter implements MapChangeListener {
    @FXML
    public Label infoMove;

    @FXML
    public GridPane mapGrid;
    @FXML
    public Button controlButton;

    @FXML
    private InformationAnimal informationAnimalController;
    @FXML
    private DetailsSimulation detailsSimulationController;


    private AbstractWorldMap map;
    private Simulation simulation;

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
        for (int i = map.getHeight() - 1; i >= 0; i--) {
            Label label = new Label(Integer.toString(map.getHeight() - i - 1));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getRowConstraints().add(new RowConstraints(30));
            mapGrid.add(label, 0, i + 1);
        }
    }

    private void drawElements() {
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = map.getHeight() - 1; j >= 0; j--) {
                Vector2D position = new Vector2D(i, j);
                Label label = new Label(" ");
                label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Make label fill the cell
                label.setAlignment(Pos.CENTER);


                if (map.isOccupied(position)) {
                    WorldElement element = map.objectsAt(position).toList().getFirst();
                    List<AbstractAnimal> animals = map.getAnimals().get(position);

                    if (animals != null) {
                        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                handleAnimalClicked(animals);
                            }
                        });
                    }

                    label.setText(element.toString());
                    mapGrid.add(label, i + 1,  map.getHeight() - j);
                } else {
                    mapGrid.add(label, i + 1, map.getHeight() - j);
                }

                GridPane.setHalignment(mapGrid.getChildren().getLast(), HPos.CENTER);
            }
        }
    }

    public void handleAnimalClicked(List<AbstractAnimal> animals) {
        for (AbstractAnimal animal : animals) {
            informationAnimalController.setAnimal(animal);
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



    public void init(AbstractWorldMap map, Simulation simulation) {
        setWorldMap(map);
        this.simulation = simulation;

        detailsSimulationController.setStatisticSimulation(simulation.getStatisticSimulation());
        simulation.subscribe((simulation1) -> informationAnimalController.updateInformation(), Simulation.SimulationEventType.CHANGE);
        simulation.subscribe((simulation1) -> detailsSimulationController.onUpdateDetails(), Simulation.SimulationEventType.CHANGE);

//        map.subscribe(new ConsoleMapDisplay());

        map.subscribe(this);

    }

    public void onSimulationControlButtonClick(ActionEvent actionEvent) {
        if (simulation.isPaused()) {
            simulation.resume();
            controlButton.setText("Pause");
        } else {
            simulation.pause();
            controlButton.setText("Resume");
        }
    }

}
