package core;

import lombok.Data;

import java.util.Random;
import java.util.function.Function;

@Data
public class DiophantineGeneticProcessor {
    static int sumSussesGenerations = 0;    //сколько генераций было суммарно при удачных поисках (для статистики)
    static int sussedCount = 0;             //сколько раз нашли решение (для статистики)

    private final long maxGenerations;     //максимальное количество генераций
    private final double mutationChance;  // вероятность мутации
    private final int populationSize;      // количество особей в популяции

    private Random random;
    private int generationCount;

    private final int genomeSize;
    private final int bound;
    private final Function<Genome, Integer> fitnessFunction;

    private Population population;

    public DiophantineGeneticProcessor(int genomeSize, int bound, Function<Genome, Integer> fitnessFunction, long maxGenerations, double mutationChance, int populationSize) {
        this.maxGenerations = maxGenerations;
        this.mutationChance = mutationChance;
        this.populationSize = populationSize;
        this.genomeSize = genomeSize;
        this.bound = bound;
        this.fitnessFunction = fitnessFunction;

        random = new Random();
        generationCount = 0;

        population = new Population(this.populationSize, genomeSize, bound, mutationChance);
    }

    public static double avgSussesGenCount() {
        return (double) sumSussesGenerations / sussedCount;
    }

    public Genome process() {
        Genome best = null;

        while (generationCount <= maxGenerations) {
            population.evaluateGenomes(fitnessFunction);
            best = population.getBest();
            if (best.getFitnessValue() == 0) break;

            population.nextGeneration();
        }
        return best;
    }
}
