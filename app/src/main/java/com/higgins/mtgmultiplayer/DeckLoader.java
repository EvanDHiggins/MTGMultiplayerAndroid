package com.higgins.mtgmultiplayer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Evan on 1/17/2015.
 */
public class DeckLoader {

    final private String LOG_TAG = DeckLoader.class.getSimpleName();

    private Context thisContext;
    private FragmentManager fragmentManager;

    public DeckLoader(Context c, FragmentManager f) {
        thisContext = c;
        fragmentManager = f;
    }

//    private void loadDeck() {
//        String deckName;
//        List<String> deckList;
//        //deckName = loadDeckDialog();
//        deckList = loadDeckFromFile();
//
//        DeckFragment fragment = (DeckFragment) fragmentManager.findFragmentById(R.id.container);
//        fragment.setDeckList(deckList);
//        ViewPager deckView = (ViewPager) findViewById(R.id.deck_view_pager);
//        PagerAdapter adapter = deckView.getAdapter();
//        adapter.notifyDataSetChanged();
//        deckView.setCurrentItem(0);
//    }

    public void loadDeckDialog() {
        Log.v(LOG_TAG, "LOG TEST");
        List<String> fileNames = Arrays.asList(thisContext.fileList());
        for(String file : fileNames) {
            if(file.startsWith(thisContext.getString(R.string.saved_deck_prefix))) {
                int index = fileNames.indexOf(file);
                fileNames.set(index, file.replaceFirst("deck_", ""));
            } else {
                fileNames.remove(file);
            }
            Log.v(LOG_TAG, file);
        }
        CharSequence[] fileCharSeqs = fileNames.toArray(new CharSequence[fileNames.size()]);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(thisContext);
        alertBuilder.setTitle("Load Deck");
        alertBuilder.setItems(fileCharSeqs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertBuilder.show();

    }

//    private List<String> loadDeckFromFile() {
//        FileInputStream fis;
//        final StringBuffer storedString = new StringBuffer();
//        List<String> newDeckList = null;
//        String deckString = null;
//
//        try {
//            FileInputStream openFile = openFileInput("NewDeck6.txt");
//            InputStreamReader reader = new InputStreamReader(openFile);
//            BufferedReader buffReader = new BufferedReader(reader);
//
//            String strLine;
//
//            while((strLine = buffReader.readLine()) != null) {
//                storedString.append(strLine + ",");
//            }
//            deckString = storedString.toString();
//            deckString.replace("\n", "");
//
//            buffReader.close();
//            openFile.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if(deckString != null) {
//            newDeckList = Arrays.asList(deckString.split("\\s*,\\s*"));
//        }
//        return newDeckList;
//    }

}
