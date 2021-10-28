package core;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class Genome {
    private List<Integer> genes;

    //Число, которое потом проставляет фитнес-функция
    private Integer fitnessValue;

    private Random random = new Random();

    public Genome(List<Integer> genes) {
        this.genes = genes;
    }

    public Genome(int genomeSize, int bound) {
        genes = new ArrayList<>();

        for(int i = 0; i < genomeSize; i++) {
            genes.add(random.nextInt(bound));
        }
    }

    //мутация может быть любая! Конкретно здесь случайное число в генах меняется на 1
    public void mutate() {
        int index = random.nextInt(genes.size());
        int chromosome = genes.get(index);

        genes.set(index, random.nextBoolean() ? chromosome - 1 : chromosome + 1);
    }
//    public void mutate(int bound) {
//        int index = random.nextInt(genes.size());
//        int chromosome = genes.get(index);
//
//        genes.set(index, random.nextInt(bound));
//    }

    public Genome crossover(Genome another) {
        List<Integer> childGenes = new ArrayList<>();
        for(int i = 0; i < genes.size(); i++) {
            childGenes.add(random.nextBoolean() ? genes.get(i) : another.genes.get(i));
        }

        return new Genome(childGenes);
    }
}
