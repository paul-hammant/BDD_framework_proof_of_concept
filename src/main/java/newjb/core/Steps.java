package newjb.core;

public class Steps implements Performable {

    private Step[] steps;

    public Steps(Step... steps) {
        this.steps = steps;
    }

    public boolean perform(StepFactory stepFactory, Monitor monitor) {
        boolean state = true;
        for (Step step : steps) {
            if (state == true) {
                state = step.perform(stepFactory, monitor);
            } else {
                step.dontPerform(stepFactory, monitor);
            }
        }
        return state;

    }

}
