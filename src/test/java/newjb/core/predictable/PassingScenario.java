package newjb.core.predictable;

import newjb.core.Monitor;
import newjb.core.Scenario;
import newjb.core.Step;
import newjb.core.StepFactory;
import newjb.core.Steps;

public class PassingScenario extends Scenario {

    private final String id;

    public PassingScenario(String id) {
        super(new Steps(new Step[0]));
        this.id = id;
    }

    @Override
    public boolean perform(StepFactory stepFactory, Monitor monitor) {
        monitor.passed(id);
        return true;
    }

    public static Scenario passingScenario(String id) {
        return new PassingScenario(id);
    }
}
