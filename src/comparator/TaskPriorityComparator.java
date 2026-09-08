package comparator;

import model.Task;

import java.util.Comparator;

public class TaskPriorityComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        return Integer.compare(o2.getPriority().getPriorityWeight(), o1.getPriority().getPriorityWeight());
    }
}
