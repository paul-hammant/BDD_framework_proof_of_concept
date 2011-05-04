package newjb.pico;

import newjb.core.StepFactory;
import org.picocontainer.PicoContainer;

public class PicoStepFactory implements StepFactory {

    private final PicoContainer picoContainer;

    public PicoStepFactory(PicoContainer picoContainer) {
        this.picoContainer = picoContainer;
    }

    public Object get(String methodSig) {
        return picoContainer.getComponent(methodSig);
    }
}
