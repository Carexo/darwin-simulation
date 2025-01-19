package presenter.boxes;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import model.elements.MapDirection;
import model.elements.animal.AbstractAnimal;

public class AnimalBox extends Pane {
    private static final int COLUMNS  = 3;
    private static final int COUNT    = 3;

    private static final int OFFSET_X = 0;
    private static final int OFFSET_Y = 0;

    private static final int WIDTH    = 24;
    private static final int HEIGHT   = 32;

    public AnimalBox(AbstractAnimal animal, boolean tracked, int size) {
        MapDirection direction = animal.getDirection().shift(animal.getGenome().getActiveGene());

        int offsetDireciton = switch (direction) {
                case NORTH, NORTH_EAST, NORTH_WEST -> 0;
                case SOUTH, SOUTH_EAST, SOUTH_WEST -> 2;
                case EAST -> 1;
                case WEST -> 3;
            };

        ImageView imageView = new ImageView(animal.getImageSource());

        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());

        // Set the initial viewport
        imageView.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y  + offsetDireciton*HEIGHT, WIDTH, HEIGHT));


        // Create the timeline for the animation
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        final int[] index = {0};

        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(100),
                event -> {
                    int x = (index[0] % COLUMNS) * WIDTH + OFFSET_X;
                    int y = (index[0] / COLUMNS) * HEIGHT + OFFSET_Y + offsetDireciton*HEIGHT;
                    imageView.setViewport(new Rectangle2D(x, y, WIDTH, HEIGHT));
                    index[0] = (index[0] + 1) % COUNT;
                }
        );

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();


        ProgressBar hpBar = createHpBar(animal.getEnergyLevel(), size);
        StackPane stackPane = new StackPane(imageView, hpBar);

        // Add the HP bar to the pane
        this.getChildren().add(stackPane);

        if(tracked) {
            this.getStyleClass().add("selected-animal");
        }
    }

    private ProgressBar createHpBar (double animalHealth, int size) {
        ProgressBar hpBar = new ProgressBar();
        hpBar.setProgress(animalHealth); // Assuming energy level is out of 100
        hpBar.setMaxWidth(size * 0.8);
        hpBar.setStyle("-fx-accent: green;");
        hpBar.setTranslateY(-size * 0.4);
        hpBar.setMaxHeight(size * 0.2);
        return hpBar;
    }
}
