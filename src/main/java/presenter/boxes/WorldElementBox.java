package presenter.boxes;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.elements.WorldElement;



public class WorldElementBox extends Pane {

    public WorldElementBox(WorldElement element) {
        ImageView elementImageView = new ImageView(new Image(element.getImageSource()));

        elementImageView.fitWidthProperty().bind(this.widthProperty());
        elementImageView.fitHeightProperty().bind(this.heightProperty());

        this.getChildren().add(elementImageView);
    }

}
