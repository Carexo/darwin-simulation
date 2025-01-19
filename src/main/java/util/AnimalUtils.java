package util;

import model.elements.animal.AbstractAnimal;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class AnimalUtils {
    public static List<AbstractAnimal> getBreedingAnimals(List<AbstractAnimal> animals) {
        if (animals.size() < 2) {
            return List.of();
        }

        return animals.stream().filter(AbstractAnimal::canBread).sorted(sortAnimal()).toList();
    }

    public static List<AbstractAnimal> getDeathAnimals(List<AbstractAnimal> animals) {
        return animals.stream().filter(Predicate.not(AbstractAnimal::isAlive)).toList();
    }

    public static AbstractAnimal getStrongestAnimal(List<AbstractAnimal> animals) {
        return animals.stream().sorted(sortAnimal()).limit(1).toList().getFirst();
    }

    private static Comparator<AbstractAnimal> sortAnimal() {
        return Comparator.comparing(AbstractAnimal::getEnergyLevel).thenComparing(AbstractAnimal::getAge).thenComparing(AbstractAnimal::getChildrenCount).reversed();
    }
}
