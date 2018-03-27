package nmmu.mills.pastelmadeeasy.QuizView;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nmmu.mills.pastelmadeeasy.CustomClasses.Answer;
import nmmu.mills.pastelmadeeasy.CustomClasses.QuizInfo;
import nmmu.mills.pastelmadeeasy.CustomClasses.QuizQuestion;
import nmmu.mills.pastelmadeeasy.CustomClasses.Step;
import nmmu.mills.pastelmadeeasy.Database.DatabaseHelper;
import nmmu.mills.pastelmadeeasy.NotesView.CreateNote;
import nmmu.mills.pastelmadeeasy.R;
import nmmu.mills.pastelmadeeasy.RecyclerAdapters.QuizAnswerRecyclerAdapter;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class Quiz extends AppCompatActivity{
    ImageView review;
    ImageView close;
    ImageView previous;
    ImageView next;
    TextView questionText;
    TextView questionHeader;
    TextView questionCount;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Answer> answers;

    QuizInfo quiz;

    boolean allPreviouslyCorrect = true;

    private int currentQuestion = 0;
    DatabaseHelper myDB;

    int conceptID;
    private String showcase_ID = "4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Bundle b = getIntent().getExtras();
        conceptID = b.getInt("conceptID");

        review = (ImageView) findViewById(R.id.quiz_icon_review);
        close = (ImageView) findViewById(R.id.quiz_icon_close);
        previous = (ImageView) findViewById(R.id.quiz_icon_previous);
        next = (ImageView) findViewById(R.id.quiz_icon_next);
        questionText = (TextView) findViewById(R.id.quiz_question_text);
        questionHeader = (TextView) findViewById(R.id.quiz_question_header);
        questionCount = (TextView) findViewById(R.id.quiz_question_count);

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Quiz.this, QuizReview.class);
                intent.putExtra("quiz", quiz);
                startActivityForResult(intent, 1);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousQuestion();
            }
        });

        setUp();

        boolean previouslyCorrect = quiz.getQuestions().get(currentQuestion).getPreviouslyCorrect();

        for (QuizQuestion q : quiz.getQuestions()){
            if (!q.getPreviouslyCorrect())
                allPreviouslyCorrect = false;

        }

        if (allPreviouslyCorrect)
            review.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.quiz_answer_recycler);
        adapter = new QuizAnswerRecyclerAdapter(answers, previouslyCorrect, this);//
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        if (previouslyCorrect) {
            recyclerView.setClickable(false);
        }
        else
            recyclerView.setClickable(true);

        new MaterialShowcaseView.Builder(this)
                .setTarget(review)
                .setDismissText("Got it.")
                .setContentText("Tap here to review which questions you've answered or to quickly jump to a specific question.")
                .singleUse(showcase_ID)
                .show();
    }

    private void closeDialog(){
        if (!allPreviouslyCorrect){
            new AlertDialog.Builder(Quiz.this)
                    .setTitle("Exit Quiz")
                    .setMessage("Are you sure you want to exit the quiz? Your progress will be lost.")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        } else{
            finish();
        }
    }

    private void previousQuestion() {
        if (currentQuestion != 0){
            currentQuestion--;
            updateUI();

        } else
            closeDialog();
    }

    private void updateUI(){

        QuizQuestion question = quiz.getQuestions().get(currentQuestion);

        answers = question.getAnswers();

        adapter = new QuizAnswerRecyclerAdapter(answers, question.getPreviouslyCorrect(), this);
        recyclerView.setAdapter(adapter);

        questionText.setText(question.getText());

        questionHeader.setText("Question " + (currentQuestion + 1));
        questionCount.setText((currentQuestion + 1) +" of " + quiz.getQuestions().size());
        if (question.getPreviouslyCorrect())
            recyclerView.setClickable(false);
        else
            recyclerView.setClickable(true);

        if (allPreviouslyCorrect && currentQuestion == quiz.getQuestions().size()-1)
            next.setVisibility(View.INVISIBLE);
        else
            next.setVisibility(View.VISIBLE);
    }

    private void nextQuestion() {
        if (currentQuestion < quiz.getQuestions().size()-1){
            currentQuestion++;
            updateUI();
        } else {
            Intent intent = new Intent(Quiz.this, QuizReview.class);
            intent.putExtra("quiz", quiz);
            startActivityForResult(intent, 1);
        }
    }

    private void setUp(){
        quiz = getQuizInfo();
        QuizQuestion question = quiz.getQuestions().get(currentQuestion);
        answers = question.getAnswers();

        questionText.setText(question.getText());

        questionHeader.setText("Question " + (currentQuestion + 1));
        questionCount.setText((currentQuestion + 1) +" of " + quiz.getQuestions().size());


    }

    public QuizInfo getQuizInfo(){
        QuizInfo quizInfo = new QuizInfo();
        myDB = new DatabaseHelper(Quiz.this);
        quizInfo.setQuizId(myDB.getQuizID(conceptID));

        if (quizInfo.getQuizId() == -1) {
            quizInfo.setQuizId(0);
        }

        quizInfo.setConceptId(conceptID);

        Cursor res = myDB.getQuestions(quizInfo.getQuizId());

        while (res.moveToNext()) {
            QuizQuestion question = new QuizQuestion();
            question.setNumber(res.getInt(1));
            question.setQuizId(quizInfo.getQuizId());
            question.setText(res.getString(2));
            if (res.getInt(4) == 0)
                question.setPreviouslyCorrect(false);
            else
                question.setPreviouslyCorrect(true);

            Cursor answerRes = myDB.getAnswers(quizInfo.getQuizId(),question.getNumber());

            while (answerRes.moveToNext()){
                Answer answer = new Answer();
                answer.setQuestionNumber(question.getNumber());
                answer.setQuizId(quizInfo.getQuizId());
                answer.setLetter(answerRes.getString(2));
                answer.setText(answerRes.getString(3));
                int binaryTF = answerRes.getInt(4);

                if (binaryTF == 0)
                    answer.setCorrect(false);
                else if (binaryTF == 1)
                    answer.setCorrect(true);

                if (question.getPreviouslyCorrect() && answer.getCorrect())
                    answer.changeSelected();

                question.addAnswer(answer);
            }

            quizInfo.addQuestion(question);
        }

        myDB.close();
        return quizInfo;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (data != null){
                currentQuestion = data.getIntExtra("view", 0);
                if (currentQuestion != -1)
                    updateUI();
                else {
                    updateDB();
                    Intent intent = new Intent(Quiz.this, Result.class);
                    intent.putExtra("result",markQuiz());
                    intent.putExtra("quizID", quiz.getQuizId());
                    intent.putExtra("conceptID", quiz.getConceptId());
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    public void updateDB(){
        for (QuizQuestion question: quiz.getQuestions()){
            if (question.actualMark() == question.possibleMarks()) {
                myDB = new DatabaseHelper(this);
                myDB.changePreviously(question);
                myDB.close();
            }
        }
    }

    public int markQuiz() {
        int possibleMarks = 0;
        int actualMarks = 0;

        for (QuizQuestion question: quiz.getQuestions()){
                possibleMarks += question.possibleMarks();
                actualMarks += question.actualMark();
        }

        float mark = (float) actualMarks / (float) possibleMarks * 100.0f;

        return Math.round(mark);
    }

    @Override
    public void onBackPressed() {
        closeDialog();
    }
}
