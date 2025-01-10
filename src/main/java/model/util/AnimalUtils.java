package model.util;

import model.elements.animal.Animal;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class AnimalUtils {
    public static List<Animal> getBreedingAnimals(List<Animal> animals) {
        if (animals.size() < 2) {
            return List.of();
        }

        return animals.stream().filter(Animal::canBread).sorted(sortAnimal()).toList();
    }

    public static List<Animal> getDeathAnimals(List<Animal> animals) {
        return animals.stream().filter(Predicate.not(Animal::isAlive)).toList();
    }

    public static Animal getStrongestAnimal(List<Animal> animals) {
        return animals.stream().sorted(sortAnimal()).limit(1).toList().getFirst();
    }

    private static Comparator<Animal> sortAnimal() {
        return Comparator.comparing(Animal::getEnergyLevel).thenComparing(Animal::getAge).thenComparing(Animal::getChildrenCount).reversed();
    }
}
