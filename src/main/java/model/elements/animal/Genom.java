package model.elements.animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Genom {
    private final int length;
    private int activeGene;
    private final List<Gene> genes;

    public Genom(int length) {
        this.length = length;
        this.activeGene = 0;

        this.genes = new ArrayList<>(length);
    }


    public static Genom RandomGenome(int length) {
        Genom genome = new Genom(length);

        for(int i = 0; i < length; i++){
            genome.genes.add(Gene.getRandom());
        }

        return genome;
    }

    public Genom combineGenomes(Genom rightGenes, float percentOfGenes) {
        int rightGenesCount = (int)Math.ceil(length * percentOfGenes);

        Genom genome = new Genom(length);
        genome.genes.addAll(this.genes.subList(0, length - rightGenesCount));
        genome.genes.addAll(rightGenes.genes.subList(length - rightGenesCount, length));

        return genome;
    }

    public void mutate(int minimumMutationsCount, int maximumMutationsCount) {
        if(maximumMutationsCount - minimumMutationsCount <= 0)
            return;

        int mutationsCount = ThreadLocalRandom.current().nextInt(minimumMutationsCount, maximumMutationsCount);

        int[] indexes = ThreadLocalRandom.current()
                .ints(0, length)
                .distinct()
                .limit(mutationsCount)
                .toArray();

        for (int index : indexes) {
            genes.set(index, genes.get(index).getNewRandom());
        }
    }

    public void nextGene() {
        activeGene = (activeGene + 1) % length;
    }

    public Gene getActiveGene() {
        return genes.get(activeGene);
    }

    public int getActiveGeneIndex() {
        return activeGene;
    }

    @Override
    public String toString() {
        return genes.stream()
                .map(Gene::toString)
                .reduce("", (acc, gene) -> acc + gene);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genom genom = (Genom) o;
        return length == genom.length && activeGene == genom.activeGene && Objects.equals(genes, genom.genes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(length, activeGene, genes);
    }
}
