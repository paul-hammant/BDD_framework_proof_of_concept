package newjb.core;

import org.junit.Test;

import static newjb.core.predictable.FailingSteps.failingSteps;
import static newjb.core.predictable.PassingSteps.passingSteps;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ScenarioTest {

    private LittleEventLanguage events = new LittleEventLanguage();
    private StepFactory stepFactory = new DefaultStepFactory();

    @Test
    public void scenarioShouldPassWhenStepsPass() {
        Scenario scenario = scenarioWith(passingSteps("stepz"));
        boolean passed = scenario.perform(stepFactory, events);
        assertThat(passed, is(Boolean.TRUE));
        assertThat(events.eventString(), is(equalTo("Sc( stepz✓ ✓)")));
    }

    @Test
    public void scenarioShouldFailWhenStepsFail() {
        Scenario scenario = scenarioWith(failingSteps("stepz"));
        boolean passed = scenario.perform(stepFactory, events);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(events.eventString(), is(equalTo("Sc( stepz✗ ✗)")));
    }

    public static Scenario scenarioWith(Steps steps) {
       return new Scenario(steps);
    }
}
