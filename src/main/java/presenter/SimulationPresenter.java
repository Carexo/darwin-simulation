package presenter;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import model.simulation.Simulation;
import model.elements.Vector2D;
import model.elements.WorldElement;
import model.elements.animal.AbstractAnimal;
import model.map.AbstractWorldMap;
import presenter.boxes.AxisLabel;
import presenter.boxes.WorldElementBoxFactory;

import java.util.HashSet;


public class SimulationPresenter {
    private static final int MAX_CELL_SIZE = 50;
    private static final int MIN_CELL_SIZE = 3;

    @FXML
    public GridPane mapGrid;
    @FXML
    public Button controlButton;
    @FXML
    public Label simulationInfo;
    @FXML
    public Button showPopularGenome;
    @FXML
    public Button showPlantsPosition;

    @FXML
    private InformationAnimal informationAnimalController;
    @FXML
    private DetailsSimulation detailsSimulationController;
    private boolean showAnimalsWithPopularGenome = false;

    private AbstractWorldMap map;
    private Simulation simulation;
    private WorldElementBoxFactory worldElementBoxFactory;
    private int cellSize = 30;
    private HashSet<AbstractAnimal> animalsWithPopularGenome = new HashSet<>();

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
        mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
        Label label = new AxisLabel("y/x", cellSize);
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);
    }

    private void xLabel() {
        for (int i = 0; i < map.getWidth(); i++) {
            Label label = new AxisLabel(Integer.toString(i), cellSize);
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
            mapGrid.add(label, i + 1, 0);
        }
    }

    private void yLabel() {
        for (int i = map.getHeight() - 1; i >= 0; i--) {
            Label label = new AxisLabel(Integer.toString(map.getHeight() - i - 1), cellSize);
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
            mapGrid.add(label, 0, i + 1);
        }
    }

    private void drawElements() {
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = map.getHeight() - 1; j >= 0; j--) {
                Vector2D position = new Vector2D(i, j);

                if (map.isOccupied(position)) {
                    WorldElement element = map.objectsAt(position).toList().getFirst();

                    if (element instanceof AbstractAnimal animal) {
                        boolean isTracked = informationAnimalController.getSelectedAnimal().map(animal::equals).orElse(false) || (showAnimalsWithPopularGenome && animalsWithPopularGenome.contains(animal));
                        Pane pane = worldElementBoxFactory.createAnimalBox(animal, isTracked);
                        mapGrid.add(pane, i + 1,  map.getHeight() - j);


                        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                handleAnimalClicked(animal);
                            }
                        });

                    } else {
                        Pane pane = worldElementBoxFactory.createWorldElementBox(element);
                        mapGrid.add(pane, i + 1,  map.getHeight() - j);
                    }

                } else {
                    mapGrid.add(new Label(" "), i + 1, map.getHeight() - j);
                }

                GridPane.setHalignment(mapGrid.getChildren().getLast(), HPos.CENTER);
            }
        }
    }

    public void handleAnimalClicked(AbstractAnimal animal) {
        informationAnimalController.setAnimal(animal);
        mapChanged();
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    public void mapChanged() {
        Platform.runLater(() -> {
            clearGrid();
            drawMap();
        });
    }


    public void init(AbstractWorldMap map, Simulation simulation) {
        setWorldMap(map);
        this.simulation = simulation;
        this.worldElementBoxFactory = new WorldElementBoxFactory(cellSize);

        detailsSimulationController.setStatisticSimulation(simulation.getStatisticSimulation());

        simulation.subscribe((simulation1) -> informationAnimalController.updateInformation(), Simulation.SimulationEventType.CHANGE);
        simulation.subscribe((simulation1) -> detailsSimulationController.onUpdateDetails(), Simulation.SimulationEventType.CHANGE);
        simulation.subscribe((simulation1) -> mapChanged(), Simulation.SimulationEventType.CHANGE);
        simulation.subscribe((simulation1) -> changeSimulationInfo("Simulation ended"), Simulation.SimulationEventType.END);
        simulation.subscribe((simulation1) -> {
            changeSimulationInfo("Simulation paused");
            showPopularGenome.setDisable(false);
            showPlantsPosition.setDisable(false);

        }, Simulation.SimulationEventType.PAUSED);
        simulation.subscribe((simulation1) -> {
            changeSimulationInfo("Simulation is running");
            showPopularGenome.setDisable(true);
            showPlantsPosition.setDisable(true);
            showAnimalsWithPopularGenome = false;
        }, Simulation.SimulationEventType.RESUME);
    }

    public void calculateCellSize(double windowWidth, double windowHeight) {
        int mapWidth = map.getWidth();
        int mapHeight = map.getHeight();

        cellSize = Math.max(Math.min(Math.min((int) Math.round((windowWidth*0.85 / (mapWidth))), (int) Math.round((windowHeight*0.85 / (mapHeight)))), MAX_CELL_SIZE), MIN_CELL_SIZE);

        worldElementBoxFactory.setSize(cellSize);
        mapChanged();
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

    public void onShowPopularGenomeClick(ActionEvent actionEvent) {
        showAnimalsWithPopularGenome = !showAnimalsWithPopularGenome;
        if (showAnimalsWithPopularGenome) {
            informationAnimalController.unselectAnimal();
           animalsWithPopularGenome = simulation.getStatisticSimulation().getAnimalsWithMostPopularGenome();
        }
        mapChanged();
    }

    public void onShowPlantsPreferredPositionClick(ActionEvent actionEvent) {
//        detailsSimulationController.showPlantsPreferredPosition();
    }

    public void changeSimulationInfo(String message) {
        Platform.runLater(() -> {
            simulationInfo.setText(message);
        });
    }

}
