package newjb.core;

public class Stories implements Performable {

    private Story[] stories;

    public Stories(Story... stories) {
        this.stories = stories;
    }

    public boolean perform(StepFactory stepFactory, Monitor monitor) {
        boolean state = true;
        for (Story story : stories) {
            if (!story.perform(stepFactory, monitor)) {
                state = false;
            }
        }
        return state;

    }

    public static class BeforeOrAfter extends Stories {
        private final Stories stories;
        private Step beforeStep;
        private Step afterStep;

        public BeforeOrAfter(Stories stories) {
            this.stories = stories;
        }

        public BeforeOrAfter withBeforeStep(Step step) {
            beforeStep = step;
            return this;
        }

        public BeforeOrAfter withAfterStep(Step step) {
            afterStep = step;
            return this;
        }

        public boolean perform(StepFactory stepFactory, Monitor monitor) {
            boolean state = true;

            if (beforeStep != null) {
                state = beforeStep.perform(stepFactory, monitor);
            }

            if (!state) {
                return false;
            }

            state = stories.perform(stepFactory, monitor);

            boolean afterState = true;
            if (afterStep != null) {
                afterState = afterStep.perform(stepFactory, monitor);
            }

            return state && afterState;
        }

    }
}
