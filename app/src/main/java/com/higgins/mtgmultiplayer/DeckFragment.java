package com.higgins.mtgmultiplayer;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Created by Evan on 1/9/2015.
 *
 * DeckFragment contains all elements involved in displaying plane/scheme
 * images and any menu items directly associated with them.
 */
public class DeckFragment extends Fragment{

    private final String LOG_TAG = DeckFragment.class.getSimpleName();

    String folderName;
    CardQueue deck;
    View rootView;
    ViewPager deckView;

    /**
     * Static constructor that assigns a folder name and returns a new
     * DeckFragment
     *
     * @param folderName: This is the assets subdirectory where images
     *                  are loaded from. "" would result in loading all images
     *                  in the assets directory, while "archenemy" results in
     *                  all images from the assets/archenemy directory, and
     *                  so on.
     * @return
     */
    public static DeckFragment newInstance(String folderName) {
        DeckFragment fragment = new DeckFragment();
        fragment.setFolderName(folderName);
        return fragment;
    }

    public DeckFragment() {
        //Required empty constructor
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    /**
     * OnAttach is the first action that happens when a fragment is created.
     * It attaches the action to the current activity. If getActivity() is called
     * before onAttach has completed it will return null.
     * This is only called on creation of the fragment.
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }

    /**
     * onCreate is the second method called. It's called after onAttach and
     * initializes(not sure if this is the appropriate term) the fragment and
     * any objects within.
     * This is only called on creation of the fragment.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //TODO: Figure out why these statements are needed
        //It is my understanding that onCreate only happens one time,
        //but if these null checks aren't here then these objects
        //are created each time the fragment is loaded.
        if (deck == null) {
            deck = new CardQueue(getActivity(), folderName);
        }

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * onCreateView is called after onCreate and is where the visual components
     * of the fragment are created and initialized. This is also the first method
     * called when a fragment is being resumed.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Renders the main view as designated in the fragment_deck layout
        rootView = inflater.inflate(R.layout.fragment_deck, container, false);

        //The adapter loads bitmaps, and thus should handle itself on
        //A seperate thread. The AsyncTask BitmapGetterTask handles this
        createAdapter();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.deck_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_remove) {
            //Finds the current position off the viewPager to adjust it once the
            //current item is deleted
            int currentItem = deckView.getCurrentItem();
            deck.remove(currentItem);

            //This ensures that the viewPager "knows" when it needs to update itself
            deckView.getAdapter().notifyDataSetChanged();

            //The view pager doesn't move when an item is deleted,
            //but rather all items beyond that are moved to the left
            //one space. If the viewPager is at the end, though, it has
            // to move back one space
            if(currentItem <= deck.getQueueLength()) {
                deckView.setCurrentItem(currentItem, false);
            } else {
                deckView.setCurrentItem(currentItem - 1, false);
            }


        } else if (id == R.id.action_shuffle) {
            deck.shuffle();
            deckView.getAdapter().notifyDataSetChanged();
            deckView.setCurrentItem(0, true);
        }
        return super.onOptionsItemSelected(item);
    }

    private void createAdapter() {
        deckView = (ViewPager)rootView.findViewById(R.id.deck_view_pager);
        BitmapGetterTask bitGTask = new BitmapGetterTask(deckView);
        bitGTask.execute(getActivity(), deck);
    }

    /**
     * Since the plane/scheme images are loaded from bitmaps, they should
     * be handled on a seperate thread. This AsyncTask creates a DeckImageAdapter
     * for the ViewPager.
     *
     * Object[0] Context: Context containing desired view
     * Object[1] CardQueue: CardQueue used to fetch card images
     */
    private class BitmapGetterTask extends AsyncTask<Object, Void, DeckImageAdapter> {

        private final String LOG_TAG = BitmapGetterTask.class.getSimpleName();

        private WeakReference<ViewPager> bitmapViewReference;
        private CardQueue deck;
        private Context mContext;

        public BitmapGetterTask(ViewPager viewPager) {
            //This view has to be a weak reference because it can be garbage collected
            //before the process has reached onPostExecute. If the user leaves the app
            //before doInBackground has completed the process is up for garbage collection.
            bitmapViewReference = new WeakReference<ViewPager>(viewPager);
        }

        @Override
        protected DeckImageAdapter doInBackground(Object... params) {
            mContext = (Context)params[0];
            deck = (CardQueue)params[1];
            DeckImageAdapter adapter = new DeckImageAdapter(mContext, deck);
            return adapter;
        }

        @Override
        protected void onPostExecute(DeckImageAdapter deckImageAdapter) {
            if(bitmapViewReference != null) {
                //Dereferences the bitmapViewReference to return the original viewPager object
                ViewPager viewPager = bitmapViewReference.get();

                if(viewPager != null) {
                    viewPager.setAdapter(deckImageAdapter);
                }
            }
            super.onPostExecute(deckImageAdapter);
        }
    }
}
