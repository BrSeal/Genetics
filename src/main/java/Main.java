import core.DiophantineGeneticProcessor;
import core.Genome;

import java.util.function.Function;


public class Main {
    private static final int BOUND = 500; //ограничение на максимальное рандомное значение числа в геноме
    private static final int GENOME_SIZE = 5; //размер генома
    private static final int REPETITIONS_COUNT = 1000;

    private static final long MAX_GENERATIONS = 100;     //максимальное количество генераций
    private static final double MUTATION_CHANCE = 0.89;  // вероятность мутации
    private static final int POPULATION_SIZE = 100;      // количество особей в популяции


    // решаем диофантово уравнение 3a+6b+17c-77=0. Фитнес-функция говорит нам как оценивать результат
    private static final Function<Genome, Integer> FITNESS_FUNCTION = n -> {
        int a = n.getGenes().get(0);
        int b = n.getGenes().get(1);
        int c = n.getGenes().get(2);
        int d = n.getGenes().get(3);
        int e = n.getGenes().get(4);

        return Math.abs(3 * a - 6 * b + 17 * c - 21*d + 13* e - 96);
    };

    public static void main(String[] args) {
        Genome bestSolution = null;

        int solved = 0;

        System.out.println("0        10        20        30        40        50        60        70        80        90      100");
        for(int i = 0; i < REPETITIONS_COUNT; i++) {
            DiophantineGeneticProcessor processor = getProcessor();

            if ((i % (REPETITIONS_COUNT / 100)) == 0) System.out.print("#");

            Genome solution = processor.process();

            if (FITNESS_FUNCTION.apply(solution) == 0) solved++;
            else {
                bestSolution = bestSolution == null || bestSolution.getFitnessValue() > solution.getFitnessValue() ? solution : bestSolution;
            }
        }
        System.out.println();
        System.out.println();
        System.out.printf("""
                        total:  %d
                        solved: %d
                        missed: %d
                                                     
                        accuracy: %.3f
                                                     
                        avg. generations per sussed: %.2f
                        
                        """,
                REPETITIONS_COUNT, solved,
                REPETITIONS_COUNT - solved,
                (double) solved / REPETITIONS_COUNT,
                DiophantineGeneticProcessor.avgSussesGenCount());

        if(bestSolution!=null){
            System.out.printf("""
                    nearest solution :
                    value = %d
                    a = %d
                    b = %d
                    c = %d
                    """,bestSolution.getFitnessValue(), bestSolution.getGenes().get(0),bestSolution.getGenes().get(1),bestSolution.getGenes().get(2));
        }


    }

    private static DiophantineGeneticProcessor getProcessor() {
        return new DiophantineGeneticProcessor(GENOME_SIZE, BOUND, FITNESS_FUNCTION, MAX_GENERATIONS, MUTATION_CHANCE, POPULATION_SIZE);
    }
}
