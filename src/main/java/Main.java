import core.DiophantineGeneticProcessor;
import core.Genome;
import statistics.StatsHolder;

import java.util.function.Function;


public class Main {
    private static final int BOUND = 500; //ограничение на максимальное рандомное значение числа в геноме
    private static final int GENOME_SIZE = 5; //размер генома
    private static final int REPETITIONS_COUNT = 1000;

    private static final long MAX_GENERATIONS = 100;     //максимальное количество генераций
    private static final double MUTATION_CHANCE = 0.9;  // вероятность мутации
    private static final int POPULATION_SIZE = 100;      // количество особей в популяции

    private static final StatsHolder stats = new StatsHolder(GENOME_SIZE);

    // решаем диофантово уравнение
    private static final Function<Genome, Integer> FITNESS_FUNCTION = n -> {
        int a = n.getGenes().get(0);
        int b = n.getGenes().get(1);
        int c = n.getGenes().get(2);
        int d = n.getGenes().get(3);
        int e = n.getGenes().get(4);

        return Math.abs(3 * a - 6 * b + 17 * c - 21*d + 13* e - 96);
    };

    public static void main(String[] args) {


        System.out.println("0        10        20        30        40        50        60        70        80        90      100");
        for(int i = 0; i < REPETITIONS_COUNT; i++) {
            if ((i % (REPETITIONS_COUNT / 100)) == 0) System.out.print("#");
            DiophantineGeneticProcessor processor = getProcessor();
            processor.process();
            stats.addSolution(processor);
        }
        System.out.println();
        System.out.println(stats);
    }

    private static DiophantineGeneticProcessor getProcessor() {
        return new DiophantineGeneticProcessor(GENOME_SIZE, BOUND, FITNESS_FUNCTION, MAX_GENERATIONS, MUTATION_CHANCE, POPULATION_SIZE, false);
    }
}
