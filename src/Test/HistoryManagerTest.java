package Test;

import Interfaces.HistoryManager;
import Manager.InMemoryHistoryManager;
import Model.SubTask;
import Model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    HistoryManager historyTest;
    Task testTask1 = new Task();
    Task testTask2 = new Task();
    SubTask testTask3 = new SubTask();
    SubTask testTask4 = new SubTask();

    private final int NON_EXISTING_TASK_ID = -99;

    int index;

    @BeforeEach
    public void beforeEach() {
        historyTest = new InMemoryHistoryManager();
        index = 0;
    }

    @Test
    public void shouldUpdHistoryListButCouldContainEveryObjOnlyOnce() {
        assertTrue
                (historyTest.getHistoryList().isEmpty(),
                "Новый объект создается с пустым списком истории.");
        historyTest.updateHistoryList(testTask1);
        index++;

        assertFalse
                (historyTest.getHistoryList().isEmpty());
        assertEquals
                (historyTest.getHistoryList().size(), index, "Новый объект добавлен в историю.");

        for (int i = 0; i <= 10; i++) {
            historyTest.updateHistoryList(testTask1);
        }
        assertEquals
                (historyTest.getHistoryList().size(), index,
                "Один объект в истории содержится только один раз. Дублирования не происходит.");
        historyTest.updateHistoryList(testTask2);
        index++;
        historyTest.updateHistoryList(testTask3);
        index++;
        historyTest.updateHistoryList(testTask4);
        index++;
        assertEquals
                (historyTest.getHistoryList().size(), index, "Разные объекты добавляются в историю.");

        assertEquals
                (0, historyTest.getHistoryList().indexOf(testTask1),
                "Индекс первого добавленного в историю элемента равен нулю.");
        assertEquals
                (1, historyTest.getHistoryList().indexOf(testTask2),
                "Индекс второго добавленного в историю элемента равен единице.");
        historyTest.updateHistoryList(testTask1);
        assertEquals
                (historyTest.getHistoryList().indexOf(testTask1), historyTest.getHistoryList().size() - 1,
                        "После повторного просмотра элемент добавляется в конец списка.");
        assertEquals
                (0, historyTest.getHistoryList().indexOf(testTask2),
                "Индекс элементов сдвигается в случае перезаписи предществующих элементов в конец списка.");

    }

    @Test
    public void shouldRemoveObjFromHistory() {
        historyTest.updateHistoryList(testTask1);
        historyTest.updateHistoryList(testTask2);
        historyTest.updateHistoryList(testTask3);
        historyTest.updateHistoryList(testTask4);

        int expectedhistorySize = 4;
        assertEquals
                (historyTest.getHistoryList().size(), expectedhistorySize);

        assertTrue
                (historyTest.getHistoryList().contains(testTask1), "Объект записан в историю");
        assertTrue
                (historyTest.getHistoryList().contains(testTask2), "Объект записан в историю");

        historyTest.removeFromHistory(testTask1.getTaskId());
        historyTest.removeFromHistory(testTask4.getTaskId());
        expectedhistorySize = 2;
        assertEquals
                (historyTest.getHistoryList().size(), expectedhistorySize);

        assertFalse
                (historyTest.getHistoryList().contains(testTask1), "Объекта больше нет в истории");
        assertFalse
                (historyTest.getHistoryList().contains(testTask4), "Объекта больше нет в истории");

        historyTest.removeFromHistory(NON_EXISTING_TASK_ID);
        assertTrue
                (historyTest.getHistoryList().contains(testTask2), "" +
                "Объект по прежнему записан в историю");
        assertTrue
                (historyTest.getHistoryList().contains(testTask3),
                "Объект по прежнему записан в историю");
        assertEquals
                (historyTest.getHistoryList().size(), expectedhistorySize,
                "При передаче в метод несущствующего ID, ничего не происходит.");

        int currentPlaceInHistory = historyTest.getHistoryList().indexOf(testTask3);
        historyTest.removeFromHistory(testTask2.getTaskId());
        int newExpectedPlace = currentPlaceInHistory - 1;

        assertEquals
                (historyTest.getHistoryList().indexOf(testTask3), newExpectedPlace,
                "При удалении предшествующего элемента последующий элемент сдвигается на его место.");

    }


}
