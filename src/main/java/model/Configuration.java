package model;

public class Configuration {


    public enum MapType {
        EARTH_MAP ,
        OCEAN_MAP
    }

    public enum GenomeBehaviour {
        FULL_PREDESTINATION,
        OLD
    }

    private MapType mapType = MapType.EARTH_MAP;
    private int mapWidth = 10;
    private int mapHeight = 10;
    private int startingGrassCount = 10;
    private int grassGrowthPerDay = 1;
    private int grassEnergyLevel = 5;
    private int startingAnimalsCount = 5;
    private int animalStartingEnergy = 1;
    private int animalEnergyLossPerMove = 1;
    private int animalReadyToBreedEnergyLevel = 50;
    private int animalEnergyGivenToChild = 20;
    private GenomeBehaviour genomeBehaviour = GenomeBehaviour.FULL_PREDESTINATION;
    private int genomeLength = 8;
    private int minimalMutationsCount = 1;
    private int maximalMutationsCount = 3;
    private int startingOceanCount = 3;
    private int maxOceanSize = 5;
    private int oceanChangeRate = 10;
    private int millisecondsPerSimulationDay = 500;
    private int totalSimulationDays = Integer.MAX_VALUE;

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

    public GenomeBehaviour getGenomeBehaviour() {
        return genomeBehaviour;
    }

    public void setGenomeBehaviour(GenomeBehaviour genomeBehaviour) {
        this.genomeBehaviour = genomeBehaviour;
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

    public int getMaxOceanSize() {
        return maxOceanSize;
    }

    public void setMaxOceanSize(int maxOceanSize) {
        this.maxOceanSize = maxOceanSize;
    }

    public int getOceanChangeRate() {
        return oceanChangeRate;
    }

    public void setOceanChangeRate(int oceanChangeRate) {
        this.oceanChangeRate = oceanChangeRate;
    }

    public int getMillisecondsPerSimulationDay() {
        return millisecondsPerSimulationDay;
    }

    public void setMillisecondsPerSimulationDay(int millisecondsPerSimulationDay) {
        this.millisecondsPerSimulationDay = millisecondsPerSimulationDay;
    }

    public int getTotalSimulationDays() {
        return totalSimulationDays;
    }

    public void setTotalSimulationDays(int totalSimulationDays) {
        this.totalSimulationDays = totalSimulationDays;
    }
}
