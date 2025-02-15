package util;


import model.elements.Vector2D;
import model.elements.WorldElement;
import model.map.WorldMap;

import java.util.stream.Stream;

/**
 * The map visualizer converts the {@link WorldMap} map into a string
 * representation.
 *
 * @author apohllo, idzik
 */
public class MapVisualizer {
    private static final String EMPTY_CELL = " ";
    private static final String FRAME_SEGMENT = "-";
    private static final String CELL_SEGMENT = "|";
    private final WorldMap map;

    /**
     * Initializes the MapVisualizer with an instance of map to visualize.
     *
     * @param map
     */
    public MapVisualizer(WorldMap map) {
        this.map = map;
    }

    /**
     * Convert selected region of the map into a string. It is assumed that the
     * indices of the map will have no more than two characters (including the
     * sign).
     *
     * @param lowerLeft  The lower left corner of the region that is drawn.
     * @param upperRight The upper right corner of the region that is drawn.
     * @return String representation of the selected region of the map.
     */
    public String draw(Vector2D lowerLeft, Vector2D upperRight) {
        StringBuilder builder = new StringBuilder();
        for (int i = upperRight.y() + 1; i >= lowerLeft.y() - 1; i--) {
            if (i == upperRight.y() + 1) {
                builder.append(drawHeader(lowerLeft, upperRight));
            }
            builder.append(String.format("%3d: ", i));
            for (int j = lowerLeft.x(); j <= upperRight.x() + 1; j++) {
                if (i < lowerLeft.y() || i > upperRight.y()) {
                    builder.append(drawFrame(j <= upperRight.x()));
                } else {
                    builder.append(CELL_SEGMENT);
                    if (j <= upperRight.x()) {
                        builder.append(drawObject(new Vector2D(j, i)));
                    }
                }
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

    private String drawFrame(boolean innerSegment) {
        if (innerSegment) {
            return FRAME_SEGMENT + FRAME_SEGMENT;
        } else {
            return FRAME_SEGMENT;
        }
    }

    private String drawHeader(Vector2D lowerLeft, Vector2D upperRight) {
        StringBuilder builder = new StringBuilder();
        builder.append(" y\\x ");
        for (int j = lowerLeft.x(); j < upperRight.x() + 1; j++) {
            builder.append(String.format("%2d", j));
        }
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    private String drawObject(Vector2D currentPosition) {
        if (this.map.isOccupied(currentPosition)) {
            Stream<WorldElement> elements = this.map.objectsAt(currentPosition);
            return elements.findFirst().map(Object::toString).orElse(EMPTY_CELL);
        }
        return EMPTY_CELL;
    }
}
