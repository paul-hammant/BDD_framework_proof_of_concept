package newjb.pico;

import org.junit.Test;
import org.picocontainer.PicoContainer;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PicoStepMapTest {

    @Test
    public void picoContainerShouldBeUsedToAcquireInstances() {

        PicoContainer picoContainer = mock(PicoContainer.class);
        when(picoContainer.getComponent(AClass.METHOD.toGenericString())).thenReturn(new AClass());

        PicoStepFactory picoStepMap = new PicoStepFactory(picoContainer);

        Object inst = picoStepMap.get(AClass.METHOD.toGenericString());

        assertThat(inst, is(instanceOf(AClass.class)));
        assertThat(inst, is(notNullValue()));

    }

    public static class AClass {
        static final Method METHOD = AClass.class.getDeclaredMethods()[0];
        public void aMethod() {
        }
    }



}
