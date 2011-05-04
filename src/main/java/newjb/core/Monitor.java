package newjb.core;

public interface Monitor {

    // Step centric, alternates

    void passed(String step);
    void failed(String step, Throwable cause);
    void failed(String step);
    void notPerformed(String step);

    // Story centric

    void startStory(Story story);
    void endStory(boolean passed);

    // Scenario centric

    void startScenario(Scenario scenario);
    void endScenario(boolean passed);
}
