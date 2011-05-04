package newjb.core;

public class LittleEventLanguage implements Monitor {

    private StringBuilder journal;

    public LittleEventLanguage(StringBuilder journal) {
        this.journal = journal;
    }

    public LittleEventLanguage() {
        this(new StringBuilder());
    }

    public void passed(String step) {
        journal.append(step).append("✓ ");
    }

    public void failed(String step, Throwable cause) {
        journal.append(step).append("(").append(cause.getMessage()).append(") ");
    }

    public void failed(String step) {
        journal.append(step).append("✗ ");
    }

    public void notPerformed(String step) {
        journal.append(step).append("- ");
    }

    public void startStory(Story story) {
        journal.append(story.getName()).append("( ");
    }

    public void endStory(boolean passed) {
        journal.append(passed ? "✓" : "✗").append(") ");
    }

    public void startScenario(Scenario scenario) {
        journal.append(scenario.getName()).append("( ");
    }

    public void endScenario(boolean passed) {
        journal.append(passed ? "✓" : "✗").append(") ");
    }

    public String eventString() {
        return journal.toString().trim();
    }

}
