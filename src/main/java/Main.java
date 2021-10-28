import core.DiophantineGeneticProcessor;
import core.Genome;

import java.util.function.Function;


public class Main {
    private static final int BOUND = 500; //ограничение на максимальное рандомное значение числа в геноме
    private static final int GENOME_SIZE = 3; //размер генома
    private static final int REPETITIONS_COUNT = 10000;

    private static final long MAX_GENERATIONS = 100;     //максимальное количество генераций
    private static final double MUTATION_CHANCE = 0.5;  // вероятность мутации
    private static final int POPULATION_SIZE = 100;      // количество особей в популяции


    // решаем диофантово уравнение 3a+6b+17c-77=0. Фитнес-функция говорит нам как оценивать результат
    private static final Function<Genome, Integer> FITNESS_FUNCTION = n -> {
        int a = n.getGenes().get(0);
        int b = n.getGenes().get(1);
        int c = n.getGenes().get(2);

        return Math.abs(5 * a - 11 * b + 3 * c + 653);
    };

    public static void main(String[] args) {
        int solved = 0;
        System.out.println("0        10        20        30        40        50        60        70        80        90      100");
        for(int i = 0; i < REPETITIONS_COUNT; i++) {

            if ((i % (REPETITIONS_COUNT / 100)) == 0) System.out.print("#");


            if (FITNESS_FUNCTION.apply(getProcessor().process()) == 0) solved++;
        }
        System.out.println();
        System.out.println();
        System.out.printf("""
                        total:  %d
                        solved: %d
                        missed: %d
                                                     
                        accuracy: %.3f
                                                     
                        avg. generations per sussed: %.2f""",
                REPETITIONS_COUNT, solved,
                REPETITIONS_COUNT - solved,
                (double) solved / REPETITIONS_COUNT,
                DiophantineGeneticProcessor.avgSussesGenCount());
    }

    private static DiophantineGeneticProcessor getProcessor() {
        return new DiophantineGeneticProcessor( GENOME_SIZE, BOUND, FITNESS_FUNCTION,MAX_GENERATIONS, MUTATION_CHANCE, POPULATION_SIZE);
    }
}
