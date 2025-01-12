package presenter;

import engine.SimulationEngine;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Configuration;
import model.Simulation;
import model.map.AbstractWorldMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationSimulationPresenter {
    private final SimulationEngine simulationEngine = new SimulationEngine();
    private final List<Stage> stagesList = new ArrayList<>();

    @FXML
    public Label informationLabel;

    @FXML
    public TextField mapWidthTextField;

    @FXML
    public TextField mapHeightTextField;

    @FXML
    public TextField startingGrassCountTextField;

    @FXML
    public TextField grassGrowthPerDayTextField;

    @FXML
    public TextField grassEnergyLevelTextField;

    @FXML
    public ComboBox animalTypeSelector;

    @FXML
    public TextField startingAnimalsCount;

    @FXML
    public TextField animalStartingEnergy;

    @FXML
    public TextField animalReadyToBreedEnergyLevelTextField;

    @FXML
    public TextField animalEnergyLossPerMoveTextField;

    @FXML
    public TextField animalEnergyGivenToChildTextField;

    @FXML
    public TextField genomeLengthTextField;

    @FXML
    public TextField minimalMutationsCountTextField;

    @FXML
    public TextField maximalMutationsCountTextField;

    @FXML
    public TextField simulationSpeed;

    @FXML
    public TextField totalSimulationDaysTextField;

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    private Simulation getSimulation(SimulationPresenter simulationPresenter) {
        Configuration configuration = getConfiguration();

        AbstractWorldMap map = configuration.getSelectedMap();

        Simulation simulation = new Simulation(map, configuration);

        simulationPresenter.init(map, simulation);

        return simulation;
    }

    public void onSimulationStartClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(getClass().getClassLoader().getResource("views/simulation.fxml"));

            BorderPane viewRoot = fxmlLoader.load();
            Stage stage = new Stage();

            configureStage(stage, viewRoot);

            SimulationPresenter simulationPresenter = fxmlLoader.getController();


            Simulation simulation = getSimulation(simulationPresenter);

            stage.setOnCloseRequest(event -> simulationEngine.stopSingleSimulation(simulation));

            simulationEngine.addSimulation(simulation);

            stage.setTitle(simulation.getSimulationId().toString());

            stagesList.add(stage);
            stage.setResizable(false);
            stage.show();

            notifyInfo("Successfully started simulation");
        } catch (NumberFormatException ex) {
            notifyError("Parsing configuration failed. Can't start simulation.");
            System.out.println("Parsing configuration failed. Can't start simulation: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Could not load fxml file: " + ex.getMessage());
            Platform.exit();
        } catch (IllegalArgumentException ex) {
            notifyError(ex.getMessage());
        }
    }


    public void onConfigurationSimulationApplicationClose() {
       simulationEngine.stopAllSimulations();
       simulationEngine.awaitSimulationsEnd();

       stagesList.forEach(Stage::close);
    }

    public void onMapSelected(ActionEvent actionEvent) {
    }

    public void onConfigurationSaveClick(ActionEvent actionEvent) {
    }

    public void onConfigurationDeleteClick(ActionEvent actionEvent) {
    }

    public void onConfigurationLoadClick(ActionEvent actionEvent) {
    }

    public void onAnimalSelect(ActionEvent actionEvent) {
    }

    private Configuration getConfiguration() throws NumberFormatException {
        Configuration configuration = new Configuration();

        // map configuration
        configuration.setMapWidth(Integer.parseInt(mapWidthTextField.getText()));
        configuration.setMapHeight(Integer.parseInt(mapHeightTextField.getText()));
        configuration.setStartingGrassCount(Integer.parseInt(startingGrassCountTextField.getText()));
        configuration.setGrassGrowthPerDay(Integer.parseInt(grassGrowthPerDayTextField.getText()));
        configuration.setGrassEnergyLevel(Integer.parseInt(grassEnergyLevelTextField.getText()));

        // animal configuration

        if (animalTypeSelector.getValue().equals("Normal")) {
            configuration.setAnimalType(Configuration.AnimalType.NORMAL);
        } else if (animalTypeSelector.getValue().equals("Aging")) {
            configuration.setAnimalType(Configuration.AnimalType.AGING);
        }

        configuration.setStartingAnimalsCount(Integer.parseInt(startingAnimalsCount.getText()));
        configuration.setAnimalStartingEnergy(Integer.parseInt(animalStartingEnergy.getText()));
        configuration.setAnimalReadyToBreedEnergyLevel(Integer.parseInt(animalReadyToBreedEnergyLevelTextField.getText()));
        configuration.setAnimalEnergyLossPerMove(Integer.parseInt(animalEnergyLossPerMoveTextField.getText()));
        configuration.setAnimalEnergyGivenToChild(Integer.parseInt(animalEnergyGivenToChildTextField.getText()));

        // genome configuration
        configuration.setGenomeLength(Integer.parseInt(genomeLengthTextField.getText()));
        configuration.setMinimalMutationsCount(Integer.parseInt(minimalMutationsCountTextField.getText()));
        configuration.setMaximalMutationsCount(Integer.parseInt(maximalMutationsCountTextField.getText()));

        // simulation configuration
        configuration.setSimulationSpeed(Integer.parseInt(simulationSpeed.getText()));
        configuration.setTotalSimulationDays(Integer.parseInt(totalSimulationDaysTextField.getText()));

        return configuration;
    }

    public void notifyError(String message) {
        informationLabel.setText(message);
        informationLabel.setTextFill(Color.RED);
    }

    public void notifyInfo(String message) {
        informationLabel.setText(message);
        informationLabel.setTextFill(Color.BLUE);
    }

}
