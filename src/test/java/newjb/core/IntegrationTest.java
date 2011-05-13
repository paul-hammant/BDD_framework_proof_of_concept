package newjb.core;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * One scenario done three different ways.
 * The scenario being a failing scenario wrapped in three pairs of before/after steps.
 */
public class IntegrationTest {

    // Simple class to test
    public static class Calculator {
        private int value;
        public Calculator(int startingValue) {
            value = startingValue;
        }
        public void add(int num) {
            value = value + num;
        }
        public Integer currentValue() {
            return value;
        }
    }

    public static class CalculatorSteps {

        private Calculator calculator;

        @Given("a current value of (.*)")
        public void givenAValueOf(String x) {
            calculator = new Calculator(Integer.parseInt(x));
        }

        @When("(.*) is added")
        public void numberIsAdded(String num) {
            calculator.add(Integer.parseInt(num));
        }

        @Then("the result is (.*)")
        public void thenEventsJustSo(String shouldBe) {
            assertThat(calculator.currentValue(), is(equalTo(Integer.parseInt(shouldBe))));
        }
    }

    @Test
    public void calculatorShouldBeAbleToAddTwoNumbers() {
        addTest("Sc( Given a current value of 1✓ When 1 is added✓ Then the result is 2✓ ✓)",
                "Given a current value of 1",
                "When 1 is added",
                "Then the result is 2");
        addTest("Sc( Given a current value of 2✓ When 2 is added✓ Then the result is 4✓ ✓)",
                "Given a current value of 2",
                "When 2 is added",
                "Then the result is 4");
        addTest("Sc( Given a current value of 1✓ When -1 is added✓ Then the result is 0✓ ✓)",
                "Given a current value of 1",
                "When -1 is added",
                "Then the result is 0");
    }

    private void addTest(String expectation, String... steps) {
        LittleEventLanguage monitor = new LittleEventLanguage();

        Scenario scenario = new ScenarioFactory(CalculatorSteps.class)
                .withSteps(steps)
                .createScenario();

        DefaultStepFactory stepFactory = new DefaultStepFactory();
        Method[] meths = CalculatorSteps.class.getDeclaredMethods();
        CalculatorSteps calcSteps = new CalculatorSteps();
        for (Method method : meths) {
            stepFactory.add(method, calcSteps);
        }
        boolean passed = scenario.perform(stepFactory, monitor);

        assertThat("scenario did not pass: " + monitor.eventString(),
                passed, is(equalTo(true)));

        assertThat(monitor.eventString(), is(equalTo(expectation)));
    }

}
