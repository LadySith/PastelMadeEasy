package nmmu.mills.pastelmadeeasy.QuizView;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import nmmu.mills.pastelmadeeasy.CustomClasses.ResultInfo;
import nmmu.mills.pastelmadeeasy.Database.DatabaseHelper;
import nmmu.mills.pastelmadeeasy.R;
import nmmu.mills.pastelmadeeasy.RecyclerAdapters.ResultsRecyclerAdapter;

public class Result extends AppCompatActivity {
    public static final String PREF_FILE_NAME = "PMEBasics";
    public static final String KEY_USERNAME = "username";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView txtResult;
    private TextView txtResultStatement;
    private ImageView close;

    private List<ResultInfo> results = new ArrayList<>();

    private String username;

    private int mark;
    private int quizID;
    private int conceptID;

    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        txtResult = (TextView) findViewById(R.id.result_text);
        txtResultStatement = (TextView) findViewById(R.id.result_statement);

        if (getIntent().getExtras() != null){
            Bundle b = getIntent().getExtras();
            mark = b.getInt("result");
            quizID = b.getInt("quizID");
            conceptID = b.getInt("conceptID");
            txtResult.setText(mark + "%");
        }

        close = (ImageView) findViewById(R.id.results_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        username = readFromPreferences(Result.this, KEY_USERNAME,"");

        myDB = new DatabaseHelper(Result.this);
        getResults();
        manageNewResult();
        myDB.updateComplete(conceptID,"conceptQuizCompleted");
        myDB.close();


        txtResultStatement.setText(getAppropriateStatement());

        recyclerView = (RecyclerView) findViewById(R.id.results_recycler);
        adapter = new ResultsRecyclerAdapter(results);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void manageNewResult() {
        DateFormat dateFormat = new SimpleDateFormat("d MMM, kk:mm:ss");
        Date d = new Date();
        String date = dateFormat.format(d);

        ResultInfo result = new ResultInfo(quizID);
        result.setID(myDB.getNewResultID(quizID));
        result.setPercentage(mark);
        if (results.isEmpty())
            result.setImproved(0);
        else {
            int maxID = -1;
            for (int i = 0; i < results.size(); i ++){
                ResultInfo cur = results.get(i);
                if (cur.getID() > maxID){
                    maxID = cur.getID();
                }
            }
            int oldResult = results.get(maxID).getPercentage();
            int improved = mark - oldResult;

            result.setImproved(improved);
        }
        result.setDate(date);
        boolean worked = myDB.insertResult(result);

        result.setDate("Now");
        results.add(result);
    }

    private String getAppropriateStatement(){
        String statement = "";
        String[] statements = new String[15];

        //100%
        statements[0] = "Woohoo! Go " + username + "! Go " + username + "!";
        statements[1] = "Perfect! Well done, " + username;
        //60+%
        statements[2] = "Good job, " + username +".";
        statements[3] = "Way to go, " + username + ".";
        //51+%
        statements[4] = "Good job, " + username +".";
        statements[5] = "Way to go, " + username + ".";
        //50%
        statements[6] = "Halfway there, " + username + "!";
        statements[7] = "You're almost there, " + username + "!";
        //0-49%
        statements[8] = "Keep on trying, " + username + "! You're getting there!";
        statements[9] = "Don't give up, " + username + "! You've got this!";
        statements[10] = "Keep on trying, " + username + "! It's easier than you think!";
        statements[11] = "You've almost got it, " + username + "! Give it another go!";
        statements[12] = "Why not give it another go, " + username + "! This time take it slow!";
        statements[13] = "Damn! You know you can do better, " + username + "!";
        statements[14] = "Don't give up, " + username + "! You'll get it soon!";

        Random random = new Random();
        int num = 0;

        if (mark == 100){
            num = random.nextInt(2);
        } else if (isBetween(mark,60,99)) {
            num = random.nextInt(2) + 2;
        } else if (isBetween(mark,51,59)) {
            num = random.nextInt(2) + 4;
        } else if (mark == 50) {
            num = random.nextInt(2) + 6;
        } else if (isBetween(mark,0,49)) {
            num = random.nextInt(7) + 8;
        }

        statement = statements[num];

        return statement;
    }

    public boolean isBetween(int x, int lower, int upper){
        return lower <= x && x <= upper;
    }

    private void getResults() {

        Cursor res = myDB.getResults(quizID);

        while (res.moveToNext()){
            ResultInfo result = new ResultInfo(quizID);
            result.setID(res.getInt(1));
            result.setDate(res.getString(2));
            result.setPercentage(res.getInt(3));
            result.setImproved(res.getInt(4));
            results.add(result);
        }

    }

    public static String readFromPreferences(Context context, String prefName, String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(prefName, defaultValue);
    }

}
