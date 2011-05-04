package newjb.core;

public abstract class Step implements Performable {

    public abstract void dontPerform(StepFactory stepFactory, Monitor monitor);

}
