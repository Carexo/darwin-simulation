package presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import model.elements.animal.AbstractAnimal;
import model.elements.animal.Gene;
import model.elements.animal.Genome;

public class InformationAnimal {
    @FXML
    public Label energyLevel;
    @FXML
    public Label eatenPlants;
    @FXML
    public Label childrenCount;
    @FXML
    public Label descendantCount;
    @FXML
    public ListView<Text> genome;
    @FXML
    public Label aliveStatusTitle;
    @FXML
    public Label aliveStatus;

    private AbstractAnimal selectedAnimal;
    private int lastGeneIndex = -1;

    public void setAnimal(AbstractAnimal selectedAnimal) {
        this.selectedAnimal = selectedAnimal;
        clearInformation();

        Genome animalGenome = selectedAnimal.getGenome();

        genome.getItems().addAll(animalGenome.getGenes().stream().map(Gene::toString).map(Text::new).toList());
        lastGeneIndex = animalGenome.getIndexActiveGene();

        updateInformation();
    }

    public void updateInformation() {
        if (selectedAnimal == null) {
            return;
        }


        Genome animalGenome = selectedAnimal.getGenome();

        Platform.runLater(() -> {
            energyLevel.setText(String.valueOf(selectedAnimal.getEnergyLevel()));
            eatenPlants.setText(String.valueOf(selectedAnimal.getEatenGrass()));
            childrenCount.setText(String.valueOf(selectedAnimal.getChildrenCount()));
            descendantCount.setText(String.valueOf(selectedAnimal.getDescendantCount()));


            genome.getItems().get(lastGeneIndex).getStyleClass().clear();
            genome.getItems().get(animalGenome.getIndexActiveGene()).getStyleClass().add("current-gene");
            lastGeneIndex = animalGenome.getIndexActiveGene();

            genome.scrollTo(Math.max(0, animalGenome.getIndexActiveGene()));

            if (selectedAnimal.getDiedAt() != -1) {
                aliveStatusTitle.setText("Died at:");
                aliveStatus.setText(String.valueOf(selectedAnimal.getDiedAt()));
            } else {
                aliveStatusTitle.setText("Age:");
                aliveStatus.setText(String.valueOf(selectedAnimal.getAge()));
            }
        });
    }

    private void clearInformation() {
        genome.getItems().clear();
        energyLevel.setText("");
        eatenPlants.setText("");
        childrenCount.setText("");
        descendantCount.setText("");
        aliveStatus.setText("");
        aliveStatusTitle.setText("Age:");
    }
}
