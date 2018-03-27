package nmmu.mills.pastelmadeeasy.CustomClasses;

/**
 * Created by Steven on 09 Jun 2016.
 */
public class GenericItem {
    private int conceptID;
    private String title;
    private String extra;
    private String actionStatement;
    private boolean complete;

    public String getActionStatement() {
        return actionStatement;
    }

    public GenericItem(int conceptID, String title, String extra, String actionStatement, boolean complete) {
        this.conceptID = conceptID;
        this.title = title;
        this.extra = extra;
        this.actionStatement = actionStatement;
        this.complete = complete;
    }

    public int getConceptID() {
        return conceptID;
    }

    public String getTitle() {
        return title;
    }

    public String getExtra() {
        return extra;
    }

    public boolean isComplete() {
        return complete;
    }
}
