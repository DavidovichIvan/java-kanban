package Interfaces;

import Model.TemplateTask;

import java.util.ArrayList;

public interface HistoryManager {

    /**
     * Method for updating History of views (historyList)
     */
    void updateHistoryList(TemplateTask T);

    /**
     * Method for getting history list
     */
    ArrayList<TemplateTask> getHistoryList();


}
