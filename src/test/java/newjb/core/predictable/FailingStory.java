package newjb.core.predictable;

import newjb.core.Monitor;
import newjb.core.StepFactory;
import newjb.core.Story;

public class FailingStory extends Story {

    private final String id;

    public FailingStory(String id) {
        super(null);
        this.id = id;
    }

    @Override
    public boolean perform(StepFactory stepFactory, Monitor monitor) {
        monitor.failed(id);
        return false;
    }

    public static Story failingStory(String id) {
        return new FailingStory(id);
    }
}
