package model;

import model.map.TidesMap;
import model.map.AbstractWorldMap;
import model.map.EarthMap;
import util.ValidationConfigurationException;

public class Configuration {
    public enum MapType {
        EARTH_MAP ,
        OCEAN_MAP
    }

    public enum AnimalType {
        NORMAL,
        AGING
    }

    // map
    private MapType mapType = MapType.EARTH_MAP;
    private int mapWidth = 10;
    private int mapHeight = 10;
    private int startingGrassCount = 20;
    private int grassGrowthPerDay = 5;
    private int grassEnergyLevel = 10;

    // ocean map
    private int startingOceanCount = 4;
    private int oceanChangeRate = 10;
    private int waterSegments = 3;

    // animal
    private int startingAnimalsCount = 10;
    private int animalStartingEnergy = 106;
    private int animalEnergyLossPerMove = 1;
    private int animalReadyToBreedEnergyLevel = 50;
    private int animalEnergyGivenToChild = 20;
    private AnimalType animalType = AnimalType.NORMAL;
    private int genomeLength = 8;
    private int minimalMutationsCount = 1;
    private int maximalMutationsCount = 3;


    // simulation
    private int simulationSpeed = 500;
    private int totalSimulationDays = Integer.MAX_VALUE;
    private double chanceOfAnimalSkipMove = 0.1;
    private boolean csvStatisticSaving = false;

    public static void validate(Configuration configuration) throws ValidationConfigurationException {
        // map validation
        int mapSize = configuration.getMapWidth() * configuration.getMapHeight();
        if (configuration.getMapWidth() <= 0) {
            throw new ValidationConfigurationException("Map width must be positive.");
        }
        if (configuration.getMapHeight() <= 0) {
            throw new ValidationConfigurationException("Map height must be positive.");
        }
        if (configuration.getMapWidth() > 100) {
            throw new ValidationConfigurationException("Map width cannot be greater than 100.");
        }
        if (configuration.getMapHeight() > 100) {
            throw new ValidationConfigurationException("Map height cannot be greater than 100.");
        }
        if (configuration.getStartingGrassCount() < 0) {
            throw new ValidationConfigurationException("Starting grass count cannot be negative.");
        }
        if (configuration.getStartingGrassCount() > mapSize) {
            throw new ValidationConfigurationException("Starting grass count cannot be greater than map size.");
        }
        if (configuration.getStartingOceanCount() > mapSize - configuration.getStartingAnimalsCount()) {
            throw new ValidationConfigurationException("Starting ocean count cannot be greater than map size - starting animals count.");
        }
        if (configuration.getStartingOceanCount() < 0) {
            throw new ValidationConfigurationException("Starting ocean count cannot be negative.");
        }
        if (configuration.getGrassGrowthPerDay() < 0) {
            throw new ValidationConfigurationException("Grass growth per day cannot be negative.");
        }
        if (configuration.getGrassEnergyLevel() < 0) {
            throw new ValidationConfigurationException("Grass energy level cannot be negative.");
        }
        if (configuration.getWaterSegments() < 0) {
            throw new ValidationConfigurationException("Water segments cannot be negative.");
        }
        if (configuration.getOceanChangeRate() < 0) {
            throw new ValidationConfigurationException("Ocean change rate cannot be negative.");
        }


        // animal validation
        if (configuration.getStartingAnimalsCount() < 0) {
            throw new ValidationConfigurationException("Starting animals count cannot be negative.");
        }
        if (configuration.getStartingAnimalsCount() > mapSize) {
            throw new ValidationConfigurationException("Starting animals count cannot be greater than map size.");
        }
        if (configuration.getAnimalStartingEnergy() < 0) {
            throw new ValidationConfigurationException("Animal starting energy cannot be negative.");
        }
        if (configuration.getAnimalEnergyLossPerMove() < 0) {
            throw new ValidationConfigurationException("Animal energy loss per move cannot be negative.");
        }
        if (configuration.getAnimalReadyToBreedEnergyLevel() < 0) {
            throw new ValidationConfigurationException("Animal ready to breed energy level cannot be negative.");
        }
        if (configuration.getAnimalEnergyGivenToChild() < 0) {
            throw new ValidationConfigurationException("Animal energy given to child cannot be negative.");
        }
        if (configuration.getAnimalReadyToBreedEnergyLevel() < configuration.getAnimalEnergyGivenToChild()) {
            throw new ValidationConfigurationException("Animal ready to breed energy level must be greater than energy given to child.");
        }
        if (configuration.getGenomeLength() <= 0) {
            throw new ValidationConfigurationException("Genome length must be positive.");
        }
        if (configuration.getMinimalMutationsCount() < 0) {
            throw new ValidationConfigurationException("Minimal mutations count cannot be negative.");
        }
        if (configuration.getMaximalMutationsCount() < 0) {
            throw new ValidationConfigurationException("Maximal mutations count cannot be negative.");
        }
        if (configuration.getMaximalMutationsCount() < configuration.getMinimalMutationsCount()) {
            throw new ValidationConfigurationException("Maximal mutations count must be greater than minimal mutations count.");
        }
        if (configuration.getSimulationSpeed() < 50) {
            throw new ValidationConfigurationException("Simulation speed must be greater than 100 ms.");
        }
        if (configuration.getTotalSimulationDays() <= 0) {
            throw new ValidationConfigurationException("Total simulation days must be positive.");
        }
        if (configuration.getChanceOfAnimalSkipMove() < 0 || configuration.getChanceOfAnimalSkipMove() >= 100) {
            throw new ValidationConfigurationException("Chance of animal skip move must be in range [0, 100).");
        }
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }

    public int getSimulationSpeed() {
        return simulationSpeed;
    }

    public void setSimulationSpeed(int simulationSpeed) {
        this.simulationSpeed = simulationSpeed;
    }

    public boolean isCsvStatisticSaving() {
        return csvStatisticSaving;
    }

    public void setCsvStatisticSaving(boolean csvStatisticSaving) {
        this.csvStatisticSaving = csvStatisticSaving;
    }

    public AbstractWorldMap getSelectedMap() {
        return switch(mapType){
            case EARTH_MAP -> new EarthMap(this);
            case OCEAN_MAP -> new TidesMap(this);
        };
    }

    public double getChanceOfAnimalSkipMove() {
        return chanceOfAnimalSkipMove;
    }

    public void setChanceOfAnimalSkipMove(double chanceOfAnimalSkipMove) {
        this.chanceOfAnimalSkipMove = chanceOfAnimalSkipMove;
    }

    public MapType getMapType() {
        return mapType;
    }

    public void setMapType(MapType mapType) {
        this.mapType = mapType;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public int getStartingGrassCount() {
        return startingGrassCount;
    }

    public void setStartingGrassCount(int startingGrassCount) {
        this.startingGrassCount = startingGrassCount;
    }

    public int getGrassGrowthPerDay() {
        return grassGrowthPerDay;
    }

    public void setGrassGrowthPerDay(int grassGrowthPerDay) {
        this.grassGrowthPerDay = grassGrowthPerDay;
    }

    public int getGrassEnergyLevel() {
        return grassEnergyLevel;
    }

    public void setGrassEnergyLevel(int grassEnergyLevel) {
        this.grassEnergyLevel = grassEnergyLevel;
    }

    public int getStartingAnimalsCount() {
        return startingAnimalsCount;
    }

    public void setStartingAnimalsCount(int startingAnimalsCount) {
        this.startingAnimalsCount = startingAnimalsCount;
    }

    public int getAnimalStartingEnergy() {
        return animalStartingEnergy;
    }

    public void setAnimalStartingEnergy(int animalStartingEnergy) {
        this.animalStartingEnergy = animalStartingEnergy;
    }

    public int getAnimalEnergyLossPerMove() {
        return animalEnergyLossPerMove;
    }

    public void setAnimalEnergyLossPerMove(int animalEnergyLossPerMove) {
        this.animalEnergyLossPerMove = animalEnergyLossPerMove;
    }

    public int getAnimalReadyToBreedEnergyLevel() {
        return animalReadyToBreedEnergyLevel;
    }

    public void setAnimalReadyToBreedEnergyLevel(int animalReadyToBreedEnergyLevel) {
        this.animalReadyToBreedEnergyLevel = animalReadyToBreedEnergyLevel;
    }

    public int getAnimalEnergyGivenToChild() {
        return animalEnergyGivenToChild;
    }

    public void setAnimalEnergyGivenToChild(int animalEnergyGivenToChild) {
        this.animalEnergyGivenToChild = animalEnergyGivenToChild;
    }


    public int getGenomeLength() {
        return genomeLength;
    }

    public void setGenomeLength(int genomeLength) {
        this.genomeLength = genomeLength;
    }

    public int getMinimalMutationsCount() {
        return minimalMutationsCount;
    }

    public void setMinimalMutationsCount(int minimalMutationsCount) {
        this.minimalMutationsCount = minimalMutationsCount;
    }

    public int getMaximalMutationsCount() {
        return maximalMutationsCount;
    }

    public void setMaximalMutationsCount(int maximalMutationsCount) {
        this.maximalMutationsCount = maximalMutationsCount;
    }

    public int getStartingOceanCount() {
        return startingOceanCount;
    }

    public void setStartingOceanCount(int startingOceanCount) {
        this.startingOceanCount = startingOceanCount;
    }

    public int getWaterSegments() {
        return waterSegments;
    }

    public void setWaterSegments(int waterSegments) {
        this.waterSegments = waterSegments;
    }

    public int getOceanChangeRate() {
        return oceanChangeRate;
    }

    public void setOceanChangeRate(int oceanChangeRate) {
        this.oceanChangeRate = oceanChangeRate;
    }


    public int getTotalSimulationDays() {
        return totalSimulationDays;
    }

    public void setTotalSimulationDays(int totalSimulationDays) {
        this.totalSimulationDays = totalSimulationDays;
    }
}
