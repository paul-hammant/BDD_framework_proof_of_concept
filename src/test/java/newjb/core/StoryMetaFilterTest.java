package newjb.core;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static newjb.core.predictable.PassingScenario.passingScenario;
import static newjb.core.predictable.PassingStep.passingStep;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class StoryMetaFilterTest {

    private LittleEventLanguage monitor = new LittleEventLanguage();
    private StepFactory stepFactory = new DefaultStepFactory();
    private Map<String, String> metaTags = new HashMap<String, String>();
    private Map<String, String> filter = new HashMap<String, String>();
    private Boolean passed = null; // tri-state
    private Story story = new Story(passingScenario("a")).withMetaTags(metaTags);

    private void tryIt() {
        story.performUnlessExcluded(new Performance() {
            public void perform() {
                passed = story.perform(stepFactory, monitor);
            }
        }, filter);
    }

    @Test
    public void excludeIfFilterExcludesSpecifically() {
        metaTags.put("foo", "bar");
        filter.put("-foo", "bar");
        tryIt();
        assertThat(passed, is(nullValue())); // not run
        assertThat(monitor.eventString(), is(equalTo(""))); // no events, because not run
    }

    @Test
    public void excludeIfFilterExcludesGenerically() {
        metaTags.put("foo", "bar");
        filter.put("-foo", "");
        tryIt();
        assertThat(passed, is(nullValue())); // not run
        assertThat(monitor.eventString(), is(equalTo(""))); // no events, because not run
    }


    @Test
    public void includeIfFilterExcludesSomethingElse() {
        metaTags.put("foo", "bar");
        filter.put("-foo", "barrrrr");
        tryIt();
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("St( a✓ ✓)")));
    }

    @Test
    public void includeIfFilterIncludesSpecifically() {
        metaTags.put("foo", "bar");
        filter.put("+foo", "bar");
        tryIt();
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("St( a✓ ✓)")));
    }

    @Test
    public void excludeBasedOnFilterAfterIncludeBasedOnFilter() {
        metaTags.put("foo", "bar");
        metaTags.put("abc", "123");
        filter.put("+foo", "");
        filter.put("-abc", "123");
        tryIt();
        assertThat(passed, is(nullValue())); // not run
        assertThat(monitor.eventString(), is(equalTo(""))); // no events, because not run
    }

    // include/exclude kicks in before beforeOrAfter steps are invoked.

    @Test
    public void nestedExcludeIfFilterExcludesSpecifically() {
        story = new Story.BeforeOrAfter(story)
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        metaTags.put("foo", "bar");
        filter.put("-foo", "bar");
        tryIt();
        assertThat(passed, is(nullValue())); // not run
        assertThat(monitor.eventString(), is(equalTo(""))); // no events, because not run
    }

    @Test
    public void nestedExcludeIfFilterExcludesGenerically() {
        story = new Story.BeforeOrAfter(story)
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        metaTags.put("foo", "bar");
        filter.put("-foo", "");
        tryIt();
        assertThat(passed, is(nullValue())); // not run
        assertThat(monitor.eventString(), is(equalTo(""))); // no events, because not run
    }


    @Test
    public void nestedIncludeIfFilterExcludesSomethingElse() {
        story = new Story.BeforeOrAfter(story)
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        metaTags.put("foo", "bar");
        filter.put("-foo", "barrrrr");
        tryIt();
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("<✓ St( a✓ ✓) >✓")));
    }

    @Test
    public void nestedIncludeIfFilterIncludesSpecifically() {
        story = new Story.BeforeOrAfter(story)
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        metaTags.put("foo", "bar");
        filter.put("+foo", "bar");
        tryIt();
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("<✓ St( a✓ ✓) >✓")));
    }

    @Test
    public void nestedExcludeBasedOnFilterAfterIncludeBasedOnFilter() {
        story = new Story.BeforeOrAfter(story)
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        metaTags.put("foo", "bar");
        metaTags.put("abc", "123");
        filter.put("+foo", "");
        filter.put("-abc", "123");
        tryIt();
        assertThat(passed, is(nullValue())); // not run
        assertThat(monitor.eventString(), is(equalTo(""))); // no events, because not run
    }


}
