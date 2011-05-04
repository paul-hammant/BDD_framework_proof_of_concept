package newjb.core.predictable;

import newjb.core.Monitor;
import newjb.core.Scenario;
import newjb.core.StepFactory;
import newjb.core.Story;

public class PassingStory extends Story {

    private final String id;

    public PassingStory(String id) {
        super(new Scenario[0]);
        this.id = id;
    }

    @Override
    public boolean perform(StepFactory stepFactory, Monitor monitor) {
        monitor.passed(id);
        return true;
    }

    public static Story passingStory(String id) {
        return new PassingStory(id);
    }
}
