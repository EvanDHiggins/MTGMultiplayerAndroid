package com.higgins.mtgmultiplayer;


import android.content.res.AssetManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Evan on 1/8/2015.
 *
 * In order to randomize and interact with Archenemy and
 * Planechase decks, CardQueue creates a randomized queue
 * of card names. Several get methods expose the image paths
 * and card names of each item in the queue.
 */
public class CardQueue{

    private final String LOG_TAG =CardQueue.class.getSimpleName();

    AssetManager assetManager;
    String[] cardNamesList;
    String folderPath;

    //Indicates index of current card
    int currentCardIndicator;

    public CardQueue(Context c, String folderName) {

        folderPath = folderName;
        currentCardIndicator = 0;
        assetManager = c.getAssets();

        try {
            cardNamesList = assetManager.list(folderName);
        } catch (IOException e) {
            Log.e(LOG_TAG, folderName + " Card list not generated");
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(Arrays.asList(cardNamesList));
    }

    /**
     * Returns cardpath.jpg of current card
     * @return
     */
    public String getCardPath() {
        return cardNamesList[currentCardIndicator];
    }

    public int getQueueLength() {
        return cardNamesList.length;
    }

    public Bitmap getCardBitmap(int position) {
        Bitmap cardImage = null;
        try {
            InputStream inputS = assetManager.open(folderPath + File.separator +
                    cardNamesList[position]);
            cardImage = BitmapFactory.decodeStream(inputS);
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException");
            e.printStackTrace();
        } finally {
            if (cardImage == null) {
                Log.e(LOG_TAG, "Error, null cardImage");
            }
        }
        return cardImage;
    }

    public void next() {
        currentCardIndicator++;
        if(currentCardIndicator >= cardNamesList.length) {
            currentCardIndicator = 0;
        }
    }

    /** Strictly for debugging purposes
     *
     *  Prints out strings as verbose logs
     *  Strictly for debuggin purposes
     *
     * @param array
     */
    private void printCardArray(String[] array) {
        if(array.length > 0) {
            for(String file : array) {
                Log.v(LOG_TAG, file);
            }
        }
    }
}
