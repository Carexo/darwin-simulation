package presenter.boxes;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.elements.WorldElement;
import model.elements.animal.AbstractAnimal;

public class WorldElementBoxFactory {
    private int size;

    public WorldElementBoxFactory(int size) {
        this.size = size;
    }

    private Rectangle createRectangleWithBackground(Color color) {
        return new Rectangle(size, size, color);
    }

    public Pane createAnimalBox(AbstractAnimal animal, boolean tracked) {
        if (size < 20) {
            Pane pane = new Pane();
            Rectangle rectangle = createRectangleWithBackground(animal.getColor());
            if (tracked) {
                rectangle.getStyleClass().add("rectangle-border");
            }

            pane.getChildren().add(rectangle);
            return pane;
        }

        return new AnimalBox(animal, tracked);
    }

    public Pane createWorldElementBox(WorldElement element) {
        if (size < 20) {
            Pane pane = new Pane();
            pane.getChildren().add(createRectangleWithBackground(element.getColor()));
            return pane;
        }

        return new WorldElementBox(element);
    }

    public void setSize(int size) {
        this.size = size;
    }
}
