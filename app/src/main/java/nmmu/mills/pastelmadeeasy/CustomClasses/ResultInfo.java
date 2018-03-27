package nmmu.mills.pastelmadeeasy.CustomClasses;

/**
 * Created by Steven on 12 Jul 2016.
 */
public class ResultInfo {
    private int quizID;
    private int ID;
    private String date;
    private int percentage;
    private int improved;


    public ResultInfo(int quizID) {
        this.quizID = quizID;
    }

    public int getQuizID() {
        return quizID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {

        this.ID = ID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public void setImproved(int improved) {
        this.improved = improved;
    }

    public String getDate() {
        return date;
    }

    public int getPercentage() {
        return percentage;
    }


    public int getImproved() {
        return improved;
    }
}


