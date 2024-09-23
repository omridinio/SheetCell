package Properties;
import javafx.concurrent.Task;

public class TaskLoadFile extends Task<Boolean> {
    @Override
    protected Boolean call() throws Exception {
        for (int i = 0; i < 100; i++) {
            updateProgress(i + 1, 100);
            Thread.sleep(30);
        }
        return true;
    }
}
