import core.DiophantineGeneticProcessor;
import core.Genome;

import java.util.function.Function;


public class Main {
    private static final int BOUND = 500; //ограничение на максимальное рандомное значение числа в геноме
    private static final int GENOME_SIZE = 2; //размер генома
    private static final int REPETITIONS_COUNT = 1000;

    // решаем диофантово уравнение a+6b=342. Фитнес-функция говорит нам как оценивать результат
    private static final Function<Genome, Integer> FITNESS_FUNCTION = n -> Math.abs(n.getGenes().get(0) + 6 * n.getGenes().get(1) - 342);

    public static void main(String[] args) {
        int solved = 0;

        for(int i = 0; i < REPETITIONS_COUNT; i++) {
            if (FITNESS_FUNCTION.apply(getProcessor().process()) == 0) {
                solved++;
            }
        }
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
        return new DiophantineGeneticProcessor(GENOME_SIZE, BOUND, FITNESS_FUNCTION);
    }
}
