package Processors;

public class Task {
    //task number
    private final int taskNumber;
    private final int[] times;

    public Task(int task, int[] times) {
        this.taskNumber = task;
        this.times = times;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public int[] getTimes() {
        return times.clone();
    }
}
