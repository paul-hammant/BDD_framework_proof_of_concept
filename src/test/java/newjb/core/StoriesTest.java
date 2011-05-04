package newjb.core;

import org.junit.Before;
import org.junit.Test;

import static newjb.core.predictable.FailingStory.failingStory;
import static newjb.core.predictable.PassingStory.passingStory;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StoriesTest {

    private LittleEventLanguage monitor;
    private StepFactory stepFactory;

    @Before
    public void setup() {
        monitor = new LittleEventLanguage();
        stepFactory = new DefaultStepFactory();
    }

    @Test
    public void storiesCanAllPass() {
        Stories stories = new Stories(passingStory("a"), passingStory("b"), passingStory("c"));
        boolean passed = stories.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("a✓ b✓ c✓")));
    }

    @Test
    public void storiesCanFailButTheSetWillComplete() {
        Stories stories = new Stories(passingStory("a"), failingStory("b"), passingStory("c"));
        boolean passed = stories.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("a✓ b✗ c✓")));
    }

}
