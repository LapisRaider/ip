package duke.task;

import duke.util.Loading;
import duke.util.Saving;

/**
 * Abstracts class Task.
 *
 * <p>Any sub-task class will inherit. Can save and load.</p>
 */
public abstract class Task implements Saving, Loading {
    protected boolean isDone;
    protected String taskDescription;

    /**
     * Default constructor for task.
     *
     * <p>Set isDone to false and taskDescription to be empty.</p>
     */
    public Task() {
        this.isDone = false;
        this.taskDescription = "";
    }

    /**
     * Overloaded constructor for task.
     *
     * <p>IsDone is still defaulted to false.</p>
     *
     * @param taskDescription Task description of task.
     */
    public Task(String taskDescription) {
        this.isDone = false;
        this.taskDescription = taskDescription;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }


    public String getTaskDescription() {
        return taskDescription;
    }

    /**
     * Gets the Task in string format.
     *
     * @return Task in string format.
     */
    @Override
    public String toString() {
        String doneMarker = isDone ? "X] " : " ] ";
        return "[" + doneMarker + taskDescription;
    }
}
