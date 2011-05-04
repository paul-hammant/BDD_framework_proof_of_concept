package newjb.core;

import org.junit.Test;

import static newjb.core.predictable.AssertingStep.assertingStep;
import static newjb.core.predictable.ExceptingStep.exceptingStep;
import static newjb.core.predictable.FailingScenario.failingScenario;
import static newjb.core.predictable.PassingScenario.passingScenario;
import static newjb.core.predictable.PassingStep.passingStep;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BeforeAfterScenarioTest {

    private LittleEventLanguage monitor = new LittleEventLanguage();
    private StepFactory stepFactory = new DefaultStepFactory();

    @Test
    public void shouldPassIfStepsPass() {
        Scenario.BeforeOrAfter baf = new Scenario.BeforeOrAfter(passingScenario("scenarioA"));
        boolean passed = baf.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("scenarioA✓")));
    }

    @Test
    public void shouldFailWhenStepsFail() {
        Scenario.BeforeOrAfter baf = new Scenario.BeforeOrAfter(failingScenario("scenarioA"));
        boolean passed = baf.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("scenarioA✗")));
    }

    @Test
    public void shouldPassWhenBeforeStepPasses() {
        Scenario.BeforeOrAfter baf = new Scenario.BeforeOrAfter(passingScenario("scenarioA"))
                .withBeforeStep(passingStep("<"));
        boolean passed = baf.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("<✓ scenarioA✓")));
    }

    @Test
    public void shouldFastFailWhenBeforeStepFailsAndNotExecuteRealSteps() {
        Scenario.BeforeOrAfter baf = new Scenario.BeforeOrAfter(passingScenario("scenarioA"))
                .withBeforeStep(exceptingStep("<"));
        boolean passed = baf.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("<(rExcpt)")));
    }

    @Test
    public void shouldPassWhenAfterStepPasses() {
        Scenario.BeforeOrAfter baf = new Scenario.BeforeOrAfter(passingScenario("scenarioA"))
                .withAfterStep(passingStep(">"));
        boolean passed = baf.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("scenarioA✓ >✓")));
    }

    @Test
    public void shouldFailWhenAfterStepFails() {
        Scenario.BeforeOrAfter baf = new Scenario.BeforeOrAfter(passingScenario("scenarioA"))
                .withAfterStep(exceptingStep(">"));
        boolean passed = baf.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("scenarioA✓ >(rExcpt)")));
    }

    @Test
    public void shouldPassWhenBeforeAndAfterStepsPass() {
        Scenario.BeforeOrAfter baf = new Scenario.BeforeOrAfter(passingScenario("scenarioA"))
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        boolean passed = baf.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("<✓ scenarioA✓ >✓")));
    }

    @Test
    public void shouldFastFailWhenFormerOfBeforeAndAfterStepsFail() {
        Scenario.BeforeOrAfter baf = new Scenario.BeforeOrAfter(passingScenario("scenarioA"))
                .withBeforeStep(exceptingStep("<"))
                .withAfterStep(passingStep(">"));
        boolean passed = baf.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("<(rExcpt)")));
    }

    @Test
    public void shouldFailWhenLatterOfBeforeAndAfterStepsFailButStillPreserveDelegateStepsResult() {
        Scenario.BeforeOrAfter baf = new Scenario.BeforeOrAfter(passingScenario("scenarioA"))
                .withBeforeStep(passingStep("<"))
                .withAfterStep(exceptingStep(">"));
        boolean passed = baf.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("<✓ scenarioA✓ >(rExcpt)")));
    }

    @Test
    public void shouldFailWhenStepsFailButPerformAfterStepAnyway() {
        Scenario.BeforeOrAfter baf = new Scenario.BeforeOrAfter(failingScenario("scenarioA"))
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        boolean passed = baf.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("<✓ scenarioA✗ >✓")));
    }

    @Test
    public void shouldFailWhenStepsAssertButPerformAfterStepAnyway() {
        Scenario.BeforeOrAfter baf = new Scenario.BeforeOrAfter(failingScenario("scenarioA"))
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        boolean passed = baf.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("<✓ scenarioA✗ >✓")));
    }

    @Test
    public void shouldPassWhenNestedBeforeAndAfterStepsPass() {
        Scenario.BeforeOrAfter baf = new Scenario.BeforeOrAfter(passingScenario("scenarioA"))
                .withBeforeStep(passingStep("["))
                .withAfterStep(passingStep("]"));
        Scenario.BeforeOrAfter baf2 = new Scenario.BeforeOrAfter(baf)
                .withBeforeStep(passingStep("{"))
                .withAfterStep(passingStep("}"));
        Scenario.BeforeOrAfter baf3 = new Scenario.BeforeOrAfter(baf2)
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        boolean passed = baf3.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.TRUE));
        assertThat(monitor.eventString(), is(equalTo("<✓ {✓ [✓ scenarioA✓ ]✓ }✓ >✓")));
    }

    @Test
    public void shouldFailIfMidPlacedAfterFailsButOtherwiseDoEverythingElse() {
        Scenario.BeforeOrAfter baf = new Scenario.BeforeOrAfter(passingScenario("scenarioA"))
                .withBeforeStep(passingStep("["))
                .withAfterStep(passingStep("]"));
        Scenario.BeforeOrAfter baf2 = new Scenario.BeforeOrAfter(baf)
                .withBeforeStep(passingStep("{"))
                .withAfterStep(assertingStep("}"));
        Scenario.BeforeOrAfter baf3 = new Scenario.BeforeOrAfter(baf2)
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        boolean passed = baf3.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("<✓ {✓ [✓ scenarioA✓ ]✓ }(aErr) >✓")));
    }

    @Test
    public void shouldFailIfMidPlacedBeforeFailsAndNotRecurseFutrther() {
        Scenario.BeforeOrAfter baf = new Scenario.BeforeOrAfter(passingScenario("scenarioA"))
                .withBeforeStep(passingStep("["))
                .withAfterStep(passingStep("]"));
        Scenario.BeforeOrAfter baf2 = new Scenario.BeforeOrAfter(baf)
                .withBeforeStep(assertingStep("{"))
                .withAfterStep(passingStep("}"));
        Scenario.BeforeOrAfter baf3 = new Scenario.BeforeOrAfter(baf2)
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        boolean passed = baf3.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("<✓ {(aErr) >✓")));
    }


}
