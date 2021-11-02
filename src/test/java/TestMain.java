import core.DiophantineGeneticProcessor;
import core.Genome;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import statistics.StatsHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMain {
    private static final int BOUND = 100; //ограничение на максимальное рандомное значение числа в геноме
    private static final int REPETITIONS_COUNT = 1000;

    private static final long MAX_GENERATIONS = 100;     //максимальное количество генераций
    private static final double MUTATION_CHANCE = 1;  // вероятность мутации
    private static final int POPULATION_SIZE = 300;      // количество особей в популяции
    private static final boolean ENABLE_NEGATIVE = true;

    Random r = new Random();
    Map<Integer, StatsHolder> stats = new HashMap<>();

    @Test
    void randomEquationTest(){
        int success= 0;
        for(int i = 0; i < REPETITIONS_COUNT; i++) {
            int genomeSize = 5+r.nextInt(100);
            var func = DiophantineFitnessFunctionGenerator.generate(genomeSize, 50, ENABLE_NEGATIVE);

            DiophantineGeneticProcessor processor = new DiophantineGeneticProcessor(genomeSize, BOUND, func, MAX_GENERATIONS, MUTATION_CHANCE, POPULATION_SIZE, ENABLE_NEGATIVE);

           if(processor.process().getFitnessValue()==0){
               success++;
           }

           addStat(genomeSize, processor);
        }

        stats.forEach((k,v)-> System.out.println(v.toString()));
        assertEquals(success,REPETITIONS_COUNT, String.format("Success: %.3f", (double)success/REPETITIONS_COUNT));
    }

    @Test
    void mutate() {
        for(int i = 0; i < 1000; i++) {
            List<Integer> genes = new ArrayList<>(List.of(
                    r.nextInt(20),
                    r.nextInt(20),
                    r.nextInt(20),
                    r.nextInt(20),
                    r.nextInt(20)
            ));

            Genome genome = new Genome(genes);
            genome.mutate();
            Assertions.assertTrue(calcDiff(genes, genome.getGenes()));
        }
    }

    private boolean calcDiff(List<Integer> a, List<Integer> b) {
        int diff = 0;
        for(int i : a) {
            diff += i;
        }
        for(int i : b) {
            diff -= i;
        }

        return diff >= -1 && diff <= 1;
    }


    @Test
    void probability() {
        for(int i = 0; i < 10000; i++) {
            double rand = r.nextDouble();
            int expected = rand < 0.33334 ? 1 : rand > 0.66668 ? 3 : 2;
            assertEquals(expected, selectByRandom(List.of(0.33334, 0.33334, 0.33334), rand));
        }
    }


    private int selectByRandom(List<Double> probabilities, double rand) {
        double cumulative = 0;

        for(int i = 0; i < probabilities.size(); i++) {
            double next = cumulative + probabilities.get(i);

            if (rand > cumulative && rand < next) {
                return i+1;
            }
            cumulative += probabilities.get(i);
        }
        return -1;
    }

    private void addStat(int variablesCount, DiophantineGeneticProcessor processor){
        stats.merge(variablesCount, new StatsHolder(variablesCount, processor),(oldVal, newVal)->{
            oldVal.addSolution(processor);
            return oldVal;
        });
    }
}
