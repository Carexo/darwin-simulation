package model.elements.animal;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenomeTest {

    @Test
    void testRandomGenome() {
        Genome genome = Genome.RandomGenome(10);
        assertNotNull(genome);
        assertEquals(10, genome.toString().length());
    }

    @Test
    void testCombineGenomes() {
        Genome genome1 = Genome.RandomGenome(10);
        Genome genome2 = Genome.RandomGenome(10);

        Genome combinedGenome = genome1.combineGenomes(genome2, 0.5f);
        assertNotNull(combinedGenome);
        assertEquals(10, combinedGenome.toString().length());
    }

    @Test
    void testMutate() {
        Genome genome = Genome.RandomGenome(10);
        List<Gene> originalGenes = List.copyOf(genome.getGenes());

        genome.mutate(1, 3);
        assertNotEquals(originalGenes, genome.getGenes());
    }

    @Test
    void testGetActiveGene() {
        Genome genome = Genome.RandomGenome(10);
        assertNotNull(genome.getActiveGene());
    }

}