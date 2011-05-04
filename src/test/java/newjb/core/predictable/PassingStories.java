package newjb.core.predictable;

import newjb.core.Monitor;
import newjb.core.StepFactory;
import newjb.core.Stories;

public class PassingStories extends Stories {

    private final String id;

    public PassingStories(String id) {
        this.id = id;
    }

    @Override
    public boolean perform(StepFactory stepFactory, Monitor monitor) {
        monitor.passed(id);
        return true;
    }

    public static Stories passingStories(String id) {
        return new PassingStories(id);
    }
}
