//Евгений, спасибо за работу

package Manager;

import Interfaces.HistoryManager;
import Model.TemplateTask;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_HISTORYLIST_LENGTH = 10;
    private ArrayList<TemplateTask> historyList = new ArrayList<>();

    @Override
    public void updateHistoryList(TemplateTask task) {

            historyList.add(0, task);

        while (historyList.size() > MAX_HISTORYLIST_LENGTH) {
            historyList.remove(historyList.size() - 1);
        }
    }
        @Override
        public ArrayList<TemplateTask> getHistoryList() { return historyList; }

}
