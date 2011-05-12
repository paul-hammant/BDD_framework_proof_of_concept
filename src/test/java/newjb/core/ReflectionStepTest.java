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
        Step step = new ReflectionStep(WillPass.METHOD, new String[0], "didItPass?");
        step.perform(stepFactory, monitor);
        assertThat(journal.toString().trim(), is(equalTo("willPass didItPass?✓")));
    }

    @Test
    public void shouldHandleFailingSteps() {
        stepFactory.add(WillThrow.METHOD, new WillThrow());
        Step step = new ReflectionStep(WillThrow.METHOD, new String[0], "didItPass?");
        boolean foo = step.perform(stepFactory, monitor);
        assertThat(foo, is(false));
        assertThat(monitor.eventString(), equalTo("didItPass?(throw RTE!)"));
    }

    @Test
    public void shouldHandleAssertingSteps() {
        stepFactory.add(WillAssert.METHOD, new WillAssert());
        Step step = new ReflectionStep(WillAssert.METHOD, new String[0], "didItPass?");
        boolean foo = step.perform(stepFactory, monitor);
        assertThat(foo, is(false));
        assertThat(monitor.eventString(), equalTo("didItPass?(throw AE!)"));
    }

    @Test
    public void shouldHandleErroringSteps() {
        stepFactory.add(WillError.METHOD, new WillError());
        Step step = new ReflectionStep(WillError.METHOD, new String[0], "didItPass?");
        boolean foo = step.perform(stepFactory, monitor);
        assertThat(foo, is(false));
        assertThat(monitor.eventString(), equalTo("didItPass?(java.lang.OutOfMemoryError: throw OoME!)"));
    }

    @Test
    public void shouldHandleInvalidStepMethod() {
        stepFactory.add(Invalid.METHOD, new Invalid());
        Step step = new ReflectionStep(Invalid.METHOD, new String[0], "didItPass?");
        boolean foo = step.perform(stepFactory, monitor);
        assertThat(foo, is(false));
        assertThat(monitor.eventString().contains("IllegalAccessException"), is(true));
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
            throw new RuntimeException("throw RTE!");
        }
    }

    public static class Invalid {
        static final Method METHOD = Invalid.class.getDeclaredMethods()[0];
        private void notAvailable() {
            System.exit(10); // never called
        }
    }

    public static class WillError {
        static final Method METHOD = WillError.class.getDeclaredMethods()[0];
        public void willError() {
            throw new OutOfMemoryError("throw OoME!");
        }
    }

    public static class WillAssert {
        static final Method METHOD = WillAssert.class.getDeclaredMethods()[0];
        public void willAssert() {
            throw new AssertionError("throw AE!");
        }
    }

}
