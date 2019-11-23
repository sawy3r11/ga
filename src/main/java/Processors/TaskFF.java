package Processors;

import io.jenetics.*;

import java.util.*;
import java.util.function.Function;

public class TaskFF implements Function<Genotype<IntegerGene>, Integer> {
    private List<Task> tasks;

    public TaskFF(List<Task> tasks) {
        this.tasks = tasks;
    }


    /**
     * Fitness function
     * */
    @Override
    public Integer apply(Genotype<IntegerGene> gt){
        int time = 0;
        Map<Integer, Integer> map = new HashMap<>();
        //iterate over genes in chromosome
        for(int i=0; i<gt.getChromosome().length(); i++){
            //get gene value which represent number of processor
            Integer processor = gt.getChromosome().getGene( i ).getAllele();
            //get time which is necessary to complete task by i-processor
            Integer taskTime = tasks.get(i).times[processor];
            //update map with total time for each processor
            if( map.get(processor)==null ){
                map.put(processor, taskTime);
            }else{
                Integer sum = map.get(processor) + taskTime;
                map.put(processor, sum);
            }
        }
        //find the longest processing time
        time = map.size()>0 ? map.values().stream().max(Comparator.comparing(Integer::intValue)).get() : 0;

        //return time with minus (longer time, smaller value)
        return -time;
    }
}
