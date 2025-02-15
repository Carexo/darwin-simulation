package model.elements.animal;

import java.util.concurrent.ThreadLocalRandom;

public enum Gene {
    ROTATION_0,
    ROTATION_45,
    ROTATION_90,
    ROTATION_135,
    ROTATION_180,
    ROTATION_225,
    ROTATION_270,
    ROTATION_315;

    public Gene getNewRandom(){
        int randomGene = ThreadLocalRandom.current().nextInt(0, values().length);
        while(randomGene == this.ordinal())
            randomGene = ThreadLocalRandom.current().nextInt(0, values().length);

        return values()[randomGene];
    }

    public static Gene getRandom() {
        return values()[ThreadLocalRandom.current().nextInt(0, values().length)];
    }

    @Override
    public String toString() {
        return String.valueOf(ordinal());
    }
}
