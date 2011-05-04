package newjb.core;

public class Scenario implements Performable {

    private final Steps steps;
    private String name = "Sc";

    public Scenario(Steps steps) {
        this.steps = steps;
    }

    public boolean perform(StepFactory stepFactory, Monitor monitor) {
        monitor.startScenario(this);
        boolean passed = steps.perform(stepFactory, monitor);
        monitor.endScenario(passed);
        return passed;
    }

    public String getName() {
        return name;
    }

    public static class BeforeOrAfter extends Scenario {
        private final Scenario scenario;
        private Step beforeStep;
        private Step afterStep;

        public BeforeOrAfter(Scenario scenario) {
            super(null);
            this.scenario = scenario;
        }

        public BeforeOrAfter withBeforeStep(Step step) {
            beforeStep = step;
            return this;
        }

        public BeforeOrAfter withAfterStep(Step step) {
            afterStep = step;
            return this;
        }


        public boolean perform(StepFactory stepFactory, Monitor monitor) {

            boolean state = true;

            if (beforeStep != null) {
                state = beforeStep.perform(stepFactory, monitor);
            }

            if (!state) {
                return false;
            }

            state = scenario.perform(stepFactory, monitor);

            boolean afterState = true;
            if (afterStep != null) {
                afterState = afterStep.perform(stepFactory, monitor);
            }

            return state && afterState;
        }

    }
}
