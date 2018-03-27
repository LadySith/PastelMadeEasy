package nmmu.mills.pastelmadeeasy.CustomClasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Steven on 14 Jun 2016.
 */
public class Answer implements Parcelable{
    private int quizId;
    private int questionNumber;
    private String letter;
    private String text;

    public Boolean getCorrect() {
        return correct;
    }

    private Boolean correct = false;
    private Boolean selected = false;

    public Answer() {
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getText() {

        return text;
    }

    public String getLetter() {
        return letter;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void changeSelected(){
        if (selected)
            selected = false;
        else
            selected = true;

    }

    //Parcelable stuff
    protected Answer(Parcel in) {
        quizId = in.readInt();
        questionNumber = in.readInt();
        letter = in.readString();
        text = in.readString();
        byte correctVal = in.readByte();
        correct = correctVal == 0x02 ? null : correctVal != 0x00;
        byte selectedVal = in.readByte();
        selected = selectedVal == 0x02 ? null : selectedVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quizId);
        dest.writeInt(questionNumber);
        dest.writeString(letter);
        dest.writeString(text);
        if (correct == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (correct ? 0x01 : 0x00));
        }
        if (selected == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (selected ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Answer> CREATOR = new Parcelable.Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public int getQuizId() {
        return quizId;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
