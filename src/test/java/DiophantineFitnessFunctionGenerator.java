import core.Genome;
import core.exceptions.WrongGenomeSizeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class DiophantineFitnessFunctionGenerator {

    /**
     * Generates fitness function with declared number of arguments
     * @param genomeSize - number of arguments in equation
     * @param coefficientsBound - upper bound for values of coefficients
     * @param enableNegative - enables negative numbers generation
     * @return - a fitness function for
     */
    static Function<Genome, Integer> generate(int genomeSize, int coefficientsBound, boolean enableNegative) {
        Random random = new Random();

        List<Integer> coefficients = new ArrayList<>();

        int sum = 0;
        do {
            int coefficient = random.nextInt(coefficientsBound) * (enableNegative && random.nextBoolean() ? -1 : 1);
            int solution = random.nextInt(coefficientsBound);

            coefficients.add(coefficient);

            sum += solution * coefficient;
        } while (coefficients.size() < genomeSize);

        final int finalSum = sum;

        return genome -> {
            List<Integer> genes = genome.getGenes();

            if (genome.getGenes().size() != genomeSize) throw new WrongGenomeSizeException();

            int result=0;
            for(int i=0; i< genomeSize; i++){
                result +=genes.get(i)*coefficients.get(i);
            }
            return Math.abs(result-finalSum);
        };
    }
}
