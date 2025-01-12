package model.elements.animal;

import model.Configuration;
import model.elements.Vector2D;
import model.map.MoveValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {
    private AbstractAnimal animal;
    private Configuration configuration;
    private MoveValidator moveValidator;

    @BeforeEach
    void setUp() {
        configuration = new Configuration();
        configuration.setAnimalEnergyLossPerMove(10);
        configuration.setGrassEnergyLevel(20);
        configuration.setAnimalReadyToBreedEnergyLevel(50);
        configuration.setAnimalEnergyGivenToChild(30);
        configuration.setMinimalMutationsCount(1);
        configuration.setMaximalMutationsCount(3);
        configuration.setGenomeLength(8);

        animal = new Animal(new Vector2D(0, 0), 100, configuration);
        moveValidator = new MoveValidator() {
            @Override
            public boolean canMoveTo(Vector2D position) {
                return true;
            }

            @Override
            public Vector2D getNewPosition(Vector2D position) {
                return new Vector2D(1, 1);
            }
        };
    }

    @Test
    void testMove() {
        animal.move(moveValidator);

        assertEquals(new Vector2D(1, 1), animal.getPosition());
        assertEquals(90, animal.getEnergyLevel());
    }

    @Test
    void testEat() {
        animal.eat();
        assertEquals(120, animal.getEnergyLevel());
        assertEquals(1, animal.getEatenGrass());
    }

    @Test
    void testReproduce() {
        AbstractAnimal otherAnimal = new Animal(new Vector2D(0, 0), 100, configuration);

        AbstractAnimal child = animal.reproduce(otherAnimal);

        assertNotNull(child);
        assertEquals(70, animal.getEnergyLevel());
        assertEquals(70, otherAnimal.getEnergyLevel());
        assertEquals(1, animal.getChildrenCount());
    }

    @Test
    void testAging() {
        animal.aging();
        assertEquals(2, animal.getAge());
    }

    @Test
    void testDie() {
        animal.die(10);
        assertFalse(animal.isAlive());
        assertEquals(0, animal.getEnergyLevel());
    }

    @Test
    void testCanBread() {
        assertTrue(animal.canBread());
        animal = new Animal(new Vector2D(0, 0), 40, configuration);
        assertFalse(animal.canBread());
    }
}