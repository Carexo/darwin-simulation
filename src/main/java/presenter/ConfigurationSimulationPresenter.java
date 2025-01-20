package presenter;

import engine.SimulationEngine;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Configuration;
import model.simulation.Simulation;
import model.map.AbstractWorldMap;
import util.CSVWriter;
import util.ConfigurationWriter;
import util.ValidationConfigurationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationSimulationPresenter {
    private final SimulationEngine simulationEngine = new SimulationEngine();
    private final List<Stage> stagesList = new ArrayList<>();

    @FXML
    public Label informationLabel;

    @FXML
    public TextField mapWidth;

    @FXML
    public TextField mapHeight;

    @FXML
    public TextField startingGrassCount;

    @FXML
    public TextField grassGrowthPerDay;

    @FXML
    public TextField grassEnergyLevel;

    @FXML
    public ComboBox<String> animalTypeSelector;

    @FXML
    public TextField startingAnimalsCount;

    @FXML
    public TextField animalStartingEnergy;

    @FXML
    public TextField animalReadyToBreedEnergyLevel;

    @FXML
    public TextField animalEnergyLossPerMove;

    @FXML
    public TextField animalEnergyGivenToChild;

    @FXML
    public TextField genomeLength;

    @FXML
    public TextField minimalMutationsCount;

    @FXML
    public TextField maximalMutationsCount;

    @FXML
    public TextField simulationSpeed;

    @FXML
    public TextField totalSimulationDays;

    @FXML
    public ComboBox<String> mapTypeSelector;
    @FXML
    public CheckBox csvStatisticSaving;
    @FXML
    public Pane mapPropertiesPane;
    @FXML
    public Pane animalPropertiesPane;
    @FXML
    public TextField chanceOfAnimalSkipMove;
    @FXML
    public TextField startingOceanCount;
    @FXML
    public TextField waterSegments;
    @FXML
    public TextField oceanChangeRate;
    @FXML
    public TextField configurationName;
    @FXML
    public ListView<String> configurationsListView;

    @FXML
    private void initialize() {
        try {
            ConfigurationWriter.loadConfigurationsFromFile();
            configurationsListView.getItems().addAll(ConfigurationWriter.getConfigurationNames());

        } catch (Exception ex) {
            notifyError("Failed to load configurations");
            System.out.printf("Failed to load configurations with error: %s\n", ex.getMessage());
        } finally {
            if(configurationsListView.getItems().isEmpty()) {
                notifyInfo("Loaded default configuration");
                setConfigurationForm(new Configuration());
            }
            else {
                setConfigurationForm(ConfigurationWriter.getConfiguration(configurationsListView.getItems().getFirst()));
                notifyInfo("Successfully one of the configurations");
            }
        }
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    private Simulation getSimulation(SimulationPresenter simulationPresenter) {
        Configuration configuration = getConfiguration();
        Configuration.validate(configuration);

        AbstractWorldMap map = configuration.getSelectedMap();

        Simulation simulation = new Simulation(map, configuration);

        if (configuration.isCsvStatisticSaving()) {
            simulation.subscribe(new CSVWriter(), Simulation.SimulationEventType.CHANGE);
        }

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

            simulationPresenter.calculateCellSize(stage.getWidth(), stage.getHeight());

            stage.widthProperty().addListener((obs, oldVal, newVal) -> {
                simulationPresenter.calculateCellSize(newVal.doubleValue(), stage.getHeight());
            });

            stage.heightProperty().addListener((obs, oldVal, newVal) -> {
                simulationPresenter.calculateCellSize(stage.getWidth(), newVal.doubleValue());
            });

            simulationEngine.addSimulation(simulation);

            stage.setTitle(simulation.getSimulationId().toString());

            stagesList.add(stage);
            stage.show();

            notifyInfo("Successfully started simulation");
        } catch (NumberFormatException ex) {
            notifyError("Parsing configuration failed. Can't start simulation.");
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
//            System.out.println("Could not load fxml file: " + ex.getMessage());
            Platform.exit();
        } catch (IllegalArgumentException | ValidationConfigurationException ex) {
            notifyError(ex.getMessage());
        }
    }


    public void onConfigurationSimulationApplicationClose() {
       simulationEngine.stopAllSimulations();
       simulationEngine.awaitSimulationsEnd();

       stagesList.forEach(Stage::close);
    }

    public void onMapSelected(ActionEvent actionEvent) {
        if (mapTypeSelector.getValue().equals("Earth")) {
            mapPropertiesPane.setDisable(true);
        } else if (mapTypeSelector.getValue().equals("Ocean")) {
            mapPropertiesPane.setDisable(false);
        }
    }


    public void onAnimalSelect(ActionEvent actionEvent) {
        if (animalTypeSelector.getValue().equals("Normal")) {
            animalPropertiesPane.setDisable(true);
        } else if (animalTypeSelector.getValue().equals("Aging")) {
            animalPropertiesPane.setDisable(false);
        }
    }

    public void onConfigurationSaveClick() {
        if(configurationName.getText().isEmpty()) {
            notifyError("Please enter configuration name");
            return;
        }

        try {
            Configuration configuration = getConfiguration();
            Configuration.validate(configuration);

            ConfigurationWriter.addConfiguration(configurationName.getText(), configuration);
            configurationsListView.getItems().add(configurationName.getText());

            ConfigurationWriter.saveConfigurationToFiles();

            notifyInfo("Successfully saved configuration");
        } catch(NumberFormatException ex) {
            notifyError("Please enter correct data");
        } catch(IllegalArgumentException | ValidationConfigurationException ex) {
            notifyError(ex.getMessage());
        } catch (IOException ex) {
            notifyError("Failed to save configurations");
            System.out.println("Failed to save configurations with error: " + ex.getMessage());
        }
    }

    public void onConfigurationDeleteClick() {
        if(configurationsListView.getSelectionModel().getSelectedItems().isEmpty()) {
            notifyError("Please select configuration to delete");
            return;
        }

        String selectedConfiguration = configurationsListView.getSelectionModel().getSelectedItems().getFirst();

        try {
            ConfigurationWriter.removeConfiguration(selectedConfiguration);
            configurationsListView.getItems().remove(selectedConfiguration);

            ConfigurationWriter.saveConfigurationToFiles();

            notifyInfo("Successfully deleted configuration");
        } catch(IllegalArgumentException ex) {
            notifyError(ex.getMessage());
        } catch (IOException ex) {
            notifyError("Failed to save configurations");
            System.out.println("Failed to save configurations with error: " + ex.getMessage());
        }
    }

    public void onConfigurationLoadClick() {
        if(configurationsListView.getSelectionModel().getSelectedItems().isEmpty()) {
            notifyError("Please select configuration to load");
            return;
        }

        try {
            String selectedConfiguration = configurationsListView.getSelectionModel().getSelectedItems().getFirst();

            Configuration configuration = ConfigurationWriter.getConfiguration(selectedConfiguration);
            setConfigurationForm(configuration);
            notifyInfo("Successfully loaded configuration");

        } catch (Exception ex) {
            notifyError("Failed to load configuration");
            System.out.printf("Failed to load configuration with error: %s\n", ex.getMessage());
        }
    }

    private void setConfigurationForm(Configuration configuration) {
        // Map configuration
        mapWidth.setText(String.valueOf(configuration.getMapWidth()));
        mapHeight.setText(String.valueOf(configuration.getMapHeight()));
        startingGrassCount.setText(String.valueOf(configuration.getStartingGrassCount()));
        grassGrowthPerDay.setText(String.valueOf(configuration.getGrassGrowthPerDay()));
        grassEnergyLevel.setText(String.valueOf(configuration.getGrassEnergyLevel()));

        if (configuration.getMapType() == Configuration.MapType.EARTH_MAP) {
            mapTypeSelector.setValue("Earth");
            mapPropertiesPane.setDisable(true);
        } else if (configuration.getMapType() == Configuration.MapType.OCEAN_MAP) {
            mapTypeSelector.setValue("Ocean");
            mapPropertiesPane.setDisable(false);
            startingOceanCount.setText(String.valueOf(configuration.getStartingOceanCount()));
            waterSegments.setText(String.valueOf(configuration.getWaterSegments()));
            oceanChangeRate.setText(String.valueOf(configuration.getOceanChangeRate()));
        }

        // Animal configuration
        if (configuration.getAnimalType() == Configuration.AnimalType.NORMAL) {
            animalTypeSelector.setValue("Normal");
            animalPropertiesPane.setDisable(true);
        } else if (configuration.getAnimalType() == Configuration.AnimalType.AGING) {
            animalTypeSelector.setValue("Aging");
            animalPropertiesPane.setDisable(false);
            chanceOfAnimalSkipMove.setText(String.valueOf(configuration.getChanceOfAnimalSkipMove()));
        }

        startingAnimalsCount.setText(String.valueOf(configuration.getStartingAnimalsCount()));
        animalStartingEnergy.setText(String.valueOf(configuration.getAnimalStartingEnergy()));
        animalReadyToBreedEnergyLevel.setText(String.valueOf(configuration.getAnimalReadyToBreedEnergyLevel()));
        animalEnergyLossPerMove.setText(String.valueOf(configuration.getAnimalEnergyLossPerMove()));
        animalEnergyGivenToChild.setText(String.valueOf(configuration.getAnimalEnergyGivenToChild()));

        // Genome configuration
        genomeLength.setText(String.valueOf(configuration.getGenomeLength()));
        minimalMutationsCount.setText(String.valueOf(configuration.getMinimalMutationsCount()));
        maximalMutationsCount.setText(String.valueOf(configuration.getMaximalMutationsCount()));

        // Simulation configuration
        simulationSpeed.setText(String.valueOf(configuration.getSimulationSpeed()));
        totalSimulationDays.setText(String.valueOf(configuration.getTotalSimulationDays()));
        csvStatisticSaving.setSelected(configuration.isCsvStatisticSaving());
    }

    private Configuration getConfiguration() throws NumberFormatException {
        Configuration configuration = new Configuration();

        //  map configuration
        configuration.setMapWidth(Integer.parseInt(mapWidth.getText()));
        configuration.setMapHeight(Integer.parseInt(mapHeight.getText()));
        configuration.setStartingGrassCount(Integer.parseInt(startingGrassCount.getText()));
        configuration.setGrassGrowthPerDay(Integer.parseInt(grassGrowthPerDay.getText()));
        configuration.setGrassEnergyLevel(Integer.parseInt(grassEnergyLevel.getText()));


        if (mapTypeSelector.getValue().equals("Earth")) {
            configuration.setMapType(Configuration.MapType.EARTH_MAP);
        } else if (mapTypeSelector.getValue().equals("Ocean")) {
            configuration.setMapType(Configuration.MapType.OCEAN_MAP);
            configuration.setStartingOceanCount(Integer.parseInt(startingOceanCount.getText()));
            configuration.setWaterSegments(Integer.parseInt(waterSegments.getText()));
            configuration.setOceanChangeRate(Integer.parseInt(oceanChangeRate.getText()));
        }

        // animal configuration
        if (animalTypeSelector.getValue().equals("Normal")) {
            configuration.setAnimalType(Configuration.AnimalType.NORMAL);
        } else if (animalTypeSelector.getValue().equals("Aging")) {
            configuration.setAnimalType(Configuration.AnimalType.AGING);
            configuration.setChanceOfAnimalSkipMove(Integer.parseInt(chanceOfAnimalSkipMove.getText()));
        }

        configuration.setStartingAnimalsCount(Integer.parseInt(startingAnimalsCount.getText()));
        configuration.setAnimalStartingEnergy(Integer.parseInt(animalStartingEnergy.getText()));
        configuration.setAnimalReadyToBreedEnergyLevel(Integer.parseInt(animalReadyToBreedEnergyLevel.getText()));
        configuration.setAnimalEnergyLossPerMove(Integer.parseInt(animalEnergyLossPerMove.getText()));
        configuration.setAnimalEnergyGivenToChild(Integer.parseInt(animalEnergyGivenToChild.getText()));

        // genome configuration
        configuration.setGenomeLength(Integer.parseInt(genomeLength.getText()));
        configuration.setMinimalMutationsCount(Integer.parseInt(minimalMutationsCount.getText()));
        configuration.setMaximalMutationsCount(Integer.parseInt(maximalMutationsCount.getText()));

        // simulation configuration
        configuration.setSimulationSpeed(Integer.parseInt(simulationSpeed.getText()));
        configuration.setTotalSimulationDays(Integer.parseInt(totalSimulationDays.getText()));

        // simulation configuration
        configuration.setCsvStatisticSaving(csvStatisticSaving.isSelected());

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
