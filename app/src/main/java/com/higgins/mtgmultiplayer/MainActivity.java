package com.higgins.mtgmultiplayer;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private DeckFragment archenemyFragment;
    private DeckFragment planechaseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        //mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {


        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        //TODO: Figure out how to move DeckFragment initialization to onCreate()
        //These null checks ensure that the DeckFragments aren't recreated
        //replacing current position and card order. This has actually been
        //a source of issue since Google Developer resources indicate that this should
        //be done in the onCreate method, but since that method loads the
        //navigation drawer it crashes the application immediately as it runs.
        //I will continue looking for a better fix for this, but as it stands
        //this works well enough.
        if(archenemyFragment == null) {
            archenemyFragment = DeckFragment.newInstance(getString(R.string.folder_archenemy));
        }
        if(planechaseFragment == null) {
            planechaseFragment = DeckFragment.newInstance(getString(R.string.folder_planechase));
        }

        if(position == 0) {
            //Sets the action bar title to the name of the current fragment
            mTitle = getString(R.string.title_archenemy);

            //Replaces the current fragment with the archenemyFragment
            fragmentManager.beginTransaction()
                    .replace(R.id.container, archenemyFragment)
                    .commit();
        } else if (position == 1) {
            mTitle = getString(R.string.title_planechase);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, planechaseFragment)
                    .commit();
        }
    }

    public void onSectionAttached(int number) {
        Log.v(LOG_TAG, Integer.toString(number));
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
