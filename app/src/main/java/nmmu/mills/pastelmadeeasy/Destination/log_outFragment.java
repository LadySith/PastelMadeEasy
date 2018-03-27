package nmmu.mills.pastelmadeeasy.Destination;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import nmmu.mills.pastelmadeeasy.BrandedLaunchScreen.BrandedUI;
import nmmu.mills.pastelmadeeasy.CustomClasses.GenericItem;
import nmmu.mills.pastelmadeeasy.Database.DatabaseHelper;
import nmmu.mills.pastelmadeeasy.Home;
import nmmu.mills.pastelmadeeasy.R;
import nmmu.mills.pastelmadeeasy.RecyclerAdapters.GenericRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by s214301427 on 2017/09/20.
 */
public class log_outFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<GenericItem> items = new ArrayList<>();

   public log_outFragment(){
       // Required empty public constructor
   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.log_out, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.log_out_recycler);
        adapter = new GenericRecyclerAdapter(items);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
