package nmmu.mills.pastelmadeeasy.RecyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import nmmu.mills.pastelmadeeasy.CustomClasses.GenericItem;
import nmmu.mills.pastelmadeeasy.NotesView.Notes;
import nmmu.mills.pastelmadeeasy.QuizView.Quiz;
import nmmu.mills.pastelmadeeasy.R;
import nmmu.mills.pastelmadeeasy.StepsView.Steps;
import nmmu.mills.pastelmadeeasy.TutorialView.Tutorial;
import nmmu.mills.pastelmadeeasy.VideoView.Video;

/**
 * Created by Steven on 09 Jun 2016.
 */
public class GenericRecyclerAdapter extends RecyclerView.Adapter<GenericRecyclerAdapter.GenericViewHolder> {
    List<GenericItem> items;
    public static final String PREF_FILE_NAME = "PMEBasics";

    public GenericRecyclerAdapter(List<GenericItem> items) {
        this.items = items;
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_item,parent,false);
        GenericViewHolder genericViewHolder = new GenericViewHolder(view);

        return genericViewHolder;
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        GenericItem item = items.get(position);

        holder.txtTitle.setText(item.getTitle());
        holder.txtExtra.setText(item.getExtra());
        holder.txtActionStatement.setText(item.getActionStatement());

        if (item.isComplete())
            holder.imgTitleTick.setVisibility(View.VISIBLE);
        else
            holder.imgTitleTick.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class GenericViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtExtra;
        TextView txtActionStatement;
        ImageView imgTitleTick;

        public GenericViewHolder(final View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.generic_item_title);
            txtExtra = (TextView) itemView.findViewById(R.id.generic_item_extra);
            txtActionStatement = (TextView) itemView.findViewById(R.id.generic_action);
            imgTitleTick = (ImageView) itemView.findViewById(R.id.generic_title_tick);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String fragment = readFromPreferences(v.getContext(), "Fragment", "");
                    Context context = v.getContext();
                    Intent intent = new Intent();

                    switch(fragment){
                        case "Tutorials": intent = new Intent(context, Tutorial.class);
                            intent.putExtra("conceptName", items.get(getAdapterPosition()).getTitle());
                            intent.putExtra("conceptID", items.get(getAdapterPosition()).getConceptID());
                            context.startActivity(intent);
                            break;
                        case "Steps": intent = new Intent(context, Steps.class);
                            intent.putExtra("conceptName", items.get(getAdapterPosition()).getTitle());
                            intent.putExtra("conceptID", items.get(getAdapterPosition()).getConceptID());
                            context.startActivity(intent);
                            break;
                        case "Videos": intent = new Intent(context, Video.class);
                            intent.putExtra("conceptName", items.get(getAdapterPosition()).getTitle());
                            intent.putExtra("conceptID", items.get(getAdapterPosition()).getConceptID());
                            context.startActivity(intent);
                            break;
                        case "Quizzes": intent = new Intent(context, Quiz.class);
                            intent.putExtra("conceptName", items.get(getAdapterPosition()).getTitle());
                            intent.putExtra("conceptID", items.get(getAdapterPosition()).getConceptID());
                            context.startActivity(intent);
                            break;
                        case "Notes":
                            intent = new Intent(context, Notes.class);
                            intent.putExtra("conceptName", items.get(getAdapterPosition()).getTitle());
                            intent.putExtra("conceptID", items.get(getAdapterPosition()).getConceptID());
                            context.startActivity(intent);
                            break;
                    }
                }
            });
        }
    }

    public static String readFromPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, preferenceValue);
    }
}
