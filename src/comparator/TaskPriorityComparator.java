package comparator;

import model.Task;

import java.util.Comparator;

//Компаратор сравнивает задачи по приоритету. Если приоритет одинаковый, сравнивает по названию
public class TaskPriorityComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        int comp = Integer.compare(o2.getPriority().getPriorityWeight(), o1.getPriority().getPriorityWeight());
        if (comp != 0) {
            return comp;
        }
        return o1.getTitle().compareToIgnoreCase(o2.getTitle());
    }
}
