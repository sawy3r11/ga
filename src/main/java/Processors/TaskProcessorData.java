package Processors;

import java.util.List;

public class TaskProcessorData {
    private List<Task> tasks;
    private Integer taskNumber;
    private Integer processorNumber;

    public TaskProcessorData(List<Task> tasks, Integer taskNumber, Integer processorNumber) {
        this.tasks = tasks;
        this.taskNumber = taskNumber;
        this.processorNumber = processorNumber;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Integer getTaskNumber() {
        return taskNumber;
    }

    public Integer getProcessorNumber() {
        return processorNumber;
    }
}
