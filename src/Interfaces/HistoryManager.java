package Interfaces;

import Model.TemplateTask;

import java.util.List;
import java.util.Map;

public interface HistoryManager {

    /**
     * Method for updating History of views (historyList)
     */
    void updateHistoryList(TemplateTask Task);

    void removeFromHistory(int id);

    List<TemplateTask> getHistoryList();

    Map<Integer, TemplateTask> getHistoryMap();
}