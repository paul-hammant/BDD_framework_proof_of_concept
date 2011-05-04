package newjb.core;

import org.junit.Before;
import org.junit.Test;

import static newjb.core.predictable.AssertingStep.assertingStep;
import static newjb.core.predictable.ExceptingStep.exceptingStep;
import static newjb.core.predictable.PassingStep.passingStep;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StepsTest {

    private LittleEventLanguage monitor;
    private StepFactory stepFactory;

    @Before
    public void setup() {
        monitor = new LittleEventLanguage();
        stepFactory = new DefaultStepFactory();
    }

    @Test
    public void stepsCanHaveSteps() {
        Steps steps = new Steps(passingStep("a"), passingStep("b"), passingStep("c"));
        boolean passed = steps.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("a✓ b✓ c✓")));
    }

    @Test
    public void stepsCanHaveThrowingStepsThatMeanFollowingStepsAreNotPerformed() {
        Steps steps = new Steps(passingStep("a"), exceptingStep("b"), passingStep("c"));
        boolean passed = steps.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("a✓ b(rExcpt) c-")));
    }

    @Test
    public void stepsCanHaveAssertingStepsThatMeanFollowingStepsAreNotPerformed() {
        Steps steps = new Steps(passingStep("a"), assertingStep("b"), passingStep("c"));
        boolean passed = steps.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("a✓ b(aErr) c-")));
    }



}
