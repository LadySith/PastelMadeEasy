package nmmu.mills.pastelmadeeasy.NotesView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import nmmu.mills.pastelmadeeasy.CustomClasses.Note;
import nmmu.mills.pastelmadeeasy.R;

public class CreateNote extends AppCompatActivity {
    EditText txtTitle;
    EditText txtText;

    Boolean titleEmpty = true;
    Boolean textEmpty = true;

    Note note = new Note();

    String originalTitle = "";
    String originalText = "";

    Boolean oldNote = false;
    Boolean changes = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtText = (EditText) findViewById(R.id.create_note_text);
        txtTitle = (EditText) findViewById(R.id.create_note_title);

        Bundle b = getIntent().getExtras();
        if (b != null){
            if (b.getParcelable("note") != null) {
                note = b.getParcelable("note");

                originalText = note.getText();
                originalTitle = note.getTitle();
                txtText.setText(note.getText());
                txtTitle.setText(note.getTitle());

                txtText.requestFocus();

                oldNote = true;
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleEmpty = true;
                textEmpty = true;
                checkInput();
                if (!titleEmpty && !textEmpty) {

                    if (changes || !oldNote) {
                        Intent intent = new Intent(CreateNote.this, Notes.class);
                        intent.putExtra("note", makeNote());
                        if (!oldNote)
                            intent.putExtra("new","yes");
                        else
                            intent.putExtra("new","no");
                        setResult(2, intent);
                    }

                    finish();
                } else
                    displayError(view);
            }
        });

    }

    @Override
    public void onBackPressed() {
        setResult(2);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_delete){
            deleteDialog();
        }

        if (id == android.R.id.home){
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkInput(){
        if (!txtTitle.getText().toString().trim().isEmpty())
            titleEmpty = false;

        if (!txtText.getText().toString().trim().isEmpty())
            textEmpty = false;

        if (oldNote){
            if (!originalTitle.equals(txtTitle.getText().toString()) || !originalText.equals(txtText.getText().toString()))
                changes = true;
        }
    }

    public void displayError(View view){
        if (textEmpty && titleEmpty)
            Snackbar.make(view,"Note title & body is empty.", Snackbar.LENGTH_LONG).show();
        else if (textEmpty)
            Snackbar.make(view,"Note body is empty.", Snackbar.LENGTH_LONG).show();
        else if (titleEmpty)
            Snackbar.make(view,"Note title is empty.", Snackbar.LENGTH_LONG).show();
    }

    public Note makeNote(){
        if (!changes) {
            Note note = new Note();
        }
        note.setTitle(txtTitle.getText().toString().trim());
        note.setText(txtText.getText().toString().trim());
        note.setLastModified(getLastModifiedString());

        return  note;
    }

    public String getLastModifiedString(){
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String day = new SimpleDateFormat("d MMM", Locale.ENGLISH).format(date.getTime());
        return day;
    }

    public void deleteDialog(){
        new AlertDialog.Builder(CreateNote.this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete the note?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (oldNote){
                            Intent intent = new Intent(CreateNote.this, Notes.class);
                            intent.putExtra("note", note);
                            intent.putExtra("new","delete");
                            setResult(2, intent);
                            finish();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

}
