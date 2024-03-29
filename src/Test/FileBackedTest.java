package Test;

import Manager.FileBackedTasksManager;
import Manager.Managers;
import Model.SubTask;
import Model.Task;
import Model.TemplateTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTest {

    FileBackedTasksManager taskManagerBackupTest;
    final String testDataPath = "C:\\Users\\Вуня\\Desktop\\dev\\6th sprint\\java-kanban\\src\\DataStorage\\testData.csv";

    final String testDataPath2 = "C:\\Users\\Вуня\\Desktop\\dev\\6th sprint\\java-kanban\\src\\DataStorage\\testData2.csv";

    final String testHistoryPath = "C:\\Users\\Вуня\\Desktop\\dev\\6th sprint\\java-kanban\\src\\DataStorage\\History.csv";
    final String testDirPath =  "C:\\Users\\Вуня\\Desktop\\dev\\6th sprint\\java-kanban\\src\\DataStorage";

    @BeforeEach
    public void beforeEach() {
        taskManagerBackupTest =
                (FileBackedTasksManager) Managers.getManagerWithBackup(
                        testDirPath);
    }

    @Test
    public void shouldCreateNewFileByTheGivenPath() throws IOException {

        if (Files.exists(Path.of(testDataPath))) {
            Files.delete(Path.of(testDataPath));
        }

        taskManagerBackupTest.setFileForDataStorage(testDataPath);
        assertTrue(Files.isWritable(Path.of(testDataPath)));
        assertTrue((Files.exists(Path.of(testDataPath))));

    }

    @Test
    public void subTaskShouldBeRepresentedAsString() {
        SubTask subTest = new SubTask("Тестируем", TemplateTask.TaskStatus.NEW);
        String expectedString = subTest.getTaskId() + ",Тестируем,NEW,";

        assertTrue
                (taskManagerBackupTest.subTaskToString(subTest) instanceof String);
        assertTrue
                (taskManagerBackupTest.subTaskToString(subTest).contains(expectedString));

    }

    @Test
    public void taskShouldBeWrittenInFile() throws IOException {
        Task testTask = new Task("Тесты", "Много тестов");
        String expected = testTask.getTaskId() + ",false,Тесты,Много тестов,NEW";

        FileWriter fileWriter = new FileWriter(testDataPath2);
        taskManagerBackupTest.saveTask(testTask, testDataPath2);
        fileWriter.close();

        FileReader fileReader = new FileReader(testDataPath2);
        BufferedReader reader = new BufferedReader(fileReader);
        String actualResult = reader.readLine();
        fileReader.close();
        reader.close();

        assertTrue(actualResult.contains(expected));
    }

    @Test
    public void gettingTaskShouldUpdHistoryFile() throws IOException {
        Task testTask = new Task("История", "Проверяем запись в историю");
        String expected = testTask.getTaskId() + ",false,История,Проверяем запись в историю,NEW";

        taskManagerBackupTest.createNewTask(testTask);
        taskManagerBackupTest.createNewTask(new Task());
        taskManagerBackupTest.getTaskById(testTask.getTaskId());

        FileReader fileReader = new FileReader(testHistoryPath);
        BufferedReader reader = new BufferedReader(fileReader);
        String actualResult = reader.readLine();
        fileReader.close();
        reader.close();
        assertTrue(actualResult.contains(expected));
    }

    @Test
    public void loadingTaskManagerFromFile() throws IOException {

                assertTrue
                (taskManagerBackupTest.getAllTasksList().isEmpty());
                 assertTrue
                (taskManagerBackupTest.getHistory().getHistoryList().isEmpty());
        taskManagerBackupTest = FileBackedTasksManager.loadFromFile(new File(testDirPath));

        assertFalse(taskManagerBackupTest.getAllTasksList().isEmpty());
        assertFalse(taskManagerBackupTest.getHistory().getHistoryList().isEmpty());

        for (Object t: taskManagerBackupTest.getAllTasksList().values()) {
            assertTrue(t instanceof Task);
               }
        for (Object t: taskManagerBackupTest.getHistory().getHistoryList()) {
            assertTrue(t instanceof TemplateTask);
        }

    }

    @Test
    public void shouldCreateSubTaskFromString()  {

        String str =
                "7,Имя подзадачи,NEW,2023-03-23T11:52:04.254663Z,2023-03-24T16:53:44.375662100Z,PT20M,2023-03-24T17:13:44.375662100Z";

        SubTask s = FileBackedTasksManager.loadSubTaskFromFile(str);
        int expectedId = 7;
        String expectedName = "Имя подзадачи";

        assertEquals(s.getSubTaskId(), expectedId);
        assertEquals(s.getName(), expectedName);
        assertSame(s.getTaskStatus(), TemplateTask.TaskStatus.NEW);
    }

}

