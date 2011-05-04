package newjb.core.predictable;

import newjb.core.Monitor;
import newjb.core.StepFactory;
import newjb.core.Steps;

public class FailingSteps extends Steps {

    private final String id;

    public FailingSteps(String id) {
        this.id = id;
    }

    @Override
    public boolean perform(StepFactory stepFactory, Monitor monitor) {
        monitor.failed(id);
        return false;
    }

    public static Steps failingSteps(String id) {
        return new FailingSteps(id);
    }
}
