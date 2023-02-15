//Убрал пустую строку.
//В методе Managers.getDefaultHistory() в сигнатуре изменил тип на интерфейс HistoryManager и сопутствующие изменения внес.
//По скорости обработки данных разъяснения понял примерно, спасибо.

package Manager;

import Interfaces.HistoryManager;
import Model.SubTask;
import Model.Task;
import Model.TemplateTask;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Integer, TemplateTask> historyList = new LinkedHashMap(50, 0.8F, true);

    @Override
    public void updateHistoryList(TemplateTask task) {
        historyList.put(task.getTaskId(), task);
    }

    public Map<Integer, TemplateTask> getHistoryMap() {
        return historyList;
    }

    @Override
    public List<TemplateTask> getHistoryList() {
        return new ArrayList<>(historyList.values());
    }

    public void removeFromHistory(int id) {

        if (historyList.get(id) instanceof Task && ((Task) historyList.get(id)).isEpic()) {
            for (SubTask sub : ((Task) historyList.get(id)).getSubTasksList()) {
                historyList.remove(sub.getSubTaskId());
            }

        }
        historyList.remove(id);
    }

}

//Спасибо за работу по ревью.