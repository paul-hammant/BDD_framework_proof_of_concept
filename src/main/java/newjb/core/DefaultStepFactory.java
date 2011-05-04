package newjb.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DefaultStepFactory implements StepFactory {

    private Map<String, Object> map = new HashMap<String, Object>();

    public void add(Method method, Object inst) {
        map.put(method.toGenericString(), inst);
    }

    public Object get(String methodSig) {
        return map.get(methodSig);
    }
}
