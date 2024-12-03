import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {
    @Test
    public void shouldAlwaysBeReady() {
        TaskManager manager1 = Managers.getDefault();
        TaskManager manager2 = Managers.getDefault();

        HistoryManager historyManager1 = Managers.getDefaultHistory();
        HistoryManager historyManager2 = Managers.getDefaultHistory();

        assertNotNull(manager1);
        assertNotNull(manager2);

        assertNotNull(historyManager1);
        assertNotNull(historyManager2);
    }
}
