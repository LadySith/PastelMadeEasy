package nmmu.mills.pastelmadeeasy.CustomClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 10 Jun 2016.
 */
public class ConceptInfo {
    int id;
    private String name;
    private String description;
    private String video;
    private boolean tutorialCompleted;
    private boolean stepsCompleted;
    private boolean videoWatched;
    private boolean quizCompleted;
    private List<Note> notes;

    public ConceptInfo(String name) {
        this.name = name;
        tutorialCompleted = false;
        stepsCompleted = false;
        videoWatched = false;
        quizCompleted = false;
        notes = new ArrayList<>();
    }

    public void addNote(Note note){
        notes.add(note);
    }

    public void removeNote(Note note){
        notes.remove(note);
    }

    public List<Note> getNotes(){
        return notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public boolean isTutorialCompleted() {
        return tutorialCompleted;
    }

    public void setTutorialCompleted(boolean tutorialCompleted) {
        this.tutorialCompleted = tutorialCompleted;
    }

    public boolean isStepsCompleted() {
        return stepsCompleted;
    }

    public void setStepsCompleted(boolean stepsCompleted) {
        this.stepsCompleted = stepsCompleted;
    }

    public boolean isVideoWatched() {
        return videoWatched;
    }

    public void setVideoWatched(boolean videoWatched) {
        this.videoWatched = videoWatched;
    }

    public boolean isQuizCompleted() {
        return quizCompleted;
    }

    public void setQuizCompleted(boolean quizCompleted) {
        this.quizCompleted = quizCompleted;
    }
}
