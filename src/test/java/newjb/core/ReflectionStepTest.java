package newjb.core;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReflectionStepTest {

    private StringBuilder journal = new StringBuilder();
    private DefaultStepFactory stepFactory = new DefaultStepFactory();
    private LittleEventLanguage monitor = new LittleEventLanguage(journal);

    @Test
    public void shouldPerformPassingSteps() {
        stepFactory.add(WillPass.METHOD, new WillPass(journal));
        Step step = new ReflectionStep(WillPass.METHOD, new String[0], "didPass");
        step.perform(stepFactory, monitor);
        assertThat(journal.toString().trim(), is(equalTo("willPass didPass✓")));
    }

    @Test
    public void shouldThrowOnFailingSteps() {
        stepFactory.add(WillThrow.METHOD, new WillThrow());
        Step step = new ReflectionStep(WillThrow.METHOD, new String[0], "didPass");
        try {
            step.perform(stepFactory, monitor);
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is(equalTo("boo hoo!")));
            assertThat(monitor.toString(), is(equalTo("didPass✗")));
        }
    }

    @Test
    public void shouldThrowOnAssertingSteps() {
        stepFactory.add(WillAssert.METHOD, new WillAssert());
        Step step = new ReflectionStep(WillAssert.METHOD, new String[0], "didPass");
        try {
            step.perform(stepFactory, monitor);
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(equalTo("boo hoo!")));
            assertThat(monitor.toString(), is(equalTo("didPass✗")));
        }
    }

    public static class WillPass {
        private StringBuilder journal;

        public WillPass(StringBuilder journal) {
            this.journal = journal;
        }

        static final Method METHOD = WillPass.class.getDeclaredMethods()[0];
        public void willPass() {
            journal.append("willPass ");
        }
    }

    public static class WillThrow {
        static final Method METHOD = WillThrow.class.getDeclaredMethods()[0];
        public void willThrow() {
            throw new RuntimeException("boo hoo!");
        }
    }

    public static class WillAssert {
        static final Method METHOD = WillAssert.class.getDeclaredMethods()[0];
        public void willAssert() {
            throw new AssertionError("boo hoo!");
        }
    }

}
