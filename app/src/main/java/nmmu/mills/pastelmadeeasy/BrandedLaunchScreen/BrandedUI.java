package nmmu.mills.pastelmadeeasy.BrandedLaunchScreen;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import nmmu.mills.pastelmadeeasy.CustomClasses.Answer;
import nmmu.mills.pastelmadeeasy.CustomClasses.ConceptInfo;
import nmmu.mills.pastelmadeeasy.CustomClasses.QuizInfo;
import nmmu.mills.pastelmadeeasy.CustomClasses.QuizQuestion;
import nmmu.mills.pastelmadeeasy.CustomClasses.Step;
import nmmu.mills.pastelmadeeasy.Database.DatabaseHelper;
import nmmu.mills.pastelmadeeasy.Home;
import nmmu.mills.pastelmadeeasy.Login.LoginActivity;
import nmmu.mills.pastelmadeeasy.R;

public class BrandedUI extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    public static final String PREF_FILE_NAME = "PMEBasics";
    public static final String KEY_DATA = "PMEData";
    public static final String KEY_FIRST_TIME = "PMEFirstTime";
    public static final String KEY_USERNAME = "username";


    private Boolean firstTime = true;
    private String username = "";
    private Boolean dataLoaded = false;

    ImageView logo;
    TextView txtStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branded_ui);

        firstTime = Boolean.valueOf(readFromPreferences(BrandedUI.this, KEY_FIRST_TIME, "true"));
        username = readFromPreferences(BrandedUI.this, KEY_USERNAME, "");
        dataLoaded = Boolean.valueOf(readFromPreferences(BrandedUI.this, KEY_DATA, "false"));
        logo = (ImageView) findViewById(R.id.logo);
        txtStart = (TextView) findViewById(R.id.pme_start_text);


        if (firstTime) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BrandedUI.this, LoginActivity.class);

                    if (Build.VERSION.SDK_INT >= 21) {
                        View sharedView = v;

                        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(BrandedUI.this, sharedView, "logo_transition");
                        startActivity(intent, transitionActivityOptions.toBundle());
                        getWindow().setExitTransition(null);
                        finish();
                    }
                }
            });
        }

        if (firstTime) {
            if (!dataLoaded) {
                populateDatabase();
                populateSteps();
                populateQuiz();
                saveToPreferences(BrandedUI.this, KEY_DATA, true + "");
            }
            txtStart.setVisibility(View.VISIBLE);
        } else {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        nextActivity();
                    }
                }
            };
            AsyncTask.execute(runnable);
        }
    }

    public void nextActivity(){
        if (!firstTime && username != "") {
            Intent intent = new Intent(BrandedUI.this, Home.class);
            startActivity(intent);
            BrandedUI.this.finish();
        }
    }

    public void populateDatabase(){
        DatabaseHelper db = new DatabaseHelper(BrandedUI.this);
        try{
            InputStream in = getResources().openRawResource(getResources().getIdentifier("pme_data","raw", getPackageName()));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                String[] items = line.split(",");
                ConceptInfo conceptInfo = new ConceptInfo(items[0]);
                String description = items[1];
                String video = items[2];
                while (description.contains(";")){
                    int pos = description.indexOf(";");
                    String curPiece = description.substring(0, pos);
                    String nextPiece = description.substring(pos+1, description.length());
                    description = curPiece + "," + nextPiece;
                }
                while (description.contains("/n")){
                    int pos = description.indexOf("/n");
                    String curPiece = description.substring(0, pos-1);
                    String nextPiece = description.substring(pos+3, description.length());
                    description = curPiece + "\r\n\n" + nextPiece;
                }
                conceptInfo.setDescription(description);
                conceptInfo.setVideo(video);
                Boolean worked = db.insertConcept(conceptInfo);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void populateSteps(){
        DatabaseHelper db = new DatabaseHelper(BrandedUI.this);
        try{
            InputStream in = getResources().openRawResource(getResources().getIdentifier("steps","raw", getPackageName()));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                int stepCounter = 1;
                String[] items = line.split(",");
                int conceptID = db.getConceptID(items[0]);
                for (int i = 1; i < items.length; i+=3){
                    Step step = new Step();
                    step.setConceptId(conceptID);
                    step.setNumber(stepCounter);
                    step.setText(items[i].replace(";", ","));
                    step.setImage(items[i + 1]);
                    step.setAudio(items[i + 2]);

                    Boolean worked = db.insertStep(step);
                    stepCounter++;
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void populateQuiz(){
        DatabaseHelper db = new DatabaseHelper(BrandedUI.this);
        try{
            InputStream in = getResources().openRawResource(getResources().getIdentifier("quiz","raw", getPackageName()));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            boolean sameQuiz = false;
            String concept = "";
            int quizID = 0;
            while ((line = br.readLine()) != null) {
                String[] items = line.split(",");

                if (concept == ""){
                    concept = items[0];
                    sameQuiz = false;
                } else if (concept.equals(items[0])){
                    sameQuiz = true;
                } else  {
                    sameQuiz = false;
                    concept = items[0];
                }

                int conceptID = db.getConceptID(items[0]);

                if (!sameQuiz) {
                    quizID = db.getNewQuizID(conceptID);
                    QuizInfo quizInfo = new QuizInfo();
                    quizInfo.setConceptId(conceptID);
                    quizInfo.setQuizId(quizID);
                    Boolean quizWorked = db.insertQuiz(quizInfo);
                }

                QuizQuestion question = new QuizQuestion();
                int questionNumber = db.getNewQuestionNumber(quizID);
                question.setText(items[1]);
                question.setQuizId(quizID);
                question.setNumber(questionNumber);
                Boolean questionWorked = db.insertQuestion(question);

                for (int i = 2; i < items.length; i+=3){
                    Answer answer = new Answer();
                    answer.setQuizId(quizID);
                    answer.setQuestionNumber(questionNumber);
                    answer.setLetter(items[i]);
                    answer.setText(items[i+1]);
                    answer.setCorrect(Boolean.parseBoolean(items[i+2]));

                    Boolean worked = db.insertAnswer(answer);
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String prefName, String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(prefName, defaultValue);
    }

}
