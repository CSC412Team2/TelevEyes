package edu.ecu.csc412.televeyes;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.ecu.csc412.televeyes.adapter.SuggestionAdapter;
import edu.ecu.csc412.televeyes.json.ShowContainer;
import edu.ecu.csc412.televeyes.model.Show;
import edu.ecu.csc412.televeyes.tv.TVMaze;
import edu.ecu.csc412.televeyes.view.SlidingTabLayout;

//notifications
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;


public class MainActivity extends AppCompatActivity implements DiscoverFragment.OnListFragmentInteractionListener {

    //Notification1 = notification for new episode about to air
    NotificationCompat.Builder notification1;
    private static final int uniqueID1 = 38294;
    //Notification2 = notification for changes in the show's platform ie. time, day, channel, etc.
    NotificationCompat.Builder notification2;
    private static final int uniqueID2 = 53926;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    String[] mCategories;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;

    private DiscoverFragment discoverFragment;
    private ShowFragment showFragment;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private List<ShowContainer> shows;
    private Gson gson;
    private RequestQueue requestQueue;
    private List<Show> searchShows;
    SearchView.SearchAutoComplete searchSrcTextView;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mCategories = getResources().getStringArray(R.array.categories);
        mDrawerList = (ListView) findViewById(R.id.filter_drawer);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name,R.string.app_name);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, mCategories);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mDrawerList.setSelector(R.color.colorAccent);
        mDrawerList.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerList.setSelection(0);
                adapter.notifyDataSetChanged();
            }
        }, 300);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawerLayout.closeDrawer(mDrawerList);
                mDrawerList.setSelection(position);
                displayDataItems(mCategories[position]);
            }
        });

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(mViewPager);

        requestQueue = VolleySingleton.getInstance().getRequestQueue();
        gson = new Gson();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //NOTIFICATION1 CODE
        notification1 = new NotificationCompat.Builder(this);
        notification1.setAutoCancel(true);

        //new ep airing notification code for the system
        //build the notification
        notification1.setSmallIcon(R.mipmap.ic_launcher);
        notification1.setTicker("Something is airing soon");
        notification1.setWhen(System.currentTimeMillis());
        notification1.setContentTitle("A New Episode is About to Air");
        notification1.setContentText("Click to go to app and view");

        //NOTIFICATION2 CODE
        notification2 = new NotificationCompat.Builder(this);
        notification2.setAutoCancel(true);

        //changes to show format notification code for the system
        //build the notification
        notification2.setSmallIcon(R.mipmap.ic_launcher);
        notification2.setTicker("Something has changed");
        notification2.setWhen(System.currentTimeMillis());
        notification2.setContentTitle("Your Show's Format Has Changed");
        notification2.setContentText("Click to go to app and view");


        //creates an intent to open the app after clicking the notification
        Intent nIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification1.setContentIntent(pendingIntent);
        notification2.setContentIntent(pendingIntent);

        //Builds notification1 and issues it ********
        //******** needs to reach a value of a certain time before show then notify user
        NotificationManager nm1 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm1.notify(uniqueID1, notification1.build());


        //Builds notification2 and issues it ********
        //********* needs to be issued to user when any changes to shows' platforms are made ie. time, day, cancellation, etc.
        NotificationManager nm2 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm2.notify(uniqueID2, notification2.build());


    }


    private void displayDataItems(final String category) {
        if(discoverFragment == null){
            discoverFragment = DiscoverFragment.newInstance(1);
        }
        discoverFragment.refreshShows(category);

        if(showFragment == null){
            showFragment = ShowFragment.newInstance(getApplicationContext(), 1);
        }
        showFragment.refreshShows(category);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchSrcTextView = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchSrcTextView.setThreshold(0);

        searchSrcTextView.setDropDownBackgroundResource(android.R.drawable.screen_background_light);

        searchSrcTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SynActivity.ShowSynop(getApplicationContext(), searchShows.get(position).getId());
            }
        });

        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchActivity.class)));
        final View dropDownAnchor = searchView.findViewById(searchSrcTextView.getDropDownAnchor());

        dropDownAnchor.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                searchSrcTextView.setDropDownWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                searchSrcTextView.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                TVMaze.getInstance().showSearch(query, 10, new TVMaze.OnShowSearchListener() {
                    @Override
                    public void onResults(List<Show> shows) {
                        List<String> items = new ArrayList<String>();

                        for (int i = 0; i < shows.size(); i++) {
                            items.add(shows.get(i).getName());
                        }

                        searchShows = shows;
                        //Show names are shown here
                        searchSrcTextView.setAdapter(new SuggestionAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, items));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                break;
            case R.id.action_filter:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Show item) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return the corresponding fragment depending on position.
            switch (position) {
                case 0:
                    if(discoverFragment == null){
                        discoverFragment = DiscoverFragment.newInstance(1);
                    }
                    return discoverFragment;
                case 1:
                    if(showFragment == null){
                        showFragment = ShowFragment.newInstance(getApplicationContext(), 1);
                    }
                    return showFragment;
                case 2:
                    return NotiFragment.newInstance(1);

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Discover";
                case 1:
                    return "TV Shows";
                case 2:
                    return "Notifications";
            }
            return null;
        }
    }
}
