package nmmu.mills.pastelmadeeasy.Destination;


import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nmmu.mills.pastelmadeeasy.CustomClasses.GenericItem;
import nmmu.mills.pastelmadeeasy.Database.DatabaseHelper;
import nmmu.mills.pastelmadeeasy.R;
import nmmu.mills.pastelmadeeasy.RecyclerAdapters.GenericRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizzesFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<GenericItem> items = new ArrayList<>();

    DatabaseHelper myDB;

    public QuizzesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quizzes, container, false);

        myDB = new DatabaseHelper(getActivity());
        fillList();
        myDB.close();

        recyclerView = (RecyclerView) view.findViewById(R.id.quizzes_fragment_recycler);
        adapter = new GenericRecyclerAdapter(items);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }


    private void fillList() {
        Cursor res = myDB.getConcepts();
        while (res.moveToNext()){
            String actionStatement = "";
            String extra = "Highest Grade: ";
            boolean complete = false;
            int highest = myDB.getHighestGrade(res.getInt(0));

            if (highest == -1) {
                extra = "Unattempted";
                actionStatement = "attempt quiz...";
            }
            else {
                complete = true;
                extra = extra + highest + "%";
                if (highest == 100)
                    actionStatement = "review quiz...";
                else
                    actionStatement = "reattempt quiz...";

            }
            GenericItem genericItem = new GenericItem(res.getInt(0),res.getString(1), extra, actionStatement, complete);
            items.add(genericItem);
        }

    }

}
