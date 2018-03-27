package nmmu.mills.pastelmadeeasy.RecyclerAdapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nmmu.mills.pastelmadeeasy.CustomClasses.Note;
import nmmu.mills.pastelmadeeasy.NotesView.CreateNote;
import nmmu.mills.pastelmadeeasy.NotesView.Notes;
import nmmu.mills.pastelmadeeasy.R;

/**
 * Created by Steven on 10 Jun 2016.
 */
public class ConceptNotesRecycler extends RecyclerView.Adapter<ConceptNotesRecycler.ConceptNoteViewHolder> {
    private List<Note> notes;
    private Activity activity;
    private String conceptName;

    public ConceptNotesRecycler(List<Note> notes_in, Activity activity, String conceptName) {
        this.activity = activity;
        this.notes = notes_in;
        this.conceptName = conceptName;
    }

    @Override
    public ConceptNoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.concept_note_item, parent, false);
        ConceptNoteViewHolder conceptNoteViewHolder = new ConceptNoteViewHolder(view);

        return conceptNoteViewHolder;
    }

    @Override
    public void onBindViewHolder(ConceptNoteViewHolder holder, int position) {
        Note note = notes.get(position);

        holder.txtTitle.setText(note.getTitle());
        holder.txtLastModified.setText(note.getLastModified());
        String text = note.getText();
        if (text.length() > 49)
            holder.txtText.setText(text.substring(0,49) + "...");
        else
            holder.txtText.setText(text);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ConceptNoteViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle;
        TextView txtLastModified;
        TextView txtText;

        public ConceptNoteViewHolder(View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.concept_note_item_title);
            txtText = (TextView) itemView.findViewById(R.id.concept_note_item_text);
            txtLastModified = (TextView) itemView.findViewById(R.id.concept_note_item_lastmodified);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!txtTitle.getText().toString().equals("No notes")) {
                        Context context = v.getContext();
//                        Intent intent = new Intent(context, CreateNote.class);
                        Intent intent = new Intent(context, Notes.class);
                        intent.putExtra("note", notes.get(getAdapterPosition()));
                        intent.putExtra("conceptName",conceptName);

                        context.startActivity(intent);

//                        if (Build.VERSION.SDK_INT >= 21) {
//                            View sharedView = v;
//
//                            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, sharedView, "note_transition");
//                            context.startActivity(intent, transitionActivityOptions.toBundle());
//                        } else {
//                            context.startActivity(intent);
//                        }
                    }
                }
            });
        }
    }
}
