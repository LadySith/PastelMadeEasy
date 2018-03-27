package nmmu.mills.pastelmadeeasy.Destination;



import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import nmmu.mills.pastelmadeeasy.CustomClasses.MainItem;
import nmmu.mills.pastelmadeeasy.Database.DatabaseHelper;
import nmmu.mills.pastelmadeeasy.R;
import nmmu.mills.pastelmadeeasy.RecyclerAdapters.MainRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class MainFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MainItem> items = new ArrayList<>();

    DatabaseHelper myDB;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        myDB = new DatabaseHelper(getActivity());
        fillList();
        myDB.close();

        recyclerView = (RecyclerView) view.findViewById(R.id.main_fragment_recycler);
        adapter = new MainRecyclerAdapter(items, getActivity());
//        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fillList();
    }

    private void fillList() {
        items.clear();
        Cursor res = myDB.getConcepts();
        while (res.moveToNext()){
            MainItem mainItem = new MainItem(res.getInt(0) ,res.getString(1), false, false, false, false);
            items.add(mainItem);
        }
    }

}
