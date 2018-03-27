package nmmu.mills.pastelmadeeasy.TutorialView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nmmu.mills.pastelmadeeasy.CustomClasses.Step;
import nmmu.mills.pastelmadeeasy.Database.DatabaseHelper;
import nmmu.mills.pastelmadeeasy.R;
import uk.co.deanwild.materialshowcaseview.IShowcaseListener;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class Tutorial extends AppCompatActivity {
    ImageView audio;
    ImageView close;
    ImageView previous;
    ImageView next;
    TextView stepCount;
    TextView stepHeader;
    ImageView imgStep;
    CoordinatorLayout rootBackground;
    View bottomLine;

    LinearLayout tutBackground;

    MediaPlayer mediaPlayer = new MediaPlayer();

    String[] colours;
    String[] lineColours; //New Line

    private int currentStep = 0;

    Step current; // added for single step mode

    private Animator animator;
    private Boolean firstTime = true;
    private Boolean listenToAudio = false;
    private Boolean justChanged = false;

    DatabaseHelper myDB;

    int conceptID;
    int numSteps;

    private String showcase_ID = "3";
    public static final String PREF_FILE_NAME = "PMEBasics";
    public static final String KEY_AUDIO_SHOWCASE = "AudioShowCaseShown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        Bundle b = getIntent().getExtras();
        conceptID = b.getInt("conceptID");

        audio = (ImageView) findViewById(R.id.tut_icon_audio);
        close = (ImageView) findViewById(R.id.tut_icon_close);
        previous = (ImageView) findViewById(R.id.tut_icon_previous);
        next = (ImageView) findViewById(R.id.tut_icon_next);
        bottomLine = findViewById(R.id.bottom_line);

        stepHeader = (TextView) findViewById(R.id.step_header);
        stepCount = (TextView) findViewById(R.id.step_count);
        imgStep = (ImageView) findViewById(R.id.tut_step_img);

        tutBackground = (LinearLayout) findViewById(R.id.tut_background);
        rootBackground = (CoordinatorLayout) findViewById(R.id.tut_root_background);

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

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        imgStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage(v);
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustMediaPlayer();
            }
        });

        audio.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                changeListening();
                return false;
            }
        });

        setColours();

        setup();
    }

    private  void changeListening(){
        listenToAudio = !listenToAudio;
        justChanged = true;
        if (listenToAudio)
            Snackbar.make(audio,"Listening to audio instructions.", Snackbar.LENGTH_SHORT).show();
        else {
            mediaPlayer.stop();
            Snackbar.make(audio, "Not listening to audio instructions.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void adjustMediaPlayer() {
        if (listenToAudio && !justChanged) {
            if (mediaPlayer.isPlaying()) {
                audio.setImageResource(R.drawable.ic_play);
                mediaPlayer.pause();
            } else {
                audio.setImageResource(R.drawable.ic_pause);
                mediaPlayer.start();
            }
        } else {
            justChanged = false;
            audio.setImageResource(R.drawable.ic_play);
            configureMediaPlayer();
        }
    }

    private void openImage(View v) {
        Intent intent = new Intent(Tutorial.this, ImageViewActivity.class);
        intent.putExtra("imageID",getResources().getIdentifier(current.getImage(), "drawable", getPackageName()));

        if (Build.VERSION.SDK_INT >= 21) {
            View sharedView = v;

            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(Tutorial.this, sharedView, "image_transition");
            startActivity(intent, transitionActivityOptions.toBundle());
        } else {
            startActivity(intent);
        }
    }

    private void previousStep() {
        if (currentStep != 0){
            currentStep--;
            mediaPlayer.stop();
            rootBackground.setBackgroundColor(Color.parseColor(colours[currentStep+1]));
            updateUI();

        } else
            closeDialog();
    }

    private void setup() {
        getStep();
        stepHeader.setText(current.getText());
        stepCount.setText((currentStep + 1) + " of " + numSteps);
        imgStep.setImageResource(getResources().getIdentifier(current.getImage(), "drawable", getPackageName()));

        Boolean shown = Boolean.valueOf(readFromPreferences(this, KEY_AUDIO_SHOWCASE, "false"));

        if (!shown) {
            saveToPreferences(this, KEY_AUDIO_SHOWCASE, true + "");
            performShowcase();
        } else {
            showAudioDialog();
        }
    }

    private void showAudioDialog() {
        new AlertDialog.Builder(Tutorial.this)
                .setTitle("Listen to audio instructions?")
                .setMessage("The standard way of performing the tutorial includes listening to audio instructions. You can change this at any time by long pressing the icon in the top left corner.")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listenToAudio = true;
                        configureMediaPlayer();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listenToAudio = false;
                    }
                })
                .show();
    }

    private void performShowcase(){
        new MaterialShowcaseView.Builder(this)
                .setTarget(audio)
                .setDismissText("Got it.")
                .setContentText("Tap here to pause or replay the step audio. To toggle audio on/off, long-press.")
                .singleUse(showcase_ID)
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {

                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
                        showAudioDialog();
                    }
                })
                .show();
    }

    private void getStep(){
        myDB = new DatabaseHelper(Tutorial.this);
        current = myDB.getOneStepForTut(conceptID,(currentStep+1));

        myDB.close();
    }

    private void nextStep() {
        if (currentStep < numSteps-1) {
            currentStep++;
            mediaPlayer.stop();
            rootBackground.setBackgroundColor(Color.parseColor(colours[currentStep-1]));
            updateUI();
        } else if (currentStep == numSteps-1){
            updateDB();
        }
    }

    private void updateUI() {
        getStep();

        stepHeader.setText(current.getText());
        stepCount.setText((currentStep + 1) + " of " + numSteps);
        imgStep.setImageResource(getResources().getIdentifier(current.getImage(), "drawable", getPackageName()));

        bottomLine.setBackgroundColor(Color.parseColor(lineColours[currentStep])); //New Line

        if (Build.VERSION.SDK_INT >= 21)
            doReveal();
        else
            tutBackground.setBackgroundColor(Color.parseColor(colours[currentStep]));

        configureMediaPlayer();
    }

    private void setColours() {
        myDB = new DatabaseHelper(Tutorial.this);
        numSteps = myDB.numSteps(conceptID);
        myDB.close();
        int count = 1;

        try{
            InputStream in = getResources().openRawResource(getResources().getIdentifier("colours","raw", getPackageName()));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                if (count == numSteps) {
                    String[] items = line.split(",");
                    colours = new String[numSteps];

                    for (int i = 0; i < items.length; i++){
                        colours[i] = items[i];
                    }

                    in.close();
                    break;
                }
                count++;
            }

            //New color line

            lineColours = new String[numSteps];
            lineColours[0] = "#475d68";

            for (int i = 1; i < colours.length; i++){
                lineColours[i] = colours[i-1];
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void closeDialog(){
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        new AlertDialog.Builder(Tutorial.this)
                .setTitle("Exit Tutorial")
                .setMessage("Are you sure you want to exit the tutorial?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (currentStep == (numSteps-1)){
                            updateDB();
                        } else
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void configureMediaPlayer(){
        if (listenToAudio) {
            audio.setImageResource(R.drawable.ic_play);
//            mediaPlayer = MediaPlayer.create(Tutorial.this, getResources().getIdentifier(steps.get(currentStep).getAudio(), "raw", getPackageName()));
            mediaPlayer = MediaPlayer.create(Tutorial.this, getResources().getIdentifier(current.getAudio(), "raw", getPackageName()));
//            if (Build.VERSION.SDK_INT >= 21) {
//                if (!firstTime && !justChanged) {
//                    animator.addListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            mediaPlayer.start();
//                            super.onAnimationEnd(animation);
//                        }
//                    });
//
//                    if (!animator.isRunning())
//                        mediaPlayer.start();
//                } else {
//                    mediaPlayer.start();
//                    firstTime = false;
//                }
//            } else
                mediaPlayer.start();

            audio.setImageResource(R.drawable.ic_pause);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    audio.setImageResource(R.drawable.ic_replay);
                }
            });
        }
    }

    private void doReveal(){
        int cx = (tutBackground.getLeft() + tutBackground.getRight()) / 2;
        int cy = (tutBackground.getBottom());
        int finalRadius = Math.max(tutBackground.getWidth(), tutBackground.getHeight());

        animator = ViewAnimationUtils.createCircularReveal(tutBackground,cx,cy,0,finalRadius);
        tutBackground.setBackgroundColor(Color.parseColor(colours[currentStep]));
        animator.start();
    }

    private void updateDB(){
        myDB = new DatabaseHelper(Tutorial.this);
        myDB.updateComplete(conceptID, "conceptTutorialCompleted");
        myDB.close();
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        startActivity(new Intent(Tutorial.this, TutComplete.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        closeDialog();
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
