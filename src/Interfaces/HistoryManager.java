package Interfaces;

import Model.TemplateTask;

import java.util.List;

public interface HistoryManager {

    /**
     * Method for updating History of views (historyList)
     */
    void updateHistoryList(TemplateTask Task);

    void removeFromHistory(int id);

    List<TemplateTask> getHistoryList();
}