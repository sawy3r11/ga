import Processors.Task;
import Processors.TaskFF;
import Processors.TaskProcessorData;
import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.engine.Limits;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final String DATA = "data2.txt";
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    // Genetic algorithm's initialization parameters
    private static final int POPULATION_SIZE = 1000;
    private static final int TOURNAMENT_SIZE = 5;
    private static final double MUTATION_PROBABILITY = 0.115;
    private static final double CROSSOVER_PROBABILITY = 0.16;
    // Genetic algorithm end's condition parameters
    private static final int MAX_STALE_GENERATIONS_RESULT = 7;
    private static final int MAX_NUMBER_OF_GENERATIONS = 100;

    public static void main(String... args) {
        // Load resource data for wrapper class
        TaskProcessorData taskProcessorData = loadResourceData();
        if (taskProcessorData == null) {
            logger.info("Something went terribly wrong");
            return;
        }
        // Create class which implements Function<Genotype<IntegerGene>, Integer> (contains fitness function)
        TaskFF taskFF = new TaskFF(taskProcessorData.getTasks());
        /*
         * Create Integer chromosome n, values: <min, max>
         * min: 0
         * max = number of available processors minus 1 (we start from 0)
         * n = number of tasks
         *
         * Each gene is represent by processor number */
        IntegerChromosome integerGenes = IntegerChromosome.of(0, taskProcessorData.getProcessorNumber() - 1, taskProcessorData.getTaskNumber());
        // Initialize genetic algorithm's engine
        Engine<IntegerGene, Integer> engine = prepare(taskFF, integerGenes);
        // Statistics for more information about the results
        EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();
        // Find best result
        Phenotype<IntegerGene, Integer> best = calculate(engine, statistics);
        logger.log(Level.INFO, "--- Statistics --- \n\n {0} \n\n", new Object[]{statistics});
        logger.log(Level.INFO, "--- Best --- \n\n {0}", new Object[]{best});
    }

    private static TaskProcessorData loadResourceData() {
        List<Task> taskList = new ArrayList<>();
        File file = new File(Main.class.getResource(DATA).getFile());
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String textLine = bufferedReader.readLine();
            int index = 0;
            int amountOfTasks = 0;
            int amountOfProcessors = 0;
            while (textLine != null && textLine.length() > 0) {
                switch (index) {
                    case 0: // gather amount of tasks (first line)
                        amountOfTasks = Integer.parseInt(textLine);
                        index++;
                        break;
                    case 1: // gather amount of processors (second line)
                        amountOfProcessors = Integer.parseInt(textLine);
                        index++;
                        break;
                    default: // gather number of task and his time on each processor
                        String[] times = textLine.split(",");
                        int task = Integer.parseInt(times[0]);
                        int[] timesInt = Arrays.stream(times).skip(1).mapToInt(Integer::parseInt).toArray();
                        taskList.add(new Task(task, timesInt));
                }
                textLine = bufferedReader.readLine();
            }
            return new TaskProcessorData(taskList, amountOfTasks, amountOfProcessors);
        } catch (IOException ignored) {
            return null;
        }
    }

    private static Engine<IntegerGene, Integer> prepare(TaskFF taskFF, IntegerChromosome integerGenes) {
        return Engine.builder(taskFF, integerGenes)
                .populationSize(POPULATION_SIZE)
                .survivorsSelector(new TournamentSelector<>(TOURNAMENT_SIZE))
                .offspringSelector(new RouletteWheelSelector<>())
                .alterers(new Mutator<>(MUTATION_PROBABILITY), new SinglePointCrossover<>(CROSSOVER_PROBABILITY))
                .build();
    }

    private static Phenotype<IntegerGene, Integer> calculate(Engine<IntegerGene, Integer> engine, EvolutionStatistics<Integer, ?> statistics) {
        return engine.stream()
                .limit(Limits.bySteadyFitness(MAX_STALE_GENERATIONS_RESULT)) // returns a predicate which will truncate evolution stream if no better phenotype could be found
                .limit(MAX_NUMBER_OF_GENERATIONS) // max number of generations
                .peek(statistics) // peek just for statistics gathering
                .collect(EvolutionResult.toBestPhenotype()); // collect best phenotype
    }
}
