package Knapsack;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.engine.Limits;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Knapsack {

    public static void main(String[] args) {
        int nItems = 15;
        double ksSize = nItems * 100.0 / 3.0;

        List<KnapsackItem> knapsackItems = Stream.generate(KnapsackItem::random)
                .limit(nItems)
                .collect(Collectors.toList());
        KnapsackItem[] knapsackItems1 = new KnapsackItem[ knapsackItems.size()];
        knapsackItems.toArray( knapsackItems1 );
               // .toArray(KnapsackItem[]::new);

        KnapsackFF ff = new KnapsackFF(
                knapsackItems1,
                    ksSize);

        Engine<BitGene, Double> engine = Engine.builder(ff, BitChromosome.of(nItems, 0.5))
                .populationSize(500)
                .survivorsSelector(new TournamentSelector<>(5))
                .offspringSelector(new RouletteWheelSelector<>())
                .alterers(new Mutator<>(0.115), new SinglePointCrossover<>(0.16))
                .build();

        EvolutionStatistics<Double, ?> statistics = EvolutionStatistics.ofNumber();

        Phenotype<BitGene, Double> best = engine.stream()
                .limit(Limits.bySteadyFitness(7))
                .limit(100)
                .peek(statistics)
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println(statistics);
        System.out.println(best);
    }
}
