package util;

import model.elements.Vector2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RandomPositionGenerator implements Iterable<Vector2D> {
    private final int maxWidth;
    private final int maxHeight;
    private final int count;
    private final List<Vector2D> positions;
    private List<Vector2D> exclude;

    public RandomPositionGenerator(int maxWidth, int maxHeight, int count) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.count = count;
        this.exclude = new ArrayList<>();

        this.positions = generateAllPosition();
        Collections.shuffle(positions);
    }

    public RandomPositionGenerator(int maxWidth, int maxHeight, int count, List<Vector2D> exclude) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.count = count;
        this.exclude = exclude;

        this.positions = generateAllPosition();

        Collections.shuffle(positions);
    }

    private List<Vector2D> generateAllPosition() {
        List<Vector2D> positions = new ArrayList<>();

        for (int x = 0; x < maxWidth; x++) {
            for (int y = 0; y < maxHeight; y++) {
                if(!exclude.contains(new Vector2D(x, y))) {
                    positions.add(new Vector2D(x, y));
                }

            }
        }

        return positions;
    }

    @Override
    public Iterator<Vector2D> iterator() {
        return new Iterator<Vector2D>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < count;
            }

            @Override
            public Vector2D next() {
                if (!hasNext()) {
                    throw new IllegalStateException("No more elements");
                }

                Vector2D position = positions.get(index);

                index++;
                return position;
            }
        };
    }
}
