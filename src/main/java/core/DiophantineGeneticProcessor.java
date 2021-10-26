package core;

import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class DiophantineGeneticProcessor {
    static int sumSussesGenerations = 0;    //сколько генераций было суммарно при удачных поисках (для статистики)
    static int sussedCount = 0;             //сколько раз нашли решение (для статистики)

    private static final long MAX_GENERATIONS = 50;     //максимальное количество генераций
    private static final double MUTATION_CHANCE = 0.1;  // вероятность мутации
    private static final int POPULATION_SIZE = 50;      // количество особей в популяции

    private Random random;
    private int generationCount;

    private final int genomeSize;
    private final int bound;
    private final Function<Genome, Integer> fitnessFunction;

    private List<Genome> population;

    public DiophantineGeneticProcessor(int genomeSize, int bound, Function<Genome, Integer> fitnessFunction) {
        this.genomeSize = genomeSize;
        this.bound = bound;
        this.fitnessFunction = fitnessFunction;

        random = new Random();
        generationCount = 0;

        population = new ArrayList<>(POPULATION_SIZE);
        for(int i = 0; i < POPULATION_SIZE; i++) {
            population.add(new Genome(genomeSize, bound));
        }
    }

    public static double avgSussesGenCount() {
        return (double) sumSussesGenerations / sussedCount;
    }

    public Genome process() {
        Genome best = null;

        while (generationCount <= MAX_GENERATIONS) {
            for(Genome genome : population) {
                int fitnessVal = fitnessFunction.apply(genome);
                genome.setFitnessValue(fitnessVal);
                if (fitnessVal == 0) {
                    sumSussesGenerations += generationCount;
                    sussedCount++;
                    return genome;
                }
            }
            best = findBest(best);
            nextGeneration();
        }
        //если ничего не нашли, то возвращаем ближайшее значение
        return best;
    }

    private void nextGeneration() {
        List<Genome> nextGeneration = new ArrayList<>();

        List<Double> probabilities = calcProbabilities();

        for(int i = 0; i < POPULATION_SIZE; i++) {
            var parent1 = chooseParent(probabilities);
            var parent2 = chooseParent(probabilities);

            Genome child = parent1.crossover(parent2);

            if (random.nextDouble() < MUTATION_CHANCE) child.mutate();

            nextGeneration.add(child);
        }

        generationCount++;
        population = nextGeneration;
    }

    private Genome chooseParent(List<Double> probabilities) {
        double rand = random.nextDouble();
        double cumulative = 0;

        for(int i = 0; i < POPULATION_SIZE; i++) {
            double next = cumulative + probabilities.get(i);

            if (rand > cumulative && rand < next) {
                return population.get(i);
            }
            cumulative += probabilities.get(i);
        }

        throw new RuntimeException("Sum of probabilities must be 1! In fact it is less!");
    }

    private List<Double> calcProbabilities() {
        double reversedSum = population.stream()
                .map(genome -> 1d / genome.getFitnessValue())
                .reduce(Double::sum)
                .orElseThrow();

        return population.stream()
                .map(genome -> 1d / genome.getFitnessValue() / reversedSum)
                .collect(Collectors.toList());
    }

    private Genome findBest(Genome prevBest) {
        Genome currentBest = population.stream()
                .min(Comparator.comparingInt(Genome::getFitnessValue))
                .orElseThrow();

        return prevBest != null && prevBest.getFitnessValue() < currentBest.getFitnessValue() ? prevBest : currentBest;
    }
}
