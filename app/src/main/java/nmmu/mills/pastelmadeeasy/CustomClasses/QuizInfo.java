package nmmu.mills.pastelmadeeasy.CustomClasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 14 Jun 2016.
 */
public class QuizInfo implements Parcelable{
    private int conceptId;
    private int quizId;
    private List<QuizQuestion> questions;

    public QuizInfo() {
        questions = new ArrayList<>();
    }

    public void addQuestion(QuizQuestion question){
        questions.add(question);
    }

    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    public int getQuizId() {

        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getConceptId() {
        return conceptId;
    }

    public void setConceptId(int conceptId) {
        this.conceptId = conceptId;
    }

    //Parcelable stuff

    protected QuizInfo(Parcel in) {
        conceptId = in.readInt();
        quizId = in.readInt();
        if (in.readByte() == 0x01) {
            questions = new ArrayList<QuizQuestion>();
            in.readList(questions, QuizQuestion.class.getClassLoader());
        } else {
            questions = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(conceptId);
        dest.writeInt(quizId);
        if (questions == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(questions);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<QuizInfo> CREATOR = new Parcelable.Creator<QuizInfo>() {
        @Override
        public QuizInfo createFromParcel(Parcel in) {
            return new QuizInfo(in);
        }

        @Override
        public QuizInfo[] newArray(int size) {
            return new QuizInfo[size];
        }
    };
}
