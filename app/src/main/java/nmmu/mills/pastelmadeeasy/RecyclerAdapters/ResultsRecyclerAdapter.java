package nmmu.mills.pastelmadeeasy.RecyclerAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nmmu.mills.pastelmadeeasy.CustomClasses.ResultInfo;
import nmmu.mills.pastelmadeeasy.R;

/**
 * Created by Steven on 02 Jul 2016.
 */
public class ResultsRecyclerAdapter extends RecyclerView.Adapter<ResultsRecyclerAdapter.ResultsViewHolder> {
    List<ResultInfo> results;

    public ResultsRecyclerAdapter(List<ResultInfo> results) {
        this.results = results;
    }

    @Override
    public ResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item,parent,false);
        ResultsViewHolder resultsViewHolder = new ResultsViewHolder(view);

        return resultsViewHolder;
    }

    @Override
    public void onBindViewHolder(ResultsViewHolder holder, int position) {
        ResultInfo result = results.get(position);

        String date = result.getDate();
        if (!date.equals("Now"))
            holder.txtDate.setText(date.substring(0,date.lastIndexOf(":")));
        else
            holder.txtDate.setText(date);

        holder.txtResult.setText(result.getPercentage() + "%");

        if (result.getImproved() != 0)
            holder.txtImproved.setText(result.getImproved() + "%");
        else
            holder.txtImproved.setText("-");
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ResultsViewHolder extends RecyclerView.ViewHolder{
        TextView txtDate;
        TextView txtResult;
        TextView txtImproved;

        public ResultsViewHolder(View itemView) {
            super(itemView);

            txtDate = (TextView) itemView.findViewById(R.id.result_date);
            txtResult = (TextView) itemView.findViewById(R.id.result_result);
            txtImproved = (TextView) itemView.findViewById(R.id.result_improved);
        }
    }
}
