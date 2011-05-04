package newjb.core;

import org.junit.Test;

import static newjb.core.predictable.ExceptingStep.exceptingStep;
import static newjb.core.predictable.FailingStories.failingStories;
import static newjb.core.predictable.PassingStories.passingStories;
import static newjb.core.predictable.PassingStep.passingStep;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BeforeAfterStoriesTest {

    private LittleEventLanguage monitor = new LittleEventLanguage();
    private StepFactory stepFactory = new DefaultStepFactory();

    @Test
    public void shouldPassIfStepsPass() {
        Stories.BeforeOrAfter bas = new Stories.BeforeOrAfter(passingStories("storiesA"));
        boolean passed = bas.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("storiesA✓")));
    }

    @Test
    public void shouldFailWhenStepsFail() {
        Stories.BeforeOrAfter bas = new Stories.BeforeOrAfter(failingStories("storiesA"));
        boolean passed = bas.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("storiesA✗")));
    }

    @Test
    public void shouldPassWhenBeforeStepPasses() {
        Stories.BeforeOrAfter bas = new Stories.BeforeOrAfter(passingStories("storiesA"))
                .withBeforeStep(passingStep("<"));
        boolean passed = bas.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("<✓ storiesA✓")));
    }

    @Test
    public void shouldFastFailWhenBeforeStepFailsAndNotExecuteRealSteps() {
        Stories.BeforeOrAfter bas = new Stories.BeforeOrAfter(passingStories("storiesA"))
                .withBeforeStep(exceptingStep("<"));
        boolean passed = bas.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("<(rExcpt)")));
    }

    @Test
    public void shouldPassWhenAfterStepPasses() {
        Stories.BeforeOrAfter bas = new Stories.BeforeOrAfter(passingStories("storiesA"))
                .withAfterStep(passingStep(">"));
        boolean passed = bas.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("storiesA✓ >✓")));
    }

    @Test
    public void shouldFailWhenAfterStepFails() {
        Stories.BeforeOrAfter bas = new Stories.BeforeOrAfter(passingStories("storiesA"))
                .withAfterStep(exceptingStep(">"));
        boolean passed = bas.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("storiesA✓ >(rExcpt)")));
    }

    @Test
    public void shouldPassWhenBeforeAndAfterStepsPass() {
        Stories.BeforeOrAfter bas = new Stories.BeforeOrAfter(passingStories("storiesA"))
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        boolean passed = bas.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("<✓ storiesA✓ >✓")));
    }

    @Test
    public void shouldFastFailWhenFormerOfBeforeAndAfterStepsFail() {
        Stories.BeforeOrAfter bas = new Stories.BeforeOrAfter(passingStories("storiesA"))
                .withBeforeStep(exceptingStep("<"))
                .withAfterStep(passingStep(">"));
        boolean passed = bas.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("<(rExcpt)")));
    }

    @Test
    public void shouldFailWhenLatterOfBeforeAndAfterStepsFailButStillPreserveDelegateStepsResult() {
        Stories.BeforeOrAfter bas = new Stories.BeforeOrAfter(passingStories("storiesA"))
                .withBeforeStep(passingStep("<"))
                .withAfterStep(exceptingStep(">"));
        boolean passed = bas.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("<✓ storiesA✓ >(rExcpt)")));
    }

    @Test
    public void shouldFailWhenStepsFailButPerformAfterStepAnyway() {
        Stories.BeforeOrAfter bas = new Stories.BeforeOrAfter(failingStories("storiesA"))
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        boolean passed = bas.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("<✓ storiesA✗ >✓")));
    }

    @Test
    public void shouldFailWhenStepsAssertButPerformAfterStepAnyway() {
        Stories.BeforeOrAfter bas = new Stories.BeforeOrAfter(failingStories("storiesA"))
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        boolean passed = bas.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("<✓ storiesA✗ >✓")));
    }

    @Test
    public void shouldPassWhenNestedBeforeAndAfterStepsPass() {
        Stories.BeforeOrAfter bas = new Stories.BeforeOrAfter(passingStories("storiesA"))
                .withBeforeStep(passingStep("["))
                .withAfterStep(passingStep("]"));
        Stories.BeforeOrAfter bas2 = new Stories.BeforeOrAfter(bas)
                .withBeforeStep(passingStep("{"))
                .withAfterStep(passingStep("}"));
        Stories.BeforeOrAfter bas3 = new Stories.BeforeOrAfter(bas2)
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        boolean passed = bas3.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("<✓ {✓ [✓ storiesA✓ ]✓ }✓ >✓")));
    }


}
