package newjb.core;

import org.junit.Test;

import static newjb.core.predictable.AssertingStep.assertingStep;
import static newjb.core.predictable.FailingStory.failingStory;
import static newjb.core.predictable.PassingStep.passingStep;
import static newjb.core.predictable.PassingStory.passingStory;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BeforeAfterStoryTest {

    private LittleEventLanguage monitor = new LittleEventLanguage();
    private StepFactory stepFactory = new DefaultStepFactory();

    @Test
    public void storyShouldPassWhenBeforeAfterAndScenarioPasses() {
        Story.BeforeOrAfter bas = new Story.BeforeOrAfter(passingStory("a"))
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        boolean passed = bas.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("<✓ a✓ >✓")));
    }

    @Test
    public void storyShouldFailWhenBeforeAfterBothPassButScenarioFails() {
        Story.BeforeOrAfter bas = new Story.BeforeOrAfter(failingStory("a"))
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        boolean passed = bas.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("<✓ a✗ >✓")));
    }

    @Test
    public void storyShouldFailWhenBeforeFailsAndScenarioShouldBeOmitted() {
        Story.BeforeOrAfter bas = new Story.BeforeOrAfter(passingStory("a"))
                .withBeforeStep(assertingStep("<"))
                .withAfterStep(passingStep(">"));
        boolean passed = bas.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("<(aErr)")));
    }

    @Test
    public void storyShouldFailWhenBeforePassesAfterFailsAndScenarioPasses() {
        Story.BeforeOrAfter bas = new Story.BeforeOrAfter(passingStory("a"))
                .withBeforeStep(passingStep("<"))
                .withAfterStep(assertingStep(">"));
        boolean passed = bas.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("<✓ a✓ >(aErr)")));
    }

}
