package nmmu.mills.pastelmadeeasy.RecyclerAdapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nmmu.mills.pastelmadeeasy.CustomClasses.Answer;
import nmmu.mills.pastelmadeeasy.CustomClasses.QuizQuestion;
import nmmu.mills.pastelmadeeasy.QuizView.Quiz;
import nmmu.mills.pastelmadeeasy.QuizView.QuizReview;
import nmmu.mills.pastelmadeeasy.R;

/**
 * Created by Steven on 27 Jun 2016.
 */
public class QuizReviewRecylerAdapter extends RecyclerView.Adapter<QuizReviewRecylerAdapter.QuizReviewViewHolder> {
    List<QuizQuestion> questions;
    Activity activity;

    public QuizReviewRecylerAdapter(List questions, Activity activity) {
        this.questions = questions;
        this.activity = activity;
    }

    @Override
    public QuizReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_answer_item, parent,false);
        QuizReviewViewHolder quizReviewViewHolder = new QuizReviewViewHolder(view);

        return quizReviewViewHolder;
    }

    @Override
    public void onBindViewHolder(QuizReviewViewHolder holder, int position) {
        QuizQuestion question = questions.get(position);

        if (question.getAnswered()) {
            for (Answer answer : question.getAnswers())
                if (answer.getCorrect())
                    holder.questionText.setText("Question " + (question.getNumber() + 1) + ": " + answer.getLetter());
        } else
            holder.questionText.setText("Question " + (question.getNumber() + 1) + ": " + "Unanswered");
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class QuizReviewViewHolder extends RecyclerView.ViewHolder {
        TextView questionText;

        public QuizReviewViewHolder(View itemView) {
            super(itemView);

            questionText = (TextView) itemView.findViewById(R.id.quiz_answer_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, Quiz.class);
                    intent.putExtra("view", getAdapterPosition());
                    activity.setResult(1, intent);
                    activity.finish();
                }
            });


        }
    }
}
