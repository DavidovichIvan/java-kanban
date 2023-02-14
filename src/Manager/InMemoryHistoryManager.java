//Пояснения.
//Я пришел к выводу, что LinkedHashMap позволяет проще реализовать решение в сравнении с предлагаемым в описании ТЗ LinkedList.
//Заданные требования к истории сюблюдены:
// - ограничений по количеству записей нет;
// - повторные просмотры не сохраняются, а обновляется существующая запись по ключу;
// - добавление (в том числе обновление существущего) объекта в историю осуществляется в конец мапы (и возвращаемого списка);
// - обращение к объектам истории осуществляется по их id (ключ в мапе), то есть за О(1);
// - при удалении задач/подзадач записи о них в истории также удаляются;
// - метод getHistoryList возвращает ArrayList, сформированный на основании значений LinkedHashMap.

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