package Processors;

import java.util.ArrayList;
import java.util.List;

public class TaskProcessorData {
    private final List<Task> tasks;
    private final Integer taskNumber;
    private final Integer processorNumber;

    public TaskProcessorData(List<Task> tasks, Integer taskNumber, Integer processorNumber) {
        this.tasks = tasks;
        this.taskNumber = taskNumber;
        this.processorNumber = processorNumber;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    public Integer getTaskNumber() {
        return taskNumber;
    }

    public Integer getProcessorNumber() {
        return processorNumber;
    }
}
