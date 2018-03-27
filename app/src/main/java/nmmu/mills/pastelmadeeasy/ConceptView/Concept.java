package nmmu.mills.pastelmadeeasy.ConceptView;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import nmmu.mills.pastelmadeeasy.CustomClasses.ConceptInfo;
import nmmu.mills.pastelmadeeasy.CustomClasses.Note;
import nmmu.mills.pastelmadeeasy.Database.DatabaseHelper;
import nmmu.mills.pastelmadeeasy.NotesView.Notes;
import nmmu.mills.pastelmadeeasy.QuizView.Quiz;
import nmmu.mills.pastelmadeeasy.R;
import nmmu.mills.pastelmadeeasy.RecyclerAdapters.ConceptNotesRecycler;
import nmmu.mills.pastelmadeeasy.StepsView.Steps;
import nmmu.mills.pastelmadeeasy.TutorialView.Tutorial;
import nmmu.mills.pastelmadeeasy.VideoView.Video;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class Concept extends AppCompatActivity {
    private TextView txtConceptName;
    private TextView txtConceptDescription;
    //private TextView txtDescriptionHeading;
    private RelativeLayout conceptRoot;
    private AppBarLayout appBar;
    private LinearLayout contentRoot;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Note> notes = new ArrayList<>();

    private  String conceptName;
    private ConceptInfo conceptInfo;
    private DatabaseHelper myDB;
    private FloatingActionsMenu menu;

    public static final String PREF_FILE_NAME = "noteInfo";
    public static final String KEY_NOTE = "ConceptsViewNoteClicked";

    private String showcase_ID = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concept);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        conceptRoot = (RelativeLayout) findViewById(R.id.concept_root);
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        contentRoot = (LinearLayout) findViewById(R.id.content_root);
        //txtDescriptionHeading = (TextView) findViewById(R.id.description_heading);

        menu = (FloatingActionsMenu) findViewById(R.id.fab_menu);

        menu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {

                if (Build.VERSION.SDK_INT >= 21)
                    doReveal();
                else
                    conceptRoot.setBackgroundColor(Color.parseColor("#546e7a"));

                appBar.setVisibility(View.INVISIBLE);
                contentRoot.setVisibility(View.INVISIBLE);
                txtConceptDescription.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                if (Build.VERSION.SDK_INT >= 21)
                    doReturnReveal();

                appBar.setVisibility(View.VISIBLE);
                contentRoot.setVisibility(View.VISIBLE);
                txtConceptDescription.setVisibility(View.VISIBLE);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        conceptName = b.getString("conceptName");

        txtConceptName = (TextView) findViewById(R.id.concept_name);
        txtConceptName.setText(conceptName.trim());

        conceptInfo = new ConceptInfo(conceptName.trim());

        conceptInfo = getConceptInformation(conceptInfo.getName());

        txtConceptDescription = (TextView) findViewById(R.id.concept_description);
        String description = conceptInfo.getDescription();
        txtConceptDescription.setText(description);

        FloatingActionButton fabTut = (FloatingActionButton) findViewById(R.id.fab_tut);
        fabTut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Concept.this, Tutorial.class);
                intent.putExtra("conceptName", conceptInfo.getName());
                intent.putExtra("conceptID", conceptInfo.getId());
                startActivity(intent);
            }
        });
        FloatingActionButton fabStep = (FloatingActionButton) findViewById(R.id.fab_step);
        fabStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Concept.this, Steps.class);
                intent.putExtra("conceptName", conceptInfo.getName());
                intent.putExtra("conceptID", conceptInfo.getId());
                startActivity(intent);
            }
        });

        FloatingActionButton fabQuiz = (FloatingActionButton) findViewById(R.id.fab_quiz);
        fabQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Concept.this, Quiz.class);
                intent.putExtra("conceptName", conceptInfo.getName());
                intent.putExtra("conceptID", conceptInfo.getId());
                startActivity(intent);
            }
        });

        FloatingActionButton fabVideo = (FloatingActionButton) findViewById(R.id.fab_video);
        fabVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Concept.this, Video.class);
                intent.putExtra("conceptName", conceptInfo.getName());
                intent.putExtra("conceptID", conceptInfo.getId());
                startActivity(intent);

            }
        });

        FloatingActionButton fabNote = (FloatingActionButton) findViewById(R.id.fab_note);
        fabNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToPreferences(Concept.this,KEY_NOTE,true+"");
                Intent intent = new Intent(Concept.this, Notes.class);
                intent.putExtra("conceptName", conceptInfo.getName());
                intent.putExtra("conceptID", conceptInfo.getId());
                startActivity(intent);
            }
        });

        if (!conceptInfo.getNotes().isEmpty())
            notes = conceptInfo.getNotes();
        else {
            notes.clear();
            Note note = new Note();
            note.setTitle("No notes");
            note.setText("Tap the action button, then tap the 'Create Note'");
            notes.add(note);
        }

        recyclerView = (RecyclerView) findViewById(R.id.concept_notes_recycler);
        adapter = new ConceptNotesRecycler(notes, Concept.this, conceptName);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        new MaterialShowcaseView.Builder(this)
                .setTarget(fabNote)
                .setDismissText("Got it.")
                .setContentText("Tap here to display the menu of tasks that can be performed, which are the tutorial, steps, video, quiz, and create note tasks.")
                .singleUse(showcase_ID)
                .setDelay(500)
                .show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (menu.isExpanded())
            menu.collapseImmediately();

        notes.clear();
        getNotes();
        if (!conceptInfo.getNotes().isEmpty())
            notes = conceptInfo.getNotes();
        else {
            Note note = new Note();
            note.setTitle("No notes");
            note.setText("Tap the action button, then tap 'Create Note'");
            notes.add(note);
        }

        adapter = new ConceptNotesRecycler(notes, Concept.this,conceptName);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (menu.isExpanded()){
            menu.collapse();
        } else {
            super.onBackPressed();
        }
    }

    private void getNotes(){
        Cursor res = myDB.getNotes(conceptInfo.getId());

        if (res.getCount() != 0){
            while (res.moveToNext()) {
                Note note = new Note();
                note.setConceptId(conceptInfo.getId());
                note.setId(res.getInt(1));
                note.setTitle(res.getString(2));
                note.setText(res.getString(3));
                note.setLastModified(res.getString(4));
                conceptInfo.addNote(note);
            }
        }
    }

    private ConceptInfo getConceptInformation(String conceptName){
        //do database stuff

        myDB = new DatabaseHelper(Concept.this);

        Cursor res = myDB.getSpecifcConcept(conceptName);

        while (res.moveToNext()) {
            conceptInfo.setId(res.getInt(0));
            conceptInfo.setDescription(res.getString(2));
            conceptInfo.setVideo(res.getString(3));
        }

        //do database stuff to get Notes;

        getNotes();

        myDB.close();
        return conceptInfo;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void doReveal(){
        int cx = conceptRoot.getRight();
        int cy = (conceptRoot.getBottom());
        int finalRadius = Math.max(conceptRoot.getWidth(), conceptRoot.getHeight() + 500);

        Animator animator = ViewAnimationUtils.createCircularReveal(conceptRoot, cx, cy, 0, finalRadius);
        conceptRoot.setBackgroundColor(Color.parseColor("#546e7a"));
        animator.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void doReturnReveal(){
        int cx = conceptRoot.getLeft();
        int cy = (conceptRoot.getTop());
        int finalRadius = Math.max(conceptRoot.getWidth(), conceptRoot.getHeight()+500);

        Animator animator = ViewAnimationUtils.createCircularReveal(conceptRoot, cx, cy, 0, finalRadius);
        conceptRoot.setBackgroundColor(Color.parseColor("#ffffff"));
        animator.start();
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }
}
