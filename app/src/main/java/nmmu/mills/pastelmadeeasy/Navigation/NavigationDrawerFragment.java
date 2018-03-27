package nmmu.mills.pastelmadeeasy.Navigation;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import nmmu.mills.pastelmadeeasy.BrandedLaunchScreen.BrandedUI;
import nmmu.mills.pastelmadeeasy.Login.LoginActivity;
import nmmu.mills.pastelmadeeasy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements NavRecyclerAdapter.NavItemClickListener{

    public static final String PREF_FILE_NAME = "PMEBasics";
    public static final String KEY_DRAWER_LEARNT = "PMEuserLearntDrawer";
    public static final String KEY_USERNAME = "username";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    private View containerView;

    private NavRecyclerAdapter navRecyclerAdapter;
    private RecyclerView recyclerView;

    private NavToDestination navToDestination;

    private TextView txtUsername;
    private String username;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_DRAWER_LEARNT, "false"));
        if (savedInstanceState != null)
            mFromSavedInstanceState = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.navigation_drawer_recycler);

        txtUsername = (TextView) view.findViewById(R.id.username);

        username = readFromPreferences(getActivity(),KEY_USERNAME,"");

        txtUsername.setText(username.trim());

        navRecyclerAdapter = new NavRecyclerAdapter(getItems());
        navRecyclerAdapter.setClickListener(this);
        recyclerView.setAdapter(navRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    private List<NavigationItem> getItems() {
        List<NavigationItem> items = new ArrayList<>();

        int[] icons = new int[7];
        String[] titles = new String[7];

        titles[0] = "Concepts";
        titles[1] = "Tutorials";
        titles[2] = "Steps";
        titles[3] = "Videos";
        titles[4] = "Quizzes";
        titles[5] = "Notes";
        titles[6] = "Log Out";

        icons[0] = R.drawable.ic_home;
        icons[1] = R.drawable.ic_tutorials;
        icons[2] = R.drawable.ic_steps;
        icons[3] = R.drawable.ic_videos;
        icons[4] = R.drawable.ic_quizzes;
        icons[5] = R.drawable.ic_notes;
        icons[6] = R.drawable.ic_logout;

        for (int i = 0; i < 7; i++){
            NavigationItem navigationItem = new NavigationItem(titles[i], icons[i]);
            items.add(navigationItem);
        }

        return items;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);

        mDrawerLayout = drawerLayout;

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer){
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_DRAWER_LEARNT, mUserLearnedDrawer+"");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };

//        if (!mUserLearnedDrawer && !mFromSavedInstanceState)
//            mDrawerLayout.openDrawer(containerView);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String prefName, String prefValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(prefName, prefValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String prefName, String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(prefName, defaultValue);
    }

    public void setClickListener(NavToDestination clickListener) {
        this.navToDestination = clickListener;
    }

    @Override
    public void itemClicked(View view, int position) {
        mDrawerLayout.closeDrawer(containerView);
        switch (position){
            case 0: navToDestination.Change("Concepts");
                break;
            case 1: navToDestination.Change("Tutorials");
                break;
            case 2: navToDestination.Change("Steps");
                break;
            case 3: navToDestination.Change("Videos");
                break;
            case 4: navToDestination.Change("Quizzes");
                break;
            case 5: navToDestination.Change("Notes");
                break;
            case 6:   Intent intent = new Intent(NavigationDrawerFragment.this.getActivity(), LoginActivity.class);
                startActivity(intent);

                break;
        }
    }

    public interface NavToDestination {
        public void Change(String title);
    }
}
