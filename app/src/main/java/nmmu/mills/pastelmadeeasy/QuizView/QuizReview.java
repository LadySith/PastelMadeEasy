package nmmu.mills.pastelmadeeasy.QuizView;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import nmmu.mills.pastelmadeeasy.CustomClasses.QuizInfo;
import nmmu.mills.pastelmadeeasy.CustomClasses.QuizQuestion;
import nmmu.mills.pastelmadeeasy.R;
import nmmu.mills.pastelmadeeasy.RecyclerAdapters.QuizReviewRecylerAdapter;

public class QuizReview extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private QuizInfo quizInfo = new QuizInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_review);

        Bundle b = getIntent().getExtras();
        if (b != null){
            if (b.getParcelable("quiz") != null) {
                quizInfo = b.getParcelable("quiz");
            }
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizReview.this, Quiz.class);
                intent.putExtra("view", -1);
                setResult(1, intent);
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.review_recycler);
        adapter = new QuizReviewRecylerAdapter(quizInfo.getQuestions(),QuizReview.this);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}
