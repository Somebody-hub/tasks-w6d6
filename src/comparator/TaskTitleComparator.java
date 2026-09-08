package comparator;

import model.Task;

import java.util.Comparator;

public class TaskTitleComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        return o1.getTitle().compareTo(o2.getTitle());
    }
}
