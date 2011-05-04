package newjb.core;

import org.junit.Test;

import static newjb.core.predictable.FailingScenario.failingScenario;
import static newjb.core.predictable.PassingScenario.passingScenario;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StoryTest {

    private LittleEventLanguage monitor = new LittleEventLanguage();
    private StepFactory stepFactory = new DefaultStepFactory();

    @Test
    public void storyShouldPassWhenScenarioPasses() {
        Story story = new Story(passingScenario("a"), passingScenario("b"), passingScenario("c"));
        boolean passed = story.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("St( a✓ b✓ c✓ ✓)")));
    }

    @Test
    public void storyShouldFailWhenStepsFail() {
        Story story = new Story(passingScenario("a"), failingScenario("b"), passingScenario("c"));
        boolean passed = story.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("St( a✓ b✗ c✓ ✗)")));
    }

}
