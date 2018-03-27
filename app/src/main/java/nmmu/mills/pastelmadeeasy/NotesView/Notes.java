package nmmu.mills.pastelmadeeasy.NotesView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nmmu.mills.pastelmadeeasy.CustomClasses.Note;
import nmmu.mills.pastelmadeeasy.Database.DatabaseHelper;
import nmmu.mills.pastelmadeeasy.R;
import nmmu.mills.pastelmadeeasy.RecyclerAdapters.NotesRecycler;

public class Notes extends AppCompatActivity {
    TextView txtConceptName;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Note> notes = new ArrayList<>();

    private int conceptID;
    private boolean noNotes = false;

    DatabaseHelper myDB;

    private Note viewnote;

    public static final String PREF_FILE_NAME="noteInfo";
    public static final String KEY = "info";
    public static final String KEY_NOTE = "ConceptsViewNoteClicked";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToPreferences(Notes.this, KEY, txtConceptName.getText().toString().trim() + "," + conceptID);
                Intent intent = new Intent(Notes.this, CreateNote.class);
                startActivityForResult(intent, 2);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtConceptName = (TextView) findViewById(R.id.concept_name);

        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            String conceptName = b.getString("conceptName");
            txtConceptName.setText(conceptName);
            if (b.get("note") == null) {//new
                conceptID = b.getInt("conceptID");
            } else{ //new
                viewnote = (Note) b.get("note");
            }
        } else {
            String string = readFromPreferences(Notes.this, KEY, "");
            String[] strings = string.split(",");
            txtConceptName.setText(strings[0]);

            conceptID = Integer.parseInt(strings[1]);
        }

        myDB = new DatabaseHelper(Notes.this);
        notes = getNotes();
        myDB.close();

        recyclerView = (RecyclerView) findViewById(R.id.notes_recycler);
        adapter = new NotesRecycler(notes, Notes.this);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        if (Boolean.valueOf(readFromPreferences(Notes.this, KEY_NOTE, false + ""))) {
            saveToPreferences(Notes.this, KEY, txtConceptName.getText().toString().trim() + "," + conceptID);
            saveToPreferences(Notes.this, KEY_NOTE,false+"");
            Intent intent = new Intent(Notes.this, CreateNote.class);
            startActivityForResult(intent, 2);
        } else if (viewnote != null){
            saveToPreferences(Notes.this, KEY, txtConceptName.getText().toString().trim() + "," + conceptID);
            Intent intent = new Intent(Notes.this, CreateNote.class);
            intent.putExtra("note", viewnote);
            startActivityForResult(intent, 2);
        }
    }

    public List<Note> getNotes(){
        List<Note> conceptNotes = new ArrayList<>();

        Cursor res = myDB.getNotes(conceptID);

        if (res.getCount() == 0){
            noNotes = true;
            Note note = new Note();
            note.setTitle("No notes");
            note.setText("To create a note click the '+' icon");
            note.setLastModified("");
            conceptNotes.add(note);
        } else {
            while (res.moveToNext()) {
                Note note = new Note();
                note.setConceptId(conceptID);
                note.setId(res.getInt(1));
                note.setTitle(res.getString(2));
                note.setText(res.getString(3));
                note.setLastModified(res.getString(4));
                conceptNotes.add(note);
            }
        }

        return conceptNotes;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2){
            if (data != null) {
                if (data.getParcelableExtra("note") != null) {
                    if (noNotes) {
                        notes.clear();
                        noNotes = false;
                    }
                    Note note = data.getParcelableExtra("note");
                    String isNew = data.getStringExtra("new");

                    note.setConceptId(conceptID);
                    if (isNew.equals("yes")) {
                        notes.add(note);
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(notes.indexOf(note));
                        addToDB(note);
                    } else if (isNew.equals("no")){
                        updateDB(note);
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(notes.indexOf(note));
                    } else if(isNew.equals("delete")){
                        deleteNote(note);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private void deleteNote(Note note) {
        myDB = new DatabaseHelper(Notes.this);
        final Note removed = note;
        if (myDB.removeNote(note)){
            Snackbar.make(txtConceptName, "Note Deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar cal = Calendar.getInstance();
                    Date date = cal.getTime();
                    String day = new SimpleDateFormat("d MMM", Locale.ENGLISH).format(date.getTime());

                    if (noNotes){
                        notes.clear();
                        noNotes = false;
                    }

                    removed.setLastModified(day);
                    notes.add(removed);
                    adapter = new NotesRecycler(notes, Notes.this);
                    recyclerView.setAdapter(adapter);
                    myDB = new DatabaseHelper(v.getContext());
                    myDB.insertNote(removed);
                    myDB.close();
                }
            }).show();
        }
        myDB.close();

        if (notes.size() == 0) {
            noNotes = true;
            Note newNote = new Note();
            newNote.setTitle("No notes");
            newNote.setText("To create a note click the '+' icon");
            newNote.setLastModified("");
            notes.add(newNote);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new NotesRecycler(getNotes(),this);
        recyclerView.setAdapter(adapter);
    }

    private void updateDB(Note note) {
        myDB = new DatabaseHelper(Notes.this);
        myDB.updateNote(note);
        myDB.close();

        Snackbar.make(txtConceptName, "Note updated",Snackbar.LENGTH_LONG).show();
    }

    private void addToDB(Note note) {
        myDB = new DatabaseHelper(Notes.this);
        Boolean added = myDB.insertNote(note);
        myDB.close();

        if (added)
            Snackbar.make(txtConceptName, "Note added",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt("conceptID", conceptID);
        outState.putString("conceptName", txtConceptName.getText().toString().trim());

        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        conceptID = savedInstanceState.getInt("conceptID");
        txtConceptName.setText(savedInstanceState.getString("conceptName"));
    }

    public static String readFromPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,preferenceValue);
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

}
