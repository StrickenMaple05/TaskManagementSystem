import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskManagementSystemTest {

    private TaskManagementSystem taskManagementSystem;
    private List<String> tasks;
    private Task task1;
    private Task task2;
    private final File file1 = new File("src\\data\\1.ser");
    private final File file2 = new File("src\\data\\2.ser");
    private final File taskList = new File("src\\data\\__taskList.ser");

    @BeforeEach
    public void init() throws IOException, ClassNotFoundException {
        taskManagementSystem = new TaskManagementSystem();
        task1 = new Task("1","1","1","1","1");
        task2 = new Task("2","2","2","2","2");
        tasks = new ArrayList<>();
    }

    @AfterEach
    public void clear() {
        file1.delete();
        file2.delete();
        taskList.delete();
        tasks.clear();
    }

    @Test
    public void addTaskTest() throws IOException, ClassNotFoundException {
        taskManagementSystem.serializeTask(task1);
        tasks.add("1");
        Assertions.assertEquals(task1.getInfo(),
                taskManagementSystem.deserializeTask("1").getInfo());
        Assertions.assertEquals(tasks, taskManagementSystem.getTasks());

        taskManagementSystem.serializeTask(task2);
        tasks.add("2");
        Assertions.assertEquals(task2.getInfo(),
                taskManagementSystem.deserializeTask("2").getInfo());
        Assertions.assertEquals(tasks, taskManagementSystem.getTasks());
    }

    @Test
    public void modifyTaskTest() throws IOException, ClassNotFoundException {
        taskManagementSystem.serializeTask(task1);
        taskManagementSystem.modifyTask("1","2","2");
        task1.setExecutor("2");
        task1.setStatus("2");
        Assertions.assertEquals(task1.getInfo(),
                taskManagementSystem.deserializeTask("1").getInfo());
    }

    @Test
    public void deleteTaskTest() throws IOException, ClassNotFoundException {
        taskManagementSystem.serializeTask(task1);
        Assertions.assertTrue(file1.exists());
        tasks.add("1");
        Assertions.assertEquals(tasks,taskManagementSystem.getTasks());

        taskManagementSystem.deleteTask("1");
        tasks.clear();
        Assertions.assertFalse(file1.exists());
        Assertions.assertEquals(tasks,taskManagementSystem.getTasks());
    }
}
