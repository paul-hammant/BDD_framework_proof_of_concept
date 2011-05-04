package newjb.core.predictable;

import newjb.core.Monitor;
import newjb.core.StepFactory;
import newjb.core.Stories;

public class FailingStories extends Stories {

    private final String id;

    public FailingStories(String id) {
        super(null);
        this.id = id;
    }

    @Override
    public boolean perform(StepFactory stepFactory, Monitor monitor) {
        monitor.failed(id);
        return false;
    }

    public static Stories failingStories(String id) {
        return new FailingStories(id);
    }
}
