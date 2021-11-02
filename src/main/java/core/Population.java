package core;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class Population {
    private int size;
    private int genomeSize;
    private int bound;
    private double mutationChance;
    private boolean enableNegative;

    private List<Genome> genomes;
    private Random random;

    private Genome best;

    public Population(int size, int genomeSize, int bound, double mutationChance, boolean enableNegative) {
        this.size = size;
        this.genomeSize = genomeSize;
        this.bound = bound;
        this.mutationChance = mutationChance;
        this.enableNegative = enableNegative;

        random = new Random();
        genomes = new ArrayList<>(size);

        for(int i = 0; i < size; i++) {
            genomes.add(new Genome(genomeSize, bound, enableNegative));
        }
    }

    public void evaluateGenomes(Function<Genome, Integer> fitnessFunction) {
        for(Genome genome : genomes) {
            int fitnessVal = fitnessFunction.apply(genome);
            genome.setFitnessValue(fitnessVal);
        }
        best = findBest(best);
    }

    public void nextGeneration() {
        List<Genome> nextGeneration = new ArrayList<>();

        List<Double> probabilities = calcProbabilities();

        for(int i = 0; i < size; i++) {
            var parent1 = chooseParent(probabilities);
            var parent2 = chooseParent(probabilities);

            Genome child = parent1.crossover(parent2);

            if (random.nextDouble() < mutationChance) child.mutate();

            nextGeneration.add(child);
        }

        genomes = nextGeneration;
    }

    private List<Double> calcProbabilities() {
        double reversedSum = genomes.stream()
                .map(genome -> 1d / genome.getFitnessValue())
                .reduce(Double::sum)
                .orElseThrow();

        return genomes.stream()
                .map(genome -> 1d / genome.getFitnessValue() / reversedSum)
                .collect(Collectors.toList());
    }

    private Genome chooseParent(List<Double> probabilities) {
        double rand = random.nextDouble();
        double cumulative = 0;

        for(int i = 0; i < size; i++) {
            double next = cumulative + probabilities.get(i);

            if (rand > cumulative && rand < next) {
                return genomes.get(i);
            }
            cumulative += probabilities.get(i);
        }

        throw new RuntimeException("Sum of probabilities must be 1! In fact it is less!");
    }

    private Genome findBest(Genome prevBest) {
        Genome currentBest = genomes.stream()
                .min(Comparator.comparingInt(Genome::getFitnessValue))
                .orElseThrow();

        return prevBest != null && prevBest.getFitnessValue() < currentBest.getFitnessValue() ? prevBest : currentBest;
    }
}
