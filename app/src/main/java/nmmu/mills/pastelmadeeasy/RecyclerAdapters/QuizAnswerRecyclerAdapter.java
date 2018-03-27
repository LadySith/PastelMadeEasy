package nmmu.mills.pastelmadeeasy.RecyclerAdapters;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nmmu.mills.pastelmadeeasy.CustomClasses.Answer;
import nmmu.mills.pastelmadeeasy.R;

/**
 * Created by Steven on 14 Jun 2016.
 */
public class QuizAnswerRecyclerAdapter extends RecyclerView.Adapter<QuizAnswerRecyclerAdapter.QuizAnswerViewHolder> {
    List<Answer> answers;
    boolean previouslyCorrect;
    Activity activity;
    QuizAnswerViewHolder selectedView;


    public QuizAnswerRecyclerAdapter(List<Answer> answers, boolean previouslyCorrect, Activity activity) {
        this.previouslyCorrect = previouslyCorrect;
        this.answers = answers;
    }

    @Override
    public QuizAnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_answer_item, parent, false);
        QuizAnswerViewHolder quizAnswerViewHolder = new QuizAnswerViewHolder(view);

        return quizAnswerViewHolder;
    }

    @Override
    public void onBindViewHolder(QuizAnswerViewHolder holder, int position) {
        Answer answer = answers.get(position);

        holder.answerText.setText(answer.getLetter() + ") " + answer.getText());
        if (answer.getSelected()) {
            holder.answerCard.setBackgroundColor(Color.parseColor("#00bcd4"));
            selectedView = holder;
        }

        if (answer.getCorrect() && previouslyCorrect){
            holder.answerCard.setBackgroundColor(Color.parseColor("#00bcd4"));
            holder.tick.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= 21)
                holder.cardView.setElevation(8);
        } else if (previouslyCorrect){
            holder.cardView.setClickable(false);
            holder.answerCard.setBackgroundColor(Color.parseColor("#475d68"));
            holder.answerText.setTextColor(Color.parseColor("#c2c3c3"));
            if (Build.VERSION.SDK_INT >= 21)
                holder.cardView.setElevation(0);
        }
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public class QuizAnswerViewHolder extends RecyclerView.ViewHolder{
        LinearLayout answerCard;
        TextView answerText;
        CardView cardView;
        ImageView tick;


        public QuizAnswerViewHolder(final View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.quiz_answer_card_view);
            answerCard = (LinearLayout) itemView.findViewById(R.id.quiz_answer_card);
            answerText = (TextView) itemView.findViewById(R.id.quiz_answer_text);
            tick = (ImageView) itemView.findViewById(R.id.answer_tick);

            if (!previouslyCorrect) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Answer answer = answers.get(getAdapterPosition());
                        if (answer.getSelected()) {
                            answerCard.setBackgroundColor(Color.parseColor("#475d68"));
                        }
                        else {
                            if (selectedView != null){
                                selectedView.answerCard.setBackgroundColor(Color.parseColor("#475d68"));
                                for (Answer ans : answers){
                                    if (!ans.getLetter().equals(answer.getLetter())){
                                        ans.setSelected(false);
                                    }
                                }
                            }
                            answerCard.setBackgroundColor(Color.parseColor("#00bcd4"));
                        }
                        answer.changeSelected();
                        selectedView = QuizAnswerViewHolder.this;
                    }
                });
            }
        }
    }
}
