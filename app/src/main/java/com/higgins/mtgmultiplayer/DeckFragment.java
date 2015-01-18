package com.higgins.mtgmultiplayer;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 1/9/2015.
 *
 * DeckFragment contains all elements involved in displaying plane/scheme
 * images and any menu items directly associated with them.
 */
public class DeckFragment extends Fragment{

    private final String LOG_TAG = DeckFragment.class.getSimpleName();

    private String folderName;
    private CardQueue deck;
    private View rootView;
    private ViewPager deckView;

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

    public CardQueue getDeck() {
        return deck;
    }

    public ViewPager getDeckView() {
        return deckView;
    }

    public List<String> getDeckAsList() {
        return deck.getCardNamesList();
    }

    public void setDeckList(ArrayList<String> newDeckList) {
        deck.setDeckList(newDeckList);
    }

    public void notifyDeckChanged() {
        deckView.getAdapter().notifyDataSetChanged();
    }

    public void resetCurrentItem() {
        deckView.setCurrentItem(0);
    }

    public void saveDeck() {
        DeckWriter writer = new DeckWriter(getActivity(), this, folderName);
        writer.saveDeck();
    }

    public void loadDeck() {
        DeckLoader loader = new DeckLoader(getActivity(), this, folderName);
        loader.loadDeckDialog();
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
        //((MainActivity) activity).onSectionAttached(1);
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
        super.onCreate(savedInstanceState);
        //It is my understanding that onCreate only happens one time,
        //but if these null checks aren't here then these objects
        //are created each time the fragment is loaded.
        if (deck == null) {
            deck = new CardQueue(getActivity(), folderName);
        }

        setHasOptionsMenu(true);
        setRetainInstance(true);
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

        deckView = (ViewPager)rootView.findViewById(R.id.deck_view_pager);

        //The adapter loads bitmaps, and thus should handle itself on
        //A seperate thread. The AsyncTask BitmapGetterTask handles this
        createAdapter();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("CURRENT_ITEM", deckView.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.deck_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id) {
            case R.id.action_remove:
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
                break;

            case R.id.action_shuffle:
                deck.shuffle();
                deckView.getAdapter().notifyDataSetChanged();
                deckView.setCurrentItem(0, true);
                break;

            case R.id.action_reset:
                deck.loadCardNamesList();
                deckView.getAdapter().notifyDataSetChanged();
                deckView.setCurrentItem(0, true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createAdapter() {

        DeckImageAdapter adapter = new DeckImageAdapter(getActivity(), deck);
        deckView.setAdapter(adapter);
    }
}
