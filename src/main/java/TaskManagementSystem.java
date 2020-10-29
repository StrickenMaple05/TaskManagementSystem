import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * task management system
 */
public class TaskManagementSystem extends Exception {

    private static final String commands =
            "1. Add task\n" +
            "2. Modify task\n" +
            "3. Display tasks\n" +
            "4. Delete task\n" +
            "5. Exit\n";

    private static final String modifyCommands =
            "1. Save changes\n" +
            "2. Display task\n" +
            "3. Cancel";

    /**
     * tasks list
     */
    private List<String> tasks;
    /**
     * variable to save task state in the program
     */
    private Task task;


    public TaskManagementSystem() throws IOException, ClassNotFoundException {
        tasks = new ArrayList<>();
        try {
            deserializeTaskList();
        }
        catch(IOException e) {
            File dataDir = new File("src\\data");
            if (!dataDir.exists()) {
                Files.createDirectory(Paths.get("src\\data"));
            }
            serializeTaskList();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        TaskManagementSystem taskManagementSystem = new TaskManagementSystem();
        taskManagementSystem.exec();
    }

    /**
     * Task management system execution
     * @throws IOException ...
     * @throws ClassNotFoundException ...
     */
    public void exec() throws IOException, ClassNotFoundException {
        int command;
        Scanner in = new Scanner(System.in);
        System.out.println("\nWelcome to task management system!\n");
        while (true) {
            System.out.print(commands);
            command = in.nextInt();
            switch (command) {
                case 1: {
                    task = new Task();
                    in = new Scanner(System.in);
                    System.out.print("Name: ");
                    String taskName = in.nextLine();
                    task.setName(taskName);
                    if (tasks.contains(taskName)) {
                        System.out.println("Error! Task already exists!");
                        continue;
                    } else
                    if (taskName.equals("")) {
                        System.out.println("Error! Task name can't be empty!");
                        continue;
                    }
                    System.out.print("Description: ");
                    task.setDescription(in.nextLine());
                    System.out.print("Executor: ");
                    task.setExecutor(in.nextLine());
                    System.out.print("Status: ");
                    task.setStatus(in.nextLine());
                    System.out.print("Code: ");
                    task.setCode(in.nextLine());
                } break;
                case 2: {
                    if (tasks.isEmpty()) {
                        System.out.println("Nothing to modify\n");
                        continue;
                    }
                    in = new Scanner(System.in);
                    String taskName;
                    System.out.print("Input task name: ");
                    taskName = in.nextLine();
                    if (!tasks.contains(taskName)) {
                        System.out.println("Error! Task does not exist!\n");
                        continue;
                    }
                    task = deserializeTask(taskName);
                    System.out.println("// You can modify task executor or status. " +
                            "If you don't want to change some of them, type \"_\" instead//");
                    System.out.print("Executor: ");
                    String executor = in.nextLine();
                    task.setExecutor(executor);
                    System.out.print("Status: ");
                    String status = in.nextLine();
                    task.setStatus(status);
                } break;
                case 3: displayTasks(); break;
                case 4: {
                    in = new Scanner(System.in);
                    if (tasks.isEmpty()) {
                        System.out.println("Nothing to delete\n");
                        break;
                    }
                    System.out.print("Input task name: ");
                    String taskName = in.nextLine();
                    if (!tasks.contains(taskName)) {
                        System.out.println("Error! Task does not exists!\n");
                        break;
                    }
                    deleteTask(taskName);
                    System.out.println("Removal was successful!\n");
                } break;
                case 5: return;
                default: System.out.println("Unknown command, try again");
            }
            if (command == 1 || command == 2) {
                modifyData(task);
            }
        }
    }

    /**
     * method to display all tasks
     * @throws IOException ...
     * @throws ClassNotFoundException ...
     */
    public void displayTasks() throws IOException, ClassNotFoundException {
        if (tasks.isEmpty()) {
            System.out.println("Nothing to display\n");
            return;
        }
        for (String taskName : tasks) {
            System.out.println(
                    deserializeTask(taskName).getInfo() + "\n"
            );
        }
    }

    /**
     * method to modify an existing task
     * @param taskName task name
     * @param executor executor
     * @param status status
     * @throws IOException ...
     * @throws ClassNotFoundException ...
     */
    public void modifyTask(String taskName,
                           String executor,
                           String status)
            throws IOException, ClassNotFoundException {

        if (!tasks.contains(taskName)) {
            System.out.println("Task does not exists");
            return;
        }
        Task task = deserializeTask(taskName);
        task.setExecutor(executor);
        task.setStatus(status);
        serializeTask(task);
    }

    /**
     * method to interact with task which is being modified. Functions: serialize, display, cancel
     * @param task task which is being modified
     * @throws IOException ...
     */
    public void modifyData(Task task) throws IOException {
        Scanner in = new Scanner(System.in);
        int command;
        while (true) {
            System.out.println(modifyCommands);
            command = in.nextInt();
            if (command != 2) break;
            System.out.println(task.getInfo() + "\n");
        }
        if (command == 3) {
            return;
        }
        serializeTask(task);
        System.out.println("Serialization was successful!\n");
    }

    /**
     * method to delete an existing task
     * @param taskName task name
     * @throws IOException ...
     */
    public void deleteTask(String taskName) throws IOException {

        File file = new File(filePath(taskName));
        tasks.remove(taskName);
        serializeTaskList();
        file.delete();
    }

    /**
     * method to serialize a task
     * @param task task
     * @throws IOException ...
     */
    public void serializeTask(Task task) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(filePath(task.getName()));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(task);
        objectOutputStream.close();
        if (!tasks.contains(task.getName())) {
            tasks.add(task.getName());
            serializeTaskList();
        }
    }

    /**
     * method to deserialize a task from file
     * @param fileName file path
     * @return task
     * @throws IOException ...
     * @throws ClassNotFoundException ...
     */
    public Task deserializeTask(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream inputStream = new FileInputStream(filePath(fileName));
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Task task = (Task) objectInputStream.readObject();
        objectInputStream.close();
        return task;
    }

    /**
     * method to serialize task list
     * @throws IOException ...
     */
    public void serializeTaskList() throws IOException {
        FileOutputStream outputStream = new FileOutputStream(filePath("__taskList"));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(tasks);
        objectOutputStream.close();
    }

    /**
     * method to deserialize task list from its file
     * @throws IOException ...
     * @throws ClassNotFoundException ...
     */
    public void deserializeTaskList() throws IOException, ClassNotFoundException {
        FileInputStream inputStream = new FileInputStream(filePath("__taskList"));
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        tasks = (List<String>) objectInputStream.readObject();
        objectInputStream.close();
    }

    /**
     * method to create path to the new file
     * @param name file name
     * @return file path
     */
    public static String filePath(String name) {
        return "src\\data\\" + name + ".ser";
    }

    public List<String> getTasks() {
        return tasks;
    }
}
