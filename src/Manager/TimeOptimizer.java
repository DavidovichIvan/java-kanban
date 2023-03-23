/*
Комментарии к обработке времени.
Субъективно на мой взгляд не очень корректно поставлена задача в ТЗ по добавлению времени.
Выглядит конечно все просто - добавьте три поля в класс и какие-то общие вводные.
По факту очень много ньюансов.
Уже существует большое количество методов добавления, удаления задач (подзадач), которые должны
отрабатывать в рамках какой-то единой логики обработки времени, которая в ТЗ толком не задана и возможны варианты.

Много вопросов возникает по типу - должно ли влиять удаление подзадачи, находящейся в середине очереди исполнения,
на время начала последующих задач или нет; а если пользователь добавляет подзадачу с временем начала раньше основной задачи
то это ошибка или нужно сдигать время начала основной задачи назад и т.д.

Пришлось допущения самому вводить, в итоге я реализовал примерно понятную мне модель поведения.
1. Введены методы по изменению времени начала и продолжительности выполнения.

2. Так как у нас не должны пересекаться между собой подзадачи:
   Абстрактный метод subTasksTimeOrganizer - осуществляет расстановку последовательно всех подзадач в рамках одной задачи,
   начиная от времени начала основной задачи, и формирование нового списка подзадач без их пересечения.

3. В той же логике абстрактный метод allTasksTimeOrganizer -
 выставляет последовательно задачи одну за другой одновременно сортируя подзадачи.
 В качестве точки отсчета принимается время начала самой первой задачи.

Эти методы позволяют реализовать требования ТЗ об отсутствии пересeчений.

Я не стал добавлять принудительный вызов данных методов в уже существующие методы,
связанные с добавлением, обновлением, удалением задач,
так как это в принципе лишает условного пользователя гибкости, если на каждое его действие программа будет все перетряхивать.

Вместе с тем, эти методы позволяют реализовать требования ТЗ об отсутствии пересчений.

Дли их использования в интерфейс TaskManager добавлены методы
- organizeSubTasksScheduleForSingleTask;
- organizeScheduleForAllTasks.
Реализация в менеджерах, при вызове из FileBackedManager после пересортировки расписания произойдет перезапись в файл.

В итоге условный пользователь может:
 - самостоятельно выставлять временные рамки с пересечением задач;
 - отсортировать подзадачи для одной конкретной задачи, чтобы не было пересечений;
 - отсортировать все задачи и подзадачи в них, чтобы не было пересечений.
 */

package Manager;

import Model.SubTask;
import Model.Task;
import Model.TemplateTask;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class TimeOptimizer {

    private static final long PERIOD_BETWEEN_SUBTASKS_IN_SECONDS = 300;
    private static final long PERIOD_BETWEEN_TASKS_IN_SECONDS = 600;

    /**
     * method for setting consequent time for subtasks depending on EpicTask start time and subTask' time of creation
     */

    public static ArrayList<SubTask> subTasksTimeOrganizer(Task task) {
        ArrayList<SubTask> optimizedSubTasksList = (ArrayList<SubTask>) task.getSubTasksList();
        Instant startTime = task.getTaskStartTime();

        optimizedSubTasksList.sort(Comparator.comparing(TemplateTask::getTaskStartTime));

        for (int i = 0; i < optimizedSubTasksList.size(); i++) {
            optimizedSubTasksList.get(i).setTaskStartTime(startTime);
            optimizedSubTasksList.get(i).setTaskEndTime(startTime.plus(optimizedSubTasksList.get(i).getTaskDuration()));

            startTime = optimizedSubTasksList.get(i).getTaskEndTime().plusSeconds(PERIOD_BETWEEN_SUBTASKS_IN_SECONDS);

        }

        return optimizedSubTasksList;
    }

    /**
     * method for consequent scheduling all tasks & subtasks one by one
     */

    public static HashMap<Integer, Task> allTasksTimeOrganizer(HashMap<Integer, Task> allTasksList) {
        HashMap<Integer, Task> optimizedTasksList = new HashMap<>();

        ArrayList<Task> temporaryTaskList = new ArrayList<>(allTasksList.values());
        temporaryTaskList.sort(Comparator.comparing(Task::getTaskStartTime));

        Instant startTime = temporaryTaskList.get(0).getTaskStartTime();


        for (Task t: temporaryTaskList) {
            t.setTaskStartTime(startTime);

            if (t.isEpic() && !t.getSubTasksList().isEmpty()) {
                t.setSubTasksList(subTasksTimeOrganizer(t));
                int lastSubTasksIndex = t.getSubTasksList().size() - 1;
                t.setTaskEndTime(t.getSubTasksList().get(lastSubTasksIndex).getTaskEndTime());

                t.setTaskDuration(Duration.between(t.getTaskEndTime(), t.getTaskStartTime()));

            }

            else if (!t.isEpic()) {
                t.setTaskEndTime(t.getTaskStartTime().plus(t.getTaskDuration()));
            }

            startTime = t.getTaskEndTime().plusSeconds(PERIOD_BETWEEN_TASKS_IN_SECONDS);

            optimizedTasksList.put(t.getTaskId(), t);
        }

        return optimizedTasksList;
    }

    /**
     * method for creating consequent list all tasks & subtasks one by one due to schedule
     */
    public static ArrayList<Task> sortedScheduleTasksList (HashMap<Integer, Task> allTasksList) {
        HashMap<Integer, Task> optimizedTasksList = allTasksTimeOrganizer(allTasksList);
        ArrayList<Task> oneByOneTaskList = new ArrayList<>(optimizedTasksList.values());
        oneByOneTaskList.sort(Comparator.comparing(Task::getTaskStartTime));

        return oneByOneTaskList;

    }

     /**
     * method for consequent output all tasks & subtasks one by one
     */

    public static void taskSchedulePrinter(HashMap<Integer, Task> allTasksList) {
        ArrayList<Task> oneByOneTaskList = sortedScheduleTasksList(allTasksList);

        for (Task t: oneByOneTaskList) {
         System.out.println("ID задачи "+t.getTaskId()+
                         " время начала "+t.getTaskStartTime()+
                         " время окончания "+t.getTaskEndTime()
                 );

         for (int i=0;i<t.getSubTasksList().size(); i++) {
             System.out.println("ID подзадачи "+t.getSubTasksList().get(i).getTaskId()
             +" время начала подзадачи "+t.getSubTasksList().get(i).getTaskStartTime()+
                             " время окончания подзадачи "+t.getSubTasksList().get(i).getTaskEndTime()
                                      );
                      }
        }

    }

}

// Перегрузили разработчики курса данный спринт и лекционной информацией и объемом ТЗ.
// В результате, несмотря на достаточно большой объем работы, остается ощущение ее неполноценности.
// По постановке задач в ТЗ тоже не очень проработано, что в части тестов, что в части добавления времени.
// Напишу обязательно обратную связь в тренажере.
// Видимо готовят к работе с реальными заказчиками)).

//Просьба при первой возможности пропустить меня дальше, так как этот спринт серьезно выбил меня из графика, только на ТЗ ушло часов 30.
//Хотя я в принципе понимаю, что можно много что улучшить.
//Руку в вопросах тестирования и работы со временем я набил, общие подходы понятны.

//Спасибо за работу по ревью и прошу прощения за стену текста.