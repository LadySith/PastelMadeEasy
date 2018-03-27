package nmmu.mills.pastelmadeeasy.StepsView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nmmu.mills.pastelmadeeasy.CustomClasses.Step;
import nmmu.mills.pastelmadeeasy.Database.DatabaseHelper;
import nmmu.mills.pastelmadeeasy.R;
import nmmu.mills.pastelmadeeasy.RecyclerAdapters.StepsRecyclerAdapter;

public class Steps extends AppCompatActivity {
    TextView txtConceptName;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Step> steps;

    DatabaseHelper myDB;

    int conceptID;
    private  boolean updated = false;
    private  boolean alreadyComplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtConceptName = (TextView) findViewById(R.id.concept_name);

        Bundle b = getIntent().getExtras();
        String conceptName = b.getString("conceptName");
        conceptID = b.getInt("conceptID");

        txtConceptName.setText(conceptName);

        myDB = new DatabaseHelper(Steps.this);
        steps = getSteps();
        alreadyComplete = myDB.getComplete(conceptID,"conceptStepsCompleted");
        myDB.close();

        recyclerView = (RecyclerView) findViewById(R.id.steps_recycler);
        adapter = new StepsRecyclerAdapter(steps);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isLastItemDisplaying(recyclerView) && !alreadyComplete && !updated)
                    updateDB();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLastItemDisplaying(recyclerView) && !alreadyComplete && !updated)
                    updateDB();
            }
        });
    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    private List<Step> getSteps() {
        List<Step> stepList = new ArrayList<>();

        Cursor res = myDB.getSteps(conceptID);

        while (res.moveToNext()) {
            Step step = new Step();
            step.setConceptId(conceptID);
            step.setNumber(res.getInt(0));
            step.setText(res.getString(1));
            stepList.add(step);
        }

        return stepList;
    }

    private void updateDB(){
        myDB = new DatabaseHelper(Steps.this);
        myDB.updateComplete(conceptID,"conceptStepsCompleted");
        updated = true;

        myDB.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_steps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
