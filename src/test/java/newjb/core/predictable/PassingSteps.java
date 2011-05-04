package newjb.core.predictable;

import newjb.core.Monitor;
import newjb.core.StepFactory;
import newjb.core.Steps;

public class PassingSteps extends Steps {

    private final String id;

    public PassingSteps(String id) {
        this.id = id;
    }

    @Override
    public boolean perform(StepFactory stepFactory, Monitor monitor) {
        monitor.passed(id);
        return true;
    }

    public static Steps passingSteps(String id) {
        return new PassingSteps(id);
    }
}
