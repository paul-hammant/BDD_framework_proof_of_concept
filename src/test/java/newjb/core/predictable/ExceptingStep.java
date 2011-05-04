package newjb.core.predictable;

import newjb.core.Monitor;
import newjb.core.Step;
import newjb.core.StepFactory;

public class ExceptingStep extends Step {

    private final String id;

    public ExceptingStep(String id) {
        this.id = id;
    }

    public boolean perform(StepFactory stepFactory, Monitor monitor) {
        monitor.failed(id, new RuntimeException("rExcpt"));
        return false;
    }

    @Override
    public void dontPerform(StepFactory stepFactory, Monitor monitor) {
        monitor.notPerformed(id);
    }


    public static Step exceptingStep(String id) {
        return new ExceptingStep(id);
    }
}
