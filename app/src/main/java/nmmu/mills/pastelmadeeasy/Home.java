package nmmu.mills.pastelmadeeasy;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;
import nmmu.mills.pastelmadeeasy.BrandedLaunchScreen.BrandedUI;
import nmmu.mills.pastelmadeeasy.Destination.*;
import nmmu.mills.pastelmadeeasy.Navigation.NavigationDrawerFragment;

public class Home extends AppCompatActivity implements NavigationDrawerFragment.NavToDestination {
    private TextView txtTitle;
    public static final String PREF_FILE_NAME = "PMEBasics";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_ALREADY_GREETED = "greeted";
    public static final String KEY_JUST_CREATED = "created";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setClickListener(this);

        txtTitle = (TextView) findViewById(R.id.page_title);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.home_fragment_container, new MainFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        if (!Boolean.valueOf(readFromPreferences(Home.this,KEY_ALREADY_GREETED,"false")) && !Boolean.valueOf(readFromPreferences(Home.this,KEY_JUST_CREATED,"true"))) {
            Toast.makeText(Home.this, "Welcome back " + readFromPreferences(Home.this, KEY_USERNAME, ""), Toast.LENGTH_LONG).show();
            saveToPreferences(Home.this,KEY_ALREADY_GREETED,"true");
        } else {
            saveToPreferences(Home.this,KEY_JUST_CREATED,"false");
            saveToPreferences(Home.this,KEY_ALREADY_GREETED,"true");
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 1){
            fragmentManager.popBackStackImmediate();
            txtTitle.setText("Concepts");
        }
        else {
            saveToPreferences(Home.this,KEY_ALREADY_GREETED,"false");
            super.onBackPressed();
        }
    }

    @Override
    public void Change(String title) {
        txtTitle.setText(title);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch(title){
            case "Concepts": fragmentTransaction.replace(R.id.home_fragment_container, new MainFragment());
                if (fragmentManager.getBackStackEntryCount() > 1)
                    fragmentManager.popBackStackImmediate();
                fragmentTransaction.commit();
                break;
            case "Tutorials": fragmentTransaction.replace(R.id.home_fragment_container, new TutorialsFragment());
                if (fragmentManager.getBackStackEntryCount() > 1)
                    fragmentManager.popBackStackImmediate();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case "Steps": fragmentTransaction.replace(R.id.home_fragment_container, new StepsFragment());
                if (fragmentManager.getBackStackEntryCount() > 1)
                    fragmentManager.popBackStackImmediate();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case "Videos": fragmentTransaction.replace(R.id.home_fragment_container, new VideosFragment());
                if (fragmentManager.getBackStackEntryCount() > 1)
                    fragmentManager.popBackStackImmediate();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case "Quizzes": fragmentTransaction.replace(R.id.home_fragment_container, new QuizzesFragment());
                if (fragmentManager.getBackStackEntryCount() > 1)
                    fragmentManager.popBackStackImmediate();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case "Notes": fragmentTransaction.replace(R.id.home_fragment_container, new NotesFragment());
                if (fragmentManager.getBackStackEntryCount() > 1)
                    fragmentManager.popBackStackImmediate();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

        }

        saveToPreferences(this,"Fragment",title);
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String prefName, String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(prefName, defaultValue);
    }

}
