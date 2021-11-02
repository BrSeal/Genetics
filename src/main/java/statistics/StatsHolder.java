package statistics;

import core.DiophantineGeneticProcessor;
import core.Genome;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.NaN;

@Getter
@Setter
public class StatsHolder {
    private final int variablesCount;

    private final List<Integer> generationsPerSolution;
    private Genome bestTry;
    private int count;

    public StatsHolder(int variablesCount) {
        this.variablesCount = variablesCount;
        this.generationsPerSolution = new ArrayList<>();
    }

    public StatsHolder(int variablesCount, DiophantineGeneticProcessor processor) {
        this(variablesCount);
        addSolution(processor);

    }

    double getAvgGenerationsPerSuccess() {
        return round(generationsPerSolution.stream()
                .mapToDouble(d -> d)
                .average()
                .orElse(NaN));
    }

    public void addSolution(DiophantineGeneticProcessor processor) {
        if (processor.getSolution() == null) return;

        Genome solution = processor.getSolution();
        count++;

        if (solution.getFitnessValue() == 0) generationsPerSolution.add(processor.getGenerationCount());
        else bestTry = bestTry != null && bestTry.getFitnessValue() < solution.getFitnessValue() ? bestTry : solution;
    }

    @Override
    public String toString() {
        int solvedCount = generationsPerSolution.size();

        StringBuilder builder = new StringBuilder();
        builder.append("number of variables: ").append(variablesCount).append("\n")
                .append("total:    ").append(count).append("\n")
                .append("solved:   ").append(solvedCount).append("\n")
                .append("missed:   ").append(count - solvedCount).append("\n")
                .append("accuracy: ").append(round((double) solvedCount / count)).append("\n")
                .append("avg. generations per success: ").append(getAvgGenerationsPerSuccess()).append("\n");

        if (bestTry != null) {
            builder.append("nearest fitness value: ").append(bestTry.getFitnessValue()).append("\n");
        }

        return builder.toString();

    }

    private double round(double d) {
        return (double) Math.round(d * 1000) / 1000;
    }
}
