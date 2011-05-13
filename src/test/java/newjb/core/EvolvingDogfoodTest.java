package newjb.core;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static newjb.core.ReflectionStep.makeStep;
import static newjb.core.predictable.FailingScenario.failingScenario;
import static newjb.core.predictable.PassingStep.passingStep;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * One scenario done three different ways.
 * The scenario being a failing scenario wrapped in three pairs of before/after steps.
 */
public class EvolvingDogfoodTest {

    @Test
    public void testWeAreTryingToSimulate() {

        LittleEventLanguage monitor = new LittleEventLanguage();
        StepFactory stepFactory = new DefaultStepFactory();

        Scenario.BeforeOrAfter baf = new Scenario.BeforeOrAfter(failingScenario("scenarioA"))
                .withBeforeStep(passingStep("["))
                .withAfterStep(passingStep("]"));
        Scenario.BeforeOrAfter baf2 = new Scenario.BeforeOrAfter(baf)
                .withBeforeStep(passingStep("{"))
                .withAfterStep(passingStep("}"));
        Scenario.BeforeOrAfter baf3 = new Scenario.BeforeOrAfter(baf2)
                .withBeforeStep(passingStep("<"))
                .withAfterStep(passingStep(">"));
        boolean passed = baf3.perform(stepFactory, monitor);
        assertThat(passed, is(Boolean.FALSE));
        assertThat(monitor.eventString(), is(equalTo("<✓ {✓ [✓ scenarioA✗ ]✓ }✓ >✓")));
    }

    /**
     * The above test has been cut up, and placed into a steps class .....
     */
    public static class SampleStepsPojo {

        private LittleEventLanguage monitor = new LittleEventLanguage();
        private StepFactory stepFactory = new DefaultStepFactory();


        private Scenario scenario;
        private boolean passed;

        @Given("passing scenario 'scenarioA'")
        public void givenSteps() {
            scenario = failingScenario("scenarioA");
        }

        @Given("a containing before and after scenario '(.*)' & '(.*)' that passes")
        public void givenPassingBeforeAndAfter(String before, String after) {
            scenario = new Scenario.BeforeOrAfter(scenario)
                    .withBeforeStep(passingStep(before))
                    .withAfterStep(passingStep(after));
        }

        @When("scenario are performed")
        public void when() {
            passed = scenario.perform(stepFactory, monitor);
        }

        @Then(value= "failure should result")
        public void thenPasses() {
            assertThat(passed, is(equalTo(Boolean.FALSE)));
        }

        @Then("the order of events should be '(.*)'")
        public void thenEventsJustSo(String shouldBe) {
            assertThat(monitor.eventString(), is(equalTo(shouldBe)));
        }
    }


    @Test
    public void simulatedBDDwithNamedStepMethods() {

        LittleEventLanguage monitor = new LittleEventLanguage();

        Scenario scenario = new Scenario(new Steps(
                makeStep(SampleStepsPojo.class, "givenSteps", "1"),
                makeStep(SampleStepsPojo.class, "givenPassingBeforeAndAfter", "2", "[", "]"),
                makeStep(SampleStepsPojo.class, "givenPassingBeforeAndAfter", "3", "{", "}"),
                makeStep(SampleStepsPojo.class, "givenPassingBeforeAndAfter", "4", "<", ">"),
                makeStep(SampleStepsPojo.class, "when", "5"),
                makeStep(SampleStepsPojo.class, "thenPasses", "6"),
                makeStep(SampleStepsPojo.class, "thenEventsJustSo", "7", "<✓ {✓ [✓ scenarioA✗ ]✓ }✓ >✓")
        ));

        final SampleStepsPojo pojo = new SampleStepsPojo();
        boolean passed = scenario.perform(new StepFactory() {
            public Object get(String methodSig) {
                return pojo; // same instance for each step.
            }
        }, monitor);

        if(!passed) {
            fail("scenario did not pass: " + monitor.eventString());
        }

        assertEquals("Sc( 1✓ 2✓ 3✓ 4✓ 5✓ 6✓ 7✓ ✓)", monitor.eventString());
    }

    @Test
    public void fullTextScenarioWithTransparentLocationOfSteps() {

        LittleEventLanguage monitor = new LittleEventLanguage();

        Scenario scenario = new ScenarioFactory(SampleStepsPojo.class)
                .withSteps("Given passing scenario 'scenarioA'",
                           "Given a containing before and after scenario '[' & ']' that passes",
                           "Given a containing before and after scenario '{' & '}' that passes",
                           "Given a containing before and after scenario '<' & '>' that passes",
                           "When scenario are performed",
                           "Then failure should result",
                           "Then the order of events should be '<✓ {✓ [✓ scenarioA✗ ]✓ }✓ >✓'")
                .createScenario();

        final SampleStepsPojo pojo = new SampleStepsPojo();
        boolean passed = scenario.perform(new StepFactory() {
            public Object get(String methodSig) {
                return pojo; // same instance for each step.
            }
        }, monitor);

        assertThat("scenario did not pass: " + monitor.eventString(),
                passed, is(equalTo(true)));

        assertThat(monitor.eventString(), is(equalTo("Sc( Given passing scenario 'scenarioA'✓ " +
                "Given a containing before and after scenario '[' & ']' that passes✓ " +
                "Given a containing before and after scenario '{' & '}' that passes✓ " +
                "Given a containing before and after scenario '<' & '>' that passes✓ " +
                "When scenario are performed✓ " +
                "Then failure should result✓ " +
                "Then the order of events should be '<✓ {✓ [✓ scenarioA✗ ]✓ }✓ >✓'✓ ✓)"
                )));
    }

}
