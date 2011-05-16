package newjb.core;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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

        public void divide(int num) {
            value = value / num;
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

        @When("(.*) is divided")
        public void numberIsDivided(String num) {
            calculator.divide(Integer.parseInt(num));
        }

        @Then("the result is (.*)")
        public void thenEventsJustSo(String shouldBe) {
            assertThat(calculator.currentValue(), is(equalTo(Integer.parseInt(shouldBe))));
        }
    }

    @Test
    public void calculatorShouldBeAbleToAddTwoNumbers() {

        test("Sc( Given a current value of 1✓ When 1 is added✓ Then the result is 2✓ ✓)",
                "Given a current value of 1",
                "When 1 is added",
                "Then the result is 2");

        test("Sc( Given a current value of 2✓ When 2 is added✓ Then the result is 4✓ ✓)",
                "Given a current value of 2",
                "When 2 is added",
                "Then the result is 4");

        test("Sc( Given a current value of 1✓ When -1 is added✓ Then the result is 0✓ ✓)",
                "Given a current value of 1",
                "When -1 is added",
                "Then the result is 0");
    }

    private void test(String expectation, String... steps) {
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

    @Test
    public void calculatorShouldBeAbleToFivideTwoNumbers() {

        test("Sc( Given a current value of 1✓ When 1 is divided✓ Then the result is 1✓ ✓)",
                "Given a current value of 1",
                "When 1 is divided",
                "Then the result is 1");

        test("Sc( Given a current value of 4✓ When 2 is divided✓ Then the result is 2✓ ✓)",
                "Given a current value of 4",
                "When 2 is divided",
                "Then the result is 2");

        try {
            test("Sc( Given a current value of 11✓ When 0 is divided(/ by zero) Then the result is infinity- ✗)",
                    "Given a current value of 11",
                    "When 0 is divided",
                    "Then the result is infinity");
            fail("should have barfed");
        } catch (AssertionError e) {
            assertThat(e.getMessage().trim(), is(equalTo("scenario did not pass: Sc( Given a current value of 11✓ When 0 is divided(/ by zero) Then the result is infinity- ✗)\n" +
                    "Expected: is <true>\n" +
                    "     got: <false>")));
        }
    }




}
