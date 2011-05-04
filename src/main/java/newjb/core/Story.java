package newjb.core;

import java.util.Map;
import java.util.Set;

public class Story implements Performable {

    private final Scenario[] scenarios;
    private String name = "St";
    private Map<String, String> metaTags;

    public Story(Scenario... scenarios) {
        this.scenarios = scenarios;
    }

    public boolean perform(StepFactory stepFactory, Monitor monitor) {
        boolean passed = true;
        monitor.startStory(this);
        for (Scenario scenario : scenarios) {
            if (!scenario.perform(stepFactory, monitor)) {
                passed = false;
            }
        }
        monitor.endStory(passed);
        return passed;
    }

    public String getName() {
        return name;
    }

    public void performUnlessExcluded(Performance performance, Map<String, String> includeExcludes) {
        Set keys = includeExcludes.keySet();
        for (Object key : keys) {
            String next = (String) key;
            if (next.startsWith("-")) {
                String tag = metaTags.get(next.substring(1));
                String filterTag = includeExcludes.get(next);
                if (tag.equals(filterTag) || filterTag.equals("")) {
                    return;
                }
            } else if (next.startsWith("+")) {
                String tag = metaTags.get(next.substring(1));
                if (!tag.equals(includeExcludes.get(next))) {
                    return;
                }
            } else {
                throw new RuntimeException("tag '" + next + "'should begin with + or -");
            }
        }
        performance.perform();

    }

    public Story withMetaTags(Map<String, String> metaTags) {
        this.metaTags = metaTags;
        return this;
    }

    public static class BeforeOrAfter extends Story {
        private final Story story;
        private Step beforeStep;
        private Step afterStep;

        public BeforeOrAfter(Story story) {
            this.story = story;
        }

        public BeforeOrAfter withBeforeStep(Step step) {
            beforeStep = step;
            return this;
        }

        public BeforeOrAfter withAfterStep(Step step) {
            afterStep = step;
            return this;
        }

        @Override
        public void performUnlessExcluded(Performance performance, Map<String, String> includeExcludes) {
            story.performUnlessExcluded(performance, includeExcludes);
        }

        public boolean perform(StepFactory stepFactory, Monitor monitor) {
            boolean state = true;

            if (beforeStep != null) {
                state = beforeStep.perform(stepFactory, monitor);
            }

            if (!state) {
                return false;
            }

            state = story.perform(stepFactory, monitor);

            boolean afterState = true;
            if (afterStep != null) {
                afterState = afterStep.perform(stepFactory, monitor);
            }

            return state && afterState;
        }

    }
}
