package newjb.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionStep extends Step {

    private final Method method;
    private final String[] groups;
    private final String step;

    public ReflectionStep(Method method, String[] groups, String step) {
        super();
        this.method = method;
        this.groups = groups;
        this.step = step;
    }

    public boolean perform(StepFactory stepFactory, Monitor monitor) {
        try {
            Object o = stepFactory.get(method.toGenericString());
            if (groups.length == 0) {
                method.invoke(o);
            } else {
                method.invoke(o, groups);
            }
            monitor.passed(step);
            return true;
        } catch (IllegalAccessException e) {
            monitor.failed(step, new RuntimeException(e));
            return false;
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                monitor.failed(step, e.getCause());
            } else  if (e.getCause() instanceof AssertionError) {
                monitor.failed(step, e.getCause());
            } else {
                monitor.failed(step, new RuntimeException(e.getCause()));
            }
            return false;
        }
    }

    @Override
    public void dontPerform(StepFactory stepFactory, Monitor monitor) {
        monitor.notPerformed(step);
    }

    public static Step makeStep(Class clazz, String methodName, String step, String... args) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return new ReflectionStep(method, args, step);
            }
        }

        throw new RuntimeException("oops, no matching method");

    }
}
