package nmmu.mills.pastelmadeeasy.CustomClasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 14 Jun 2016.
 */
public class QuizQuestion implements Parcelable{
    private int quizId;
    private int number;
    private String text;
    private List<Answer> answers;
    private Boolean answered = false;
    private Boolean previouslyCorrect = false;

    public Boolean getPreviouslyCorrect() {
        return previouslyCorrect;
    }

    public void setPreviouslyCorrect(Boolean previouslyCorrect) {
        this.previouslyCorrect = previouslyCorrect;
    }

    public Boolean getAnswered() {
        for (Answer answer: answers){
            if (answer.getSelected())
                answered = true;
        }

        return answered;
    }

    public QuizQuestion() {
        answers = new ArrayList<>();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void addAnswer(Answer answer){
        answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public int possibleMarks(){
        int marks = 0;
        for (Answer answer: answers){
            if (answer.getCorrect())
                marks++;
        }
        return marks;
    }

    public int actualMark(){

        if (!getAnswered()) {
            return 0;
        }
        int mark = 0;

        for (Answer answer: answers){
            if (answer.getCorrect() && answer.getSelected())
                mark++;
        }

        return mark;
    }

    public int getNumber() {
        return number;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    //Parcelable stuff

    protected QuizQuestion(Parcel in) {
        quizId = in.readInt();
        number = in.readInt();
        text = in.readString();
        if (in.readByte() == 0x01) {
            answers = new ArrayList<Answer>();
            in.readList(answers, Answer.class.getClassLoader());
        } else {
            answers = null;
        }
        byte answeredVal = in.readByte();
        answered = answeredVal == 0x02 ? null : answeredVal != 0x00;
        byte previouslyCorrectVal = in.readByte();
        previouslyCorrect = previouslyCorrectVal == 0x02 ? null : previouslyCorrectVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quizId);
        dest.writeInt(number);
        dest.writeString(text);
        if (answers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(answers);
        }
        if (answered == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (answered ? 0x01 : 0x00));
        }
        if (previouslyCorrect == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (previouslyCorrect ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<QuizQuestion> CREATOR = new Parcelable.Creator<QuizQuestion>() {
        @Override
        public QuizQuestion createFromParcel(Parcel in) {
            return new QuizQuestion(in);
        }

        @Override
        public QuizQuestion[] newArray(int size) {
            return new QuizQuestion[size];
        }
    };

}
