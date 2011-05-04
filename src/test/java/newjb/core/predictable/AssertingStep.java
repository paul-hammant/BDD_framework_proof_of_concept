package newjb.core.predictable;

import newjb.core.Monitor;
import newjb.core.Step;
import newjb.core.StepFactory;

public class AssertingStep extends Step {

    private final String id;

    public AssertingStep(String id) {
        this.id = id;
    }

    public boolean perform(StepFactory stepFactory, Monitor monitor) {
        monitor.failed(id, new AssertionError("aErr"));
        return false;
    }

    @Override
    public void dontPerform(StepFactory stepFactory, Monitor monitor) {
        monitor.notPerformed(id);
    }

    public static Step assertingStep(String id) {
        return new AssertingStep(id);
    }
}
