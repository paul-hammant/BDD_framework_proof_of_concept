package newjb.core.predictable;

import newjb.core.Step;
import newjb.core.StepFactory;
import newjb.core.Monitor;

public class PassingStep extends Step {

    private final String id;

    public PassingStep(String id) {
        this.id = id;
    }

    public boolean perform(StepFactory stepFactory, Monitor monitor) {
        monitor.passed(id);
        return true;
    }

    @Override
    public void dontPerform(StepFactory stepFactory, Monitor monitor) {
        monitor.notPerformed(id);
    }


    public static Step passingStep(String id) {
        return new PassingStep(id);
    }
}
