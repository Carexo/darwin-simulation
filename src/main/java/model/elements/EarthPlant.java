package model.elements;

public class EarthPlant extends Plant {
    private final boolean preferredPosition;

    public EarthPlant(Vector2D pos, boolean preferredPosition) {
        super(pos);
        this.preferredPosition = preferredPosition;
    }
    public boolean isPreferredPosition() {
        return this.preferredPosition;
    }

}
