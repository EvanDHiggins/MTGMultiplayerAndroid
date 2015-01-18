package com.higgins.mtgmultiplayer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by Evan on 1/17/2015.
 */
public class DeckLoader {

    final private String LOG_TAG = DeckLoader.class.getSimpleName();

    private Context thisContext;
    private ArrayList<String> fileNames;
    private DeckFragment deckFragment;
    private String deckTag;

    public DeckLoader(Context c, DeckFragment f, String folderName) {
        thisContext = c;
        deckFragment = f;
        this.deckTag = (new StringBuilder(folderName).append("_").toString());
        Log.v(LOG_TAG, "deckTag == " + deckTag);
    }

    public void loadDeckDialog() {
        fileNames = new ArrayList<>(Arrays.asList(thisContext.fileList()));

        //Removes any files that don't have the proper prefix, and removes the prefix
        //From those which have it.
        Log.v(LOG_TAG, Integer.toString(fileNames.size()));
        Iterator<String> iter = fileNames.iterator();
        while(iter.hasNext()) {
            String file = iter.next();
            if(file.startsWith(deckTag)) {
                int index = fileNames.indexOf(file);
                fileNames.set(index, file.replaceFirst(deckTag, ""));
            } else {
                iter.remove();
            }
        }
        //for(String file : fileNames) {
//        for(int i = 0; i < fileNames.size(); i++) {
//            file = fileNames.get(i);
//
//        }
        Collections.sort(fileNames);
        //Conversion to charSequence is due to alertBuilder.setItems parameters
        CharSequence[] fileCharSeqs = fileNames.toArray(new CharSequence[fileNames.size()]);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(thisContext);
        alertBuilder.setTitle("Load Deck");

        //Creates list dialog of
        alertBuilder.setItems(fileCharSeqs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadDeck(which);
            }
        });
        alertBuilder.show();

    }

    private void loadDeck(int position) {
        String deckName = fileNames.get(position);
        ArrayList<String> newDeck = loadDeckFromFile(deckName);

        deckFragment.setDeckList(newDeck);
        deckFragment.notifyDeckChanged();

        deckFragment.resetCurrentItem();

    }

    private ArrayList<String> loadDeckFromFile(String deckName) {
        final StringBuffer storedString = new StringBuffer();
        ArrayList<String> newDeckList = null;
        String deckString = null;

        try {
            FileInputStream openFile = thisContext.openFileInput(deckTag + deckName);

            InputStreamReader reader = new InputStreamReader(openFile);
            BufferedReader buffReader = new BufferedReader(reader);

            String strLine;

            while((strLine = buffReader.readLine()) != null) {
                storedString.append(strLine + ",");
            }
            deckString = storedString.toString();
            deckString.replace(thisContext.getString(R.string.saved_deck_delimiter), "");

            buffReader.close();
            openFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(deckString != null) {
            newDeckList = new ArrayList<>(Arrays.asList(deckString.split("\\s*,\\s*")));
            Log.e(LOG_TAG, "DeckString isn't null");
        } else {
            Log.e(LOG_TAG, "DeckString is null");
        }
        return newDeckList;
    }

}
