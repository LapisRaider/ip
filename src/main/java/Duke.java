import java.util.Scanner;
import java.util.ArrayList;
import java.util.StringTokenizer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Duke {
    private boolean isRunning;
    private ArrayList<Task> userTexts;

    public static void main(String[] args) {
        Duke duke = new Duke();

        duke.startGreeting();
        duke.runDuke();
    }

    public Duke() {
        this.userTexts = new ArrayList<Task>();
        this.isRunning = true;
    }

    /* Initial greeting for Duke */
    public void startGreeting() {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);

        printDukeResponse("Sup! Name's Duke \nHow can I help you today?");
    }

    /* Run Duke default behavior */
    public void runDuke() {
        Scanner sc = new Scanner(System.in);

        while (this.isRunning) {
            String userResponse = sc.nextLine();

            try {
                commandsParsed(userResponse);
            } catch (DukeException error) {
                printDukeResponse(error.getMessage());
            }

        }

        sc.close();
    }

    /* Return true if command is successfully parsed */
    public void commandsParsed(String input) throws DukeException {
        StringTokenizer st = new StringTokenizer(input, " ");
        String firstCommand = st.nextToken();

        // quit application
        if (firstCommand.equals("bye")) {
            printDukeResponse("See ya!");
            this.isRunning = false;
            return;
        }

        // print task list
        if (firstCommand.equals("list")) {
            printDukeResponse("Here are the tasks in your list: \n" + getListStr(userTexts));
            return;
        }

        // mark task
        if (firstCommand.equals("mark") || firstCommand.equals("unmark")) {
            boolean markTask = firstCommand.equals("mark");
            int taskIndex = 0;

            try {
                taskIndex = Integer.parseInt(st.nextToken()) - 1;
            } catch (NumberFormatException error) {
                throw new DukeException("Invalid input, you need to give a number/integer");
            }

            if (taskIndex >= userTexts.size()) {
                throw new DukeException("Yo yo yo, this task don't exist. Give a legitimate task number.");
            }

            Task task = userTexts.get(taskIndex);
            task.setIsDone(markTask);
            saveTaskList(userTexts);
            String cmdDescription = markTask ? "Nice I've marked this task as done: \n"
                    : "Alright, I've unmarked the task: \n ";

            printDukeResponse(cmdDescription + task.toString());
            return;
        }

        // for adding the diff types of tasks
        if (firstCommand.equals("todo") || firstCommand.equals("deadline") || firstCommand.equals("event")) {
            Task newTask = null;

            String exceptionErrPrint = "The description of your task cannot be empty.";
            try {
                String taskDescription = input.substring(input.indexOf(firstCommand) + firstCommand.length() + 1);

                if (taskDescription.length() == 0) {
                    throw new DukeException(exceptionErrPrint);
                }

                // init the correct task type
                if (firstCommand.equals("deadline")) {
                    exceptionErrPrint = "Did you remember to put in the deadline after /by? Or did u remember to add /by?";
                    newTask = new Deadline(taskDescription.substring(0, taskDescription.indexOf(" /by")),
                            taskDescription.substring(taskDescription.indexOf("/by") + 4));

                } else if (firstCommand.equals("event")) {
                    exceptionErrPrint = "Did you remember to put in the timing after /at? Or did u remember to add /at?";
                    newTask = new Event(taskDescription.substring(0, taskDescription.indexOf(" /at")),
                            taskDescription.substring(taskDescription.indexOf("/at") + 4));

                } else {
                    newTask = new Todo(taskDescription);
                }

            } catch (StringIndexOutOfBoundsException error) {
                throw new DukeException(
                        "Something is wrong. " + exceptionErrPrint);
            }

            this.userTexts.add(newTask);
            saveTaskList(userTexts);
            String printStr = "Gotcha. Added the task: \n   " + newTask.toString()
                    + "\nNow you have " + String.valueOf(this.userTexts.size()) + " tasks in your list.";

            printDukeResponse(printStr);
            return;
        }

        if (firstCommand.equals("delete")) {
            int taskIndex = 0;
            try {
                taskIndex = Integer.parseInt(st.nextToken()) - 1;
            } catch (NumberFormatException error) {
                throw new DukeException("Invalid input, you need to give a number/integer");
            }

            if (taskIndex >= userTexts.size()) {
                throw new DukeException("Yo yo yo, this task don't exist. Give a legitimate task number.");
            }

            Task task = userTexts.get(taskIndex);
            userTexts.remove(taskIndex);
            saveTaskList(userTexts);
            printDukeResponse("Got it, task has been removed: \n" + task.toString() + "\nNow you have "
                    + String.valueOf(this.userTexts.size()) + " tasks in your list.");

            return;
        }

        throw new DukeException("HEY! I don't know what this mean, command doesn't exist.");
    }

    /* Load task list from file saved */
    public void loadFromSave(ArrayList<Task> taskList) {

    }

    /* save task list to a file */
    public void saveTaskList(ArrayList<Task> taskList) {
        String filePath = "src/main/data";
        String fileName = "duke.txt";

        File directory = new File(filePath);
        try {
            // create directory if dont exist
            if (!directory.exists()) {
                directory.mkdir();
            }

            // create the file if it dont exist
            File file = new File(filePath + "/" + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            // write data to file
            FileWriter fileWriter = new FileWriter(file);
            for (Task task : taskList) {
                fileWriter.write(task.saveFileFormat());
            }
            fileWriter.close();

        } catch (IOException exception) {
            System.out.println("Something wrong here");
            return;
        }
    }

    /* Print in the Duke response format */
    public void printDukeResponse(String response) {
        System.out.println(
                "\n--------------------------------------------------------------------------------------------");
        System.out.println("Duke Speaking:\n");
        System.out.println(response);
        System.out.println(
                "--------------------------------------------------------------------------------------------\n");
    }

    public String getListStr(ArrayList<? extends Object> list) {
        StringBuilder sb = new StringBuilder("");

        for (int i = 0; i < list.size(); ++i) {
            sb.append(String.valueOf(i + 1)).append(". ").append(list.get(i).toString()).append("\n");
        }

        return sb.toString();
    }
}
