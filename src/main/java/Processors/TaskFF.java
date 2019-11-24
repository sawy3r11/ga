package Processors;

import io.jenetics.Genotype;
import io.jenetics.IntegerGene;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TaskFF implements Function<Genotype<IntegerGene>, Integer> {
    private final List<Task> tasks;

    public TaskFF(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Fitness function
     */
    @Override
    public Integer apply(Genotype<IntegerGene> gt) {
        Map<Integer, Integer> processorTaskTimeMap = new HashMap<>();
        // Iterate over genes in chromosome
        for (int i = 0; i < gt.getChromosome().length(); i++) {
            // Get gene value which represent number of processor
            Integer processor = gt.getChromosome().getGene(i).getAllele();
            // Get time which is necessary to complete task by i-processor
            Integer taskTime = tasks.get(i).getTimes()[processor];
            // Update map with total time for each processor
            processorTaskTimeMap.putIfAbsent(processor, taskTime);
            processorTaskTimeMap.computeIfPresent(processor, (processorNumber, currentTaskTime) -> currentTaskTime + taskTime);
        }
        // Find the longest processing time
        int time = processorTaskTimeMap.size() > 0 ? processorTaskTimeMap.values().stream().mapToInt(Integer::intValue).max().getAsInt() : 0;
        // Return time with minus (longer time, smaller value)
        return -time;
    }
}
