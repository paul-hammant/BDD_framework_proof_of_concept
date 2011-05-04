package newjb.core.predictable;

import newjb.core.Scenario;
import newjb.core.StepFactory;
import newjb.core.Monitor;

public class FailingScenario extends Scenario {

    private final String id;

    public FailingScenario(String id) {
        super(null);
        this.id = id;
    }

    @Override
    public boolean perform(StepFactory stepFactory, Monitor monitor) {
        monitor.failed(id);
        return false;
    }

    public static Scenario failingScenario(String id) {
        return new FailingScenario(id);
    }
}
