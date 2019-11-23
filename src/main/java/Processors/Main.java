package Processors;

import io.jenetics.*;
import io.jenetics.engine.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final String DATA_1 = "data1.txt";
    public static final String DATA_2 = "data2.txt";

    public static void main(String ...args){
        //load data from file to wrapper class
        TaskProcessorData taskProcessorData = getTaskList(DATA_2);

        //create class which implements Function<Genotype<IntegerGene>, Integer> (contain fitness function)
        TaskFF taskFF = new TaskFF( taskProcessorData.getTasks() );

        /*
        * Create Integer chromosome n, values: <min, max>
        * min: 0
        * max = number of available processors minus 1 (we start form 0)
        * n = number of tasks
        *
        * Each gene is represent by processor number */
        IntegerChromosome integerGenes = IntegerChromosome.of(0, taskProcessorData.getProcessorNumber()-1, taskProcessorData.getTaskNumber());
        Engine<IntegerGene, Integer> engine = Engine.builder(taskFF, integerGenes)
                .populationSize(1000)
                .survivorsSelector(new TournamentSelector<>(5))
                .offspringSelector(new RouletteWheelSelector<>())
                .alterers(new Mutator<>(0.115), new SinglePointCrossover<>(0.16))
                .build();

        EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();
        Phenotype<IntegerGene, Integer> best = engine.stream()
                .limit(Limits.bySteadyFitness(7))
                .limit(100)
                .peek(statistics)
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println(statistics);
        System.out.println(best);
    }

    public static TaskProcessorData getTaskList(String fileName){
        List<Task> taskList = new ArrayList<>();
        try{
            File file = new File(
                    Main.class.getClassLoader().getResource(fileName).getFile()
            );
            FileReader fR = new FileReader( file);
            BufferedReader bF = new BufferedReader( fR );
            String textLine ="";
            int index=0;
            textLine = bF.readLine();
            int nubmerOfTasks = 0;
            int numberOfProcessors = 0;

            while (textLine!=null && textLine.length()>0 ){
                if(index==0){
                    nubmerOfTasks = (Integer.valueOf(textLine));
                }else if(index==1){
                    numberOfProcessors = (Integer.valueOf(textLine));
                }else{
                    String[] times = textLine.split(",");
                    int task = -1;
                    int[] timesInt = new int[ numberOfProcessors ];
                    for(int i=0; i<(numberOfProcessors+1); i++){
                        int number = (Integer.valueOf(times[i]));
                        if( i==0 ){
                            task = number;
                        }else{
                            timesInt[i-1] = number;
                        }

                    }
                    taskList.add(new Task(task, timesInt));
                }
                textLine = bF.readLine();
                index++;
            }

            TaskProcessorData taskProcessorData = new TaskProcessorData(taskList, nubmerOfTasks, numberOfProcessors);

            return taskProcessorData;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
