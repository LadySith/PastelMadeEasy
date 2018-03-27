package nmmu.mills.pastelmadeeasy.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nmmu.mills.pastelmadeeasy.CustomClasses.WalkthroughPage;
import nmmu.mills.pastelmadeeasy.Home;
import nmmu.mills.pastelmadeeasy.R;

public class AppWalkthrough extends AppCompatActivity {

    private ImageView close;
    private ImageView next;
    private ImageView previous;
    private ImageView walkImage;
    private TextView walkText;
    private TextView skipText;

    private int currentPage = 0;

    private List<WalkthroughPage> pages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_walkthrough);

//        close = (ImageView) findViewById(R.id.tut_icon_close);
        next = (ImageView) findViewById(R.id.tut_icon_next);
        previous = (ImageView) findViewById(R.id.tut_icon_previous);
        walkImage = (ImageView) findViewById(R.id.walk_image);
        walkText = (TextView) findViewById(R.id.walk_text);
        skipText = (TextView) findViewById(R.id.skip_text);

        setUp();

        updateUI();

        skipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                close();
//            }
//        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStep();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousStep();
            }
        });
    }

    private void setUp() {

        for (int i = 0; i < 5; i++) {
            WalkthroughPage page = new WalkthroughPage();

            switch (i){
                case 0: page.setText("Pastel Made Easy is an app designed to assist you in learning the basics of Pastel.");
                    page.setImage("walk_1");
                    break;
                case 1: page.setText("To get the most out of Pastel Made Easy, start with a tutorial, which breaks down each step of the process.");
                    page.setImage("walk_2");
                    break;
                case 2: page.setText("Reinforce your knowledge by doing a quiz and tracking your improvement.");
                    page.setImage("walk_quiz");
                    break;
                case 3: page.setText("If you're stuck while performing a task, check the steps for help.");
                    page.setImage("walk_3");
                    break;
                case 4: page.setText("If all else fails, watch the video which will show you exactly how to do it.");
                    page.setImage("walk_vid");
                    break;
            }

            pages.add(page);
        }
    }

    private void previousStep() {
        if (currentPage != 0){
            currentPage--;
            updateUI();
        }
    }

    private void nextStep() {
        if (currentPage < pages.size()-1) {
            currentPage++;
            updateUI();
        } else
            close();
    }

    private void updateUI() {
        if (currentPage == 0)
            previous.setVisibility(View.INVISIBLE);
        else if (currentPage == 1)
            previous.setVisibility(View.VISIBLE);

        WalkthroughPage page = pages.get(currentPage);

        walkText.setText(page.getText());
        walkImage.setImageResource(getResources().getIdentifier(page.getImage(), "drawable", getPackageName()));
    }

    private void close(){
        Intent intent = new Intent(AppWalkthrough.this, Home.class);
        startActivity(intent);
        finish();
    }
}
