package nmmu.mills.pastelmadeeasy.CustomClasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Steven on 10 Jun 2016.
 */
public class Note implements Parcelable{
    private int conceptId;
    private int id;
    private String title;
    private String text;
    private String lastModified;

    public Note() {
    }

    public Note(Note otherNote) {
        this.conceptId = otherNote.getConceptId();
        this.id = otherNote.getId();
        this.title = otherNote.getTitle();
        this.text = otherNote.getText();
        this.lastModified = otherNote.getLastModified();
    }

    public int getConceptId() {
        return conceptId;
    }

    public void setConceptId(int conceptId) {
        this.conceptId = conceptId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    protected Note(Parcel in) {
        conceptId = in.readInt();
        id = in.readInt();
        title = in.readString();
        text = in.readString();
        lastModified = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(conceptId);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(text);
        dest.writeString(lastModified);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
