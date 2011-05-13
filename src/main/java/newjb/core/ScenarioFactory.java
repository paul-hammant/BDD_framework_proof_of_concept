package newjb.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScenarioFactory {
    private final Class<?>[] stepsClasses;
    private String[] steps = new String[0];

    public ScenarioFactory(Class<?>... stepsClasses) {
        this.stepsClasses = stepsClasses;
    }

    public ScenarioFactory withSteps(String... steps) {
        this.steps = steps;
        return this;
    }

    public Scenario createScenario() {

        Step[] steps = new Step[this.steps.length];

        String[] stepz = this.steps;
        for (int x = 0, steps1Length = stepz.length; x < steps1Length; x++) {
            String step = stepz[x];
            for (Class<?> stepsClass : stepsClasses) {
                Method[] methods = stepsClass.getDeclaredMethods();
                for (Method method : methods) {
                    Annotation[] annotations = method.getAnnotations();
                    for (Annotation annotation : annotations) {
                        String name = annotation.annotationType().getSimpleName() + " " + annotationValue(annotation);
                        Pattern pattern = Pattern.compile(name);
                        Matcher matcher = pattern.matcher(step);
                        String[] groups = new String[method.getParameterTypes().length];
                        if (matcher.matches()) {
                            for (int p = 0; p < groups.length; p++) {
                                String group = matcher.group(p + 1);
                                groups[p] = group;
                            }
                            steps[x] = new ReflectionStep(method, groups, step);
                            break;
                        }
                    }
                    if (steps[x] != null) {
                        break;
                    }
                }
                if (steps[x] != null) {
                    break;
                }
            }
            if (steps[x] == null) {
                throw new RuntimeException("no step for " + stepz[x]);
            }
        }

        return new Scenario(new Steps(steps));
    }

    private String annotationValue(Annotation annotation) {
        if (annotation instanceof Given) {
            return ((Given) annotation).value();
        } else if (annotation instanceof When) {
            return ((When) annotation).value();
        } else if (annotation instanceof Then) {
            return ((Then) annotation).value();
        } else {
            throw new RuntimeException("foo");
        }

    }
}
