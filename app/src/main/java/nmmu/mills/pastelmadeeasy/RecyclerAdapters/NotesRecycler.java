package nmmu.mills.pastelmadeeasy.RecyclerAdapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nmmu.mills.pastelmadeeasy.CustomClasses.Note;
import nmmu.mills.pastelmadeeasy.Database.DatabaseHelper;
import nmmu.mills.pastelmadeeasy.NotesView.CreateNote;
import nmmu.mills.pastelmadeeasy.R;

/**
 * Created by Steven on 10 Jun 2016.
 */
public class NotesRecycler extends RecyclerView.Adapter<NotesRecycler.NotesViewHolder> {
    List<Note> notes;
    Note removed;
    Activity activity;

    DatabaseHelper myDB;

    public NotesRecycler(List<Note> notes, Activity activity) {
        this.activity = activity;
        this.notes = notes;
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item, parent, false);
        NotesViewHolder notesViewHolder = new NotesViewHolder(view);

        return notesViewHolder;
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        Note note = notes.get(position);

        holder.txtTitle.setText(note.getTitle());
        holder.txtText.setText(note.getText());
        holder.txtLastModified.setText(note.getLastModified());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtText;
        TextView txtLastModified;
        private Boolean noNotes = false;

        public NotesViewHolder(final View itemView) {
            super(itemView);

            txtLastModified = (TextView) itemView.findViewById(R.id.note_item_lastmodified);
            txtText = (TextView) itemView.findViewById(R.id.note_item_text);
            txtTitle = (TextView) itemView.findViewById(R.id.note_item_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!txtTitle.getText().toString().equals("No notes")) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, CreateNote.class);
                        intent.putExtra("note", notes.get(getAdapterPosition()));

                        if (Build.VERSION.SDK_INT >= 21) {
                            View sharedView = itemView;
                            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, sharedView, "note_transition");

                            activity.startActivityForResult(intent,2,transitionActivityOptions.toBundle());
                        } else {
                            activity.startActivityForResult(intent,2);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!txtTitle.getText().toString().equals("No notes")) {
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Delete Note")
                                .setMessage("Are you sure you want to delete the note?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        doRemove(itemView);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();

                    }
                    return false;
                }
            });
        }

        private void doRemove(View itemView) {
            removed = new Note(notes.get(getAdapterPosition()));
            notes.remove(getAdapterPosition());
            notifyDataSetChanged();

            myDB = new DatabaseHelper(itemView.getContext());
            if (myDB.removeNote(removed)){
                Snackbar.make(itemView, "Note Deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
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
                        notifyDataSetChanged();
                        myDB = new DatabaseHelper(v.getContext());
                        myDB.insertNote(removed);
                        myDB.close();
                    }
                }).show();
            }
            myDB.close();

            if (notes.size() == 0) {
                noNotes = true;
                Note note = new Note();
                note.setTitle("No notes");
                note.setText("To create a note click the '+' icon");
                note.setLastModified("");
                notes.add(note);
            }
        }
    }
}
