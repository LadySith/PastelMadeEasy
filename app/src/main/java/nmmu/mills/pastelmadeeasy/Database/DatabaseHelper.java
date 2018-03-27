package nmmu.mills.pastelmadeeasy.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import nmmu.mills.pastelmadeeasy.CustomClasses.Answer;
import nmmu.mills.pastelmadeeasy.CustomClasses.ConceptInfo;
import nmmu.mills.pastelmadeeasy.CustomClasses.Note;
import nmmu.mills.pastelmadeeasy.CustomClasses.QuizInfo;
import nmmu.mills.pastelmadeeasy.CustomClasses.QuizQuestion;
import nmmu.mills.pastelmadeeasy.CustomClasses.ResultInfo;
import nmmu.mills.pastelmadeeasy.CustomClasses.Step;
import nmmu.mills.pastelmadeeasy.Login.User;

/**
 * Created by Sithe on 3/1/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "PME.db";

    // Table names
    private static final String TABLE_USER = "user";
    private static final String TABLE_CONCEPT = "Concept";
    private static final String TABLE_NOTE = "Note";
    private static final String TABLE_STEP = "Step";
    private static final String TABLE_QUIZ = "Answer";
    private static final String TABLE_ANSWER = "Quiz";
    private static final String TABLE_QUESTION = "Question";
    private static final String TABLE_RESULT = "Result";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_FNAME = "user_fname";
    private static final String COLUMN_USER_LNAME = "user_lname";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_FNAME + " TEXT," + COLUMN_USER_LNAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_CONCEPT_TABLE = "DROP TABLE IF EXISTS " + TABLE_CONCEPT;
    private String DROP_NOTE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NOTE;
    private String DROP_STEP_TABLE = "DROP TABLE IF EXISTS " + TABLE_STEP;
    private String DROP_QUIZ_TABLE = "DROP TABLE IF EXISTS " + TABLE_QUIZ;
    private String DROP_ANSWER_TABLE = "DROP TABLE IF EXISTS " + TABLE_ANSWER;
    private String DROP_QUESTION_TABLE = "DROP TABLE IF EXISTS " + TABLE_QUESTION;
    private String DROP_RESULT_TABLE = "DROP TABLE IF EXISTS " + TABLE_RESULT;

    //Data tables
    public static final String CREATE_CONCEPT_TABLE = "create table Concept (" +
            "conceptID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "conceptName TEXT," +
            "conceptDescription TEXT," +
            "conceptVideo TEXT," +
            "conceptVideoWatched INTEGER," +
            "conceptQuizCompleted INTEGER," +
            "conceptTutorialCompleted INTEGER," +
            "conceptStepsCompleted INTEGER);";

    public static final String CREATE_NOTE_TABLE = "create table Note (" +
            "conceptID INTEGER," +
            "noteID INTEGER," +
            "noteTitle TEXT," +
            "noteText TEXT," +
            "noteLastModified TEXT," +
            "FOREIGN KEY(conceptID) references Concept(conceptID),"+
            "PRIMARY KEY(conceptID,noteID));";

    public static final String CREATE_STEP_TABLE = "create table Step (" +
            "conceptID INTEGER," +
            "stepNumber INTEGER," +
            "stepText TEXT," +
            "stepImage TEXT," +
            "stepAudio TEXT," +
            "FOREIGN KEY(conceptID) references Concept(conceptID),"+
            "PRIMARY KEY(conceptID,stepNumber));";

    public static final String CREATE_QUIZ_TABLE = "create table Quiz (" +
            "conceptID INTEGER," +
            "quizID INTEGER UNIQUE," +
            "FOREIGN KEY(conceptID) references Concept(conceptID),"+
            "PRIMARY KEY(conceptID,quizID));";

    public static final String CREATE_QUESTION_TABLE = "create table Question (" +
            "quizID INTEGER," +
            "questionNumber INTEGER," +
            "questionText TEXT," +
            "questionImage TEXT," +
            "questionPreviouslyCorrect INTEGER," +
            "FOREIGN KEY(quizID) references Quiz(quizID),"+
            "PRIMARY KEY(quizID,questionNumber));";

    public static final String CREATE_ANSWER_TABLE = "create table Answer (" +
            "quizID INTEGER," +
            "questionNumber INTEGER," +
            "answerLetter TEXT," +
            "answerText TEXT," +
            "answerCorrect INTEGER," +
            "FOREIGN KEY(quizID) references Quiz(quizID),"+
            "FOREIGN KEY(questionNumber) references Question(questionNumber),"+
            "PRIMARY KEY(quizID,questionNumber,answerLetter));";

    public static final String CREATE_RESULT_TABLE = "create table Result (" +
            "quizID INTEGER," +
            "resultID INTEGER," +
            "resultDate TEXT," +
            "resultPercentage INTEGER," +
            "resultImproved INTEGER," +
            "FOREIGN KEY(quizID) references Quiz(quizID),"+
            "PRIMARY KEY(quizID,resultID));";

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static DatabaseHelper databaseHelperInstance;

    public static synchronized DatabaseHelper getDatabaseHelperInstance(Context context) {
        if (databaseHelperInstance == null) {
            databaseHelperInstance = new DatabaseHelper((context.getApplicationContext()));
        }
        return databaseHelperInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CONCEPT_TABLE);
        db.execSQL(CREATE_NOTE_TABLE);
        db.execSQL(CREATE_STEP_TABLE);
        db.execSQL(CREATE_QUIZ_TABLE);
        db.execSQL(CREATE_ANSWER_TABLE);
        db.execSQL(CREATE_QUESTION_TABLE);
        db.execSQL(CREATE_RESULT_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_CONCEPT_TABLE);
        db.execSQL(DROP_NOTE_TABLE);
        db.execSQL(DROP_STEP_TABLE);
        db.execSQL(DROP_ANSWER_TABLE);
        db.execSQL(DROP_QUIZ_TABLE);
        db.execSQL(DROP_QUESTION_TABLE);
        db.execSQL(DROP_RESULT_TABLE);
        // Create tables again
        onCreate(db);

    }

    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_FNAME, user.getFName());
        values.put(COLUMN_USER_LNAME, user.getLName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<User> getUsers() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_FNAME,
                COLUMN_USER_LNAME,
                COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_LNAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setFName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_FNAME)));
                user.setLName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_LNAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_FNAME, user.getFName());
        values.put(COLUMN_USER_LNAME, user.getLName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 's123456789@nmmu.ac.za';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /*Other tables*/

    //Add Quiz Result
    public boolean insertResult(ResultInfo resultInfo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quizID", resultInfo.getQuizID());
        contentValues.put("resultID", resultInfo.getID());
        contentValues.put("resultDate", resultInfo.getDate());
        contentValues.put("resultPercentage", resultInfo.getPercentage());
        contentValues.put("resultImproved", resultInfo.getImproved());
        return db.insert("Result", null, contentValues) > 0;
    }


    public Cursor getResults(int quizID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Result WHERE quizID = " + quizID + " ;",null);
        return res;
    }

    public int getNewResultID(int quizID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Result WHERE quizID = " + quizID + " ORDER BY resultID;",null);
        if (res.getCount() == 0)
            return 0;
        res.moveToLast();
        return res.getInt(1) + 1;
    }

    public int getNewQuizID(int conceptID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Quiz ORDER BY quizID;",null);
        if (res.getCount() == 0)
            return 0;
        res.moveToLast();
        return res.getInt(1) + 1;
    }

    public int getNewQuestionNumber(int quizID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Question WHERE quizID = " + quizID + " ORDER BY questionNumber;",null);
        if (res.getCount() == 0)
            return 0;
        res.moveToLast();
        return res.getInt(1) + 1;
    }

    public boolean insertQuiz(QuizInfo quizInfo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("conceptID", quizInfo.getConceptId());
        contentValues.put("quizID", quizInfo.getQuizId());

        return db.insert("Quiz", null, contentValues) > 0;
    }

    public int getQuizID(int conceptID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT quizID FROM Quiz WHERE conceptID = " + conceptID +";",null);

        if (res.getCount() != 0){
            res.moveToFirst();
            return res.getInt(0);
        }
        return -1;
    }

    public Cursor getQuestions(int quizID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Question WHERE quizID = " + quizID + " ;",null);
        return res;
    }

    public Cursor getAnswers(int quizID, int questionNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Answer WHERE quizID = " + quizID + " AND questionNumber = " + questionNumber + ";",null);
        return res;
    }

    public boolean insertQuestion(QuizQuestion question){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quizID", question.getQuizId());
        contentValues.put("questionNumber", question.getNumber());
        contentValues.put("questionText", question.getText());
        contentValues.put("questionImage", "");
        contentValues.put("questionPreviouslyCorrect", 0);

        return db.insert("Question", null, contentValues) > 0;
    }

    public void changePreviously(QuizQuestion question){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("questionPreviouslyCorrect", 1);

        db.update("Question", contentValues, "quizID = " + question.getQuizId() + " AND questionNumber = " + question.getNumber(), null);
    }

    public boolean insertAnswer(Answer answer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quizID", answer.getQuizId());
        contentValues.put("questionNumber", answer.getQuestionNumber());
        contentValues.put("answerLetter", answer.getLetter());
        contentValues.put("answerText", answer.getText());
        if (answer.getCorrect())
            contentValues.put("answerCorrect", 1);
        else
            contentValues.put("answerCorrect", 0);

        return db.insert("Answer", null, contentValues) > 0;
    }

    public boolean insertConcept(ConceptInfo concept){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("conceptName", concept.getName());
        contentValues.put("conceptDescription", concept.getDescription());
        contentValues.put("conceptVideo", concept.getVideo());
        contentValues.put("conceptVideoWatched", 0);
        contentValues.put("conceptQuizCompleted", 0);
        contentValues.put("conceptTutorialCompleted", 0);
        contentValues.put("conceptStepsCompleted", 0);

        return db.insert("Concept", null, contentValues) > 0;

    }

    public Cursor getConcepts(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM CONCEPT;",null);
        return res;
    }

    public Cursor getSpecifcConcept(String conceptName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM CONCEPT WHERE conceptName = '" + conceptName +"';",null);
        return res;
    }

    public boolean insertStep(Step step){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("conceptID", step.getConceptId());
        contentValues.put("stepNumber", step.getNumber());
        contentValues.put("stepText", step.getText());
        contentValues.put("stepImage", step.getImage());
        contentValues.put("stepAudio", step.getAudio());

        return db.insert("Step", null, contentValues) > 0;
    }

    public Cursor getSteps(int conceptID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT stepNumber, stepText FROM Step WHERE conceptID = " + conceptID + ";",null);
        return res;
    }

    public Cursor getStepsForTut(int conceptID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Step WHERE conceptID = " + conceptID + ";",null);
        return res;
    }

    public Step getOneStepForTut(int conceptID, int stepNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Step WHERE conceptID = " + conceptID + " AND stepNumber = " + stepNumber + ";",null);

        res.moveToFirst();
        Step step = new Step();
        step.setConceptId(conceptID);
        step.setNumber(stepNumber);
        step.setText(res.getString(2));
        step.setImage(res.getString(3));
        step.setAudio(res.getString(4));

        return step;
    }

    public int getConceptID(String conceptName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT conceptID FROM CONCEPT WHERE conceptName = '" + conceptName +"';",null);

        if (res.getCount() != 0){
            res.moveToFirst();
            return res.getInt(0);
        }
        return 0;
    }

    public Cursor getNotes(int conceptID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Note WHERE conceptID = " + conceptID + ";",null);
        return res;
    }

    public int countNotes(int conceptID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT COUNT(*) FROM Note WHERE conceptID = " + conceptID + ";", null);
        res.moveToFirst();
        return res.getInt(0);
    }

    public int getNewNoteID(int conceptID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Note WHERE conceptID = " + conceptID + " ORDER BY noteID;",null);
        if (res.getCount() == 0)
            return 0;
        res.moveToLast();
        return res.getInt(1) + 1;
    }

    public boolean insertNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("conceptID", note.getConceptId());
        contentValues.put("noteID", getNewNoteID(note.getConceptId()));
        contentValues.put("noteTitle", note.getTitle());
        contentValues.put("noteText", note.getText());
        contentValues.put("noteLastModified", note.getLastModified());

        return db.insert("Note", null, contentValues) > 0;
    }

    public void updateNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("noteTitle", note.getTitle());
        contentValues.put("noteText", note.getText());
        contentValues.put("noteLastModified", note.getLastModified());

        db.update("Note", contentValues, "conceptID = " + note.getConceptId() + " AND noteID = " + note.getId(), null);
    }

    public boolean removeNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Note", "conceptID = " + note.getConceptId() + " AND noteID = " + note.getId(), null) > 0;
    }

    public int numSteps(int conceptID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT COUNT(*) FROM Step WHERE conceptID = " + conceptID + ";", null);
        res.moveToFirst();
        return res.getInt(0);
    }

    public boolean getComplete(int conceptID, String column){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + column + " FROM Concept WHERE conceptID = " + conceptID + ";", null);
        if (res.getCount() != 0) {
            res.moveToFirst();
            if (res.getInt(0) == 0)
                return false;
            else if (res.getInt(0) == 1)
                return  true;
        }
        return false;
    }

    public void updateComplete(int conceptID,  String column){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(column, 1);

        db.update("Concept", contentValues, "conceptID = "+ conceptID,null);
    }

    public int getHighestGrade(int conceptID){
        int quizID = getQuizID(conceptID);

        if (quizID == -1)
            return -1;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT resultPercentage FROM Result WHERE quizID = " + quizID + " ORDER BY resultPercentage DESC;", null);
        res.moveToFirst();
        if (res.getCount() ==0)
            return -1;

        return res.getInt(0);
    }

    public String getVideo(int conceptID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT conceptVideo FROM Concept WHERE conceptID = " + conceptID + ";", null);
        if (res.getCount() != 0) {
            res.moveToFirst();
            if (res.getString(0).isEmpty())
                return "";
            else
                return res.getString(0);
        }
        return "";
    }

}