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
import java.util.List;

/**
 * Created by Evan on 1/17/2015.
 */
public class DeckLoader {

    final private String LOG_TAG = DeckLoader.class.getSimpleName();

    private Context thisContext;
    private List<String> fileNames;
    private DeckFragment deckFragment;

    public DeckLoader(Context c, DeckFragment f) {
        thisContext = c;
        deckFragment = f;
    }

    public void loadDeckDialog() {
        fileNames = Arrays.asList(thisContext.fileList());

        //Removes any files that don't have the proper prefix, and removes the prefix
        //From those which have it.
        for(String file : fileNames) {
            if(file.startsWith(thisContext.getString(R.string.saved_deck_prefix))) {
                int index = fileNames.indexOf(file);
                fileNames.set(index, file.replaceFirst("deck_", ""));
            } else {
                fileNames.remove(file);
            }
            Log.v(LOG_TAG, file);
        }
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
            FileInputStream openFile = thisContext.openFileInput(
                    thisContext.getString(R.string.saved_deck_prefix) + deckName);
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
