import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;

import java.util.Arrays;
import java.util.List;

public class Main {
    // 2.) Definition of the fitness function.
    private static int eval(Genotype<BitGene> gt) {
        return gt.getChromosome()
                .as(BitChromosome.class)
                .bitCount()%3;
    }

    private static int eval(Genotype<BitGene> gt, List<Process> processes){
        int result  = 0;
        //gt.
        return 0;
    }

    public static void main(String[] args) {
        // 1.) Define the genotype (factory) suitable
        //     for the problem.
        Factory<Genotype<BitGene>> gtf =
                Genotype.of(BitChromosome.of(10, 0.5));

        // 3.) Create the execution environment.
        Engine<BitGene, Integer> engine = Engine
                .builder(Main::eval, gtf)
                .build();

        // 4.) Start the execution (evolution) and
        //     collect the result.
        Genotype<BitGene> result = engine.stream()
                .limit(100)
                .collect(EvolutionResult.toBestGenotype());

        System.out.println("Hello World:\n" + result);
//
//        Process p1 = new Process(1, Arrays.asList(1,2,3));
//        Process p2 = new Process(1, Arrays.asList(1,2,3));
//        Process p3 = new Process(1, Arrays.asList(1,2,3));
//        Process p4 = new Process(1, Arrays.asList(1,2,3));
//        Process[] processes = {p1, p2, p3, p4};
//
//        ISeq<Process> processISeq = Arrays.asList(processes).stream().collect(  ISeq.toISeq() );
    }
}



