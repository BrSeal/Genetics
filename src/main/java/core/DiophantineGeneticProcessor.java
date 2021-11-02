package core;

import lombok.Data;

import java.util.Random;
import java.util.function.Function;

@Data
public class DiophantineGeneticProcessor {
    private int sumSussesGenerations = 0;    //сколько генераций было суммарно при удачных поисках (для статистики)
    private int sussedCount = 0;             //сколько раз нашли решение (для статистики)

    private final long maxGenerations;     //максимальное количество генераций
    private final double mutationChance;  // вероятность мутации
    private final int populationSize;      // количество особей в популяции
    private final boolean enableNegative;

    private Random random;
    private int generationCount;

    private final int genomeSize;
    private final int bound;
    private final Function<Genome, Integer> fitnessFunction;

    private Population population;

    private boolean isFinished;

    public DiophantineGeneticProcessor(int genomeSize, int bound, Function<Genome, Integer> fitnessFunction, long maxGenerations, double mutationChance, int populationSize, boolean enableNegative) {
        this.maxGenerations = maxGenerations;
        this.mutationChance = mutationChance;
        this.populationSize = populationSize;
        this.genomeSize = genomeSize;
        this.bound = bound;
        this.fitnessFunction = fitnessFunction;
        this.enableNegative = enableNegative;

        random = new Random();
        generationCount = 1;

        population = new Population(this.populationSize, genomeSize, bound, mutationChance, enableNegative);
    }

    public double avgSussesGenCount() {
        return (double) sumSussesGenerations / sussedCount;
    }

    public Genome process() {
        if (isFinished) return population.getBest();

        Genome best = null;

        while (generationCount <= maxGenerations) {
            population.evaluateGenomes(fitnessFunction);
            best = population.getBest();
            if (best.getFitnessValue() == 0) break;

            population.nextGeneration();
            generationCount++;
        }

        isFinished = true;
        return best;
    }

    public Genome getSolution(){
       return population.getBest();
    }
}
