package presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.elements.animal.Genome;
import model.simulation.StatisticSimulation;

public class DetailsSimulation {
    @FXML
    public Label dayNumber;
    @FXML
    private Label mostPopularGenome;
    @FXML
    private Label plantsCount;
    @FXML
    private Label animalsCount;
    @FXML
    private Label freePositionsCount;
    @FXML
    private Label averageAnimalEnergy;
    @FXML
    private Label averageDiedAnimalLifespan;
    @FXML
    private Label averageChildCount;


    private StatisticSimulation statisticSimulation;

    public void setStatisticSimulation(StatisticSimulation statisticSimulation) {
        this.statisticSimulation= statisticSimulation;
    }

    public void onUpdateDetails() {
        Platform.runLater(() -> {
            dayNumber.setText(String.valueOf(statisticSimulation.getDayNumber()));
            animalsCount.setText(String.valueOf(statisticSimulation.getAnimalsCount()));
            plantsCount.setText(String.valueOf(statisticSimulation.getPlantsCount()));
            freePositionsCount.setText(String.valueOf(statisticSimulation.getFreePositionsCount()));
            averageAnimalEnergy.setText(String.format("%.2f", statisticSimulation.getAverageAnimalEnergy()));
            averageDiedAnimalLifespan.setText(String.format("%.2f", statisticSimulation.getAverageDiedAnimalLifespan()));
            averageChildCount.setText(String.format("%.2f", statisticSimulation.getAverageChildCount()));
            mostPopularGenome.setText(statisticSimulation.getMostPopularGenome().map(Genome::toString).orElse("No animals"));
        });
    }
}
