import core.Genome;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMain {
    Random r = new Random();

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
        for(int i = 0; i < 300; i++) {

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
}
