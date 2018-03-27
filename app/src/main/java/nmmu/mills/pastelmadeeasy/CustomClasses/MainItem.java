package nmmu.mills.pastelmadeeasy.CustomClasses;

/**
 * Created by Steven on 09 Jun 2016.
 */
public class MainItem {
    int conceptID;
    String title;
    Boolean tutorial;
    Boolean steps;
    Boolean video;
    Boolean quiz;

    public int getConceptID() {
        return conceptID;
    }

    public MainItem(int conceptID, String title, Boolean tutorial, Boolean steps, Boolean video, Boolean quiz) {
        this.conceptID = conceptID;

        this.title = title;
        this.tutorial = tutorial;
        this.steps = steps;
        this.video = video;
        this.quiz = quiz;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getTutorial() {

        return tutorial;
    }

    public void setTutorial(Boolean tutorial) {
        this.tutorial = tutorial;
    }

    public Boolean getSteps() {
        return steps;
    }

    public void setSteps(Boolean steps) {
        this.steps = steps;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Boolean getQuiz() {
        return quiz;
    }

    public void setQuiz(Boolean quiz) {
        this.quiz = quiz;
    }
}
