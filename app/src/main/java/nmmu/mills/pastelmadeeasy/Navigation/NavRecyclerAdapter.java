package nmmu.mills.pastelmadeeasy.Navigation;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import nmmu.mills.pastelmadeeasy.R;

/**
 * Created by Steven on 08 Jun 2016.
 */
public class NavRecyclerAdapter extends RecyclerView.Adapter<NavRecyclerAdapter.NavItemViewHolder> {
    private List<NavigationItem> items = Collections.emptyList();
    private NavItemClickListener clickListener;

    public NavRecyclerAdapter(List<NavigationItem> items) {
        this.items = items;
    }

    @Override
    public NavItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_item,parent, false);
        NavItemViewHolder navItemViewHolder = new NavItemViewHolder(view);

        return navItemViewHolder;
    }

    @Override
    public void onBindViewHolder(NavItemViewHolder holder, int position) {
        NavigationItem navigationItem = items.get(position);
        holder.imgIcon.setImageResource(navigationItem.iconId);
        holder.txtTitle.setText(navigationItem.title);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setClickListener(NavItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class NavItemViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle;
        ImageView imgIcon;
        RelativeLayout navLine;

        public NavItemViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.navigation_item_title);
            imgIcon = (ImageView) itemView.findViewById(R.id.navigation_item_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.itemClicked(v, getAdapterPosition());
                }
            });

        }
    }

    public interface NavItemClickListener {
        public void itemClicked(View view, int position);
    }
}
