package nmmu.mills.pastelmadeeasy.RecyclerAdapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nmmu.mills.pastelmadeeasy.ConceptView.Concept;
import nmmu.mills.pastelmadeeasy.CustomClasses.MainItem;
import nmmu.mills.pastelmadeeasy.Database.DatabaseHelper;
import nmmu.mills.pastelmadeeasy.Home;
import nmmu.mills.pastelmadeeasy.R;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.shape.Shape;

/**
 * Created by Steven on 09 Jun 2016.
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {
    List<MainItem> items;
    Activity activity;
    private DatabaseHelper myDB;
    private String showcase_ID = "1";
    public static final String PREF_FILE_NAME = "PMEBasics";
    public static final String KEY_SHOWCASE = "PMEConceptShowcase";
    private Boolean showcaseComplete;

    public MainRecyclerAdapter(List<MainItem> items, Activity activity) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent,false);
        MainViewHolder mainViewHolder = new MainViewHolder(view);

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        MainItem item = items.get(position);

        boolean allComplete = false;

        holder.txtTitle.setText(item.getTitle());

        holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        myDB = new DatabaseHelper(activity);
        if (myDB.getComplete(item.getConceptID(),"conceptVideoWatched")) {
            allComplete = true;
            holder.txtVideo.setTextColor(Color.parseColor("#d4d4d4"));
            holder.imgVideo.setVisibility(View.VISIBLE);
        } else {
            holder.txtVideo.setTextColor(Color.parseColor("#546e7a"));
            holder.imgVideo.setVisibility(View.INVISIBLE);
            allComplete = false;
        }

        if (myDB.getComplete(item.getConceptID(),"conceptQuizCompleted")) {
            holder.txtQuiz.setTextColor(Color.parseColor("#d4d4d4"));
            holder.imgQuiz.setVisibility(View.VISIBLE);
        } else {
            holder.txtQuiz.setTextColor(Color.parseColor("#546e7a"));
            holder.imgQuiz.setVisibility(View.INVISIBLE);
            allComplete = false;
        }

        if (myDB.getComplete(item.getConceptID(),"conceptTutorialCompleted")) {
            holder.txtTutorial.setTextColor(Color.parseColor("#d4d4d4"));
            holder.imgTut.setVisibility(View.VISIBLE);
        } else {
            holder.txtTutorial.setTextColor(Color.parseColor("#546e7a"));
            holder.imgTut.setVisibility(View.INVISIBLE);
            allComplete = false;
        }

        if (myDB.getComplete(item.getConceptID(),"conceptStepsCompleted")) {
            holder.txtSteps.setTextColor(Color.parseColor("#d4d4d4"));
            holder.imgStep.setVisibility(View.VISIBLE);
        } else {
            holder.txtSteps.setTextColor(Color.parseColor("#546e7a"));
            holder.imgStep.setVisibility(View.INVISIBLE);
            allComplete = false;
        }

        myDB.close();

        //new stuff

        holder.txtSteps.setVisibility(View.VISIBLE);
        holder.txtTutorial.setVisibility(View.VISIBLE);
        holder.txtQuiz.setVisibility(View.VISIBLE);
        holder.txtVideo.setVisibility(View.VISIBLE);
        holder.checkList.setVisibility(View.VISIBLE);
        holder.bottomLine.setVisibility(View.VISIBLE);
        holder.imgTitleTick.setVisibility(View.GONE);

        if (allComplete){
            holder.imgQuiz.setVisibility(View.GONE);
            holder.imgTut.setVisibility(View.GONE);
            holder.imgVideo.setVisibility(View.GONE);
            holder.imgStep.setVisibility(View.GONE);
            holder.txtSteps.setVisibility(View.GONE);
            holder.txtTutorial.setVisibility(View.GONE);
            holder.txtQuiz.setVisibility(View.GONE);
            holder.txtVideo.setVisibility(View.GONE);
            holder.checkList.setVisibility(View.GONE);
            holder.bottomLine.setVisibility(View.GONE);
            holder.imgTitleTick.setVisibility(View.VISIBLE);
        }

        showcaseComplete = Boolean.valueOf(readFromPreferences(activity, KEY_SHOWCASE, "false"));
        if (!showcaseComplete) {
            saveToPreferences(activity,KEY_SHOWCASE,true+"");

            ShowcaseConfig config = new ShowcaseConfig();
            config.setDelay(500);

            MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(activity, showcase_ID);
            sequence.setConfig(config);
            sequence.addSequenceItem(holder.checkList, "Concept Cards show you all the tasks for the concept. When you've completed one, it will be ticked.", "Got it.");
            sequence.addSequenceItem(holder.txtSeeMore, "Tap here to see more about the concept and to perform tasks like the tutorial, steps, video, quiz, or create note tasks.", "Got it.");
            sequence.start();
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtVideo;
        TextView txtSteps;
        TextView txtQuiz;
        TextView txtTutorial;
        TextView txtSeeMore;
        ImageView imgTut;
        ImageView imgStep;
        ImageView imgQuiz;
        ImageView imgVideo;
        ImageView imgTitleTick;
        LinearLayout checkList;
        View bottomLine;


        public MainViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.home_item_title);
            txtVideo = (TextView) itemView.findViewById(R.id.home_item_video);
            txtSteps = (TextView) itemView.findViewById(R.id.home_item_steps);
            txtQuiz = (TextView) itemView.findViewById(R.id.home_item_quiz);
            txtTutorial = (TextView) itemView.findViewById(R.id.home_item_tutorial);
            txtSeeMore = (TextView) itemView.findViewById(R.id.see_more);
            imgQuiz = (ImageView) itemView.findViewById(R.id.home_item_quiz_tick);
            imgStep = (ImageView) itemView.findViewById(R.id.home_item_step_tick);
            imgTut = (ImageView) itemView.findViewById(R.id.home_item_tut_tick);
            imgVideo = (ImageView) itemView.findViewById(R.id.home_item_video_tick);
            checkList = (LinearLayout) itemView.findViewById(R.id.checkmark_list);
            imgTitleTick = (ImageView) itemView.findViewById(R.id.main_title_tick);
            bottomLine = itemView.findViewById(R.id.main_item_bottom_line);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Concept.class);
                    intent.putExtra("conceptName", items.get(getAdapterPosition()).getTitle());
                    intent.putExtra("conceptID", items.get(getAdapterPosition()).getConceptID());

                    context.startActivity(intent);
//
//                    if (Build.VERSION.SDK_INT >= 21) {
//                        View sharedView = v;
//
//                        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, sharedView, "concept_transition");
//                        context.startActivity(intent,transitionActivityOptions.toBundle());
//                    } else {
                        //context.startActivity(intent);
//                    }

                }
            });
        }
    }

    public static void saveToPreferences(Context context, String prefName, String prefValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(prefName, prefValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String prefName, String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(prefName, defaultValue);
    }
}
