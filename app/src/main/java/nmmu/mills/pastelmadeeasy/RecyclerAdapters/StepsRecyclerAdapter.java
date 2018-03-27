package nmmu.mills.pastelmadeeasy.RecyclerAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nmmu.mills.pastelmadeeasy.CustomClasses.Step;
import nmmu.mills.pastelmadeeasy.R;

/**
 * Created by Steven on 10 Jun 2016.
 */
public class StepsRecyclerAdapter extends RecyclerView.Adapter<StepsRecyclerAdapter.StepViewHolder> {
    List<Step> steps;

    public StepsRecyclerAdapter(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item,parent,false);
        StepViewHolder stepViewHolder = new StepViewHolder(view);

        return stepViewHolder;
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        Step step = steps.get(position);

        holder.txtText.setText(step.getText());
        holder.txtNumber.setText("Step " + step.getNumber());
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder{
        TextView txtNumber;
        TextView txtText;

        public StepViewHolder(View itemView) {
            super(itemView);

            txtNumber = (TextView) itemView.findViewById(R.id.step_item_number);
            txtText = (TextView) itemView.findViewById(R.id.step_item_text);
        }
    }
}
