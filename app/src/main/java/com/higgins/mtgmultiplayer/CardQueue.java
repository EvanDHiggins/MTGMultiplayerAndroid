package com.higgins.mtgmultiplayer;


import android.content.res.AssetManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
public class CardQueue {

    private final String LOG_TAG =CardQueue.class.getSimpleName();

    AssetManager assetManager;
    ArrayList<String> cardNamesList;
    String folderPath;
    Bitmap cardImage;

    public CardQueue(Context c, String folderName) {

        folderPath = folderName;
        assetManager = c.getAssets();
        loadCardNamesList();
        shuffle();
    }

    public void loadCardNamesList() {
        try {
            cardNamesList = new ArrayList<String>(Arrays.asList(assetManager.list(folderPath)));
        } catch (IOException e) {
            Log.e(LOG_TAG, folderPath + " Card list not generated");
        }
    }

    public void shuffle() {
        Collections.shuffle(cardNamesList);
    }

    public int getQueueLength() {
        return cardNamesList.size();
    }

    public Bitmap getCardBitmap(int position) {
        Bitmap cardImage = null;
        BitmapGetterTask task = new BitmapGetterTask(assetManager, folderPath,
                cardNamesList.get(position), cardImage);
//        try {
//            InputStream inputS = assetManager.open(folderPath + File.separator +
//                    cardNamesList.get(position));
//            cardImage = BitmapFactory.decodeStream(inputS);
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "IOException");
//            e.printStackTrace();
//        } finally {
//            if (cardImage == null) {
//                Log.e(LOG_TAG, "Error, null cardImage");
//            }
//        }
        task.execute();
        return cardImage;
    }

    public void remove(int position) {
        cardNamesList.remove(position);
    }

    /** Strictly for debugging purposes
     *
     *  Prints out strings as verbose logs
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

    private class BitmapGetterTask extends AsyncTask<Void, Void, Bitmap> {

        AssetManager aManager;
        String folderName;
        String cardName;
        WeakReference<Bitmap> cardImage;

        public BitmapGetterTask(AssetManager assetManager, String folderPath, String cardName, Bitmap cardImage) {
            aManager = assetManager;
            if(aManager == null) {
                Log.v(LOG_TAG, "AssetManager is null");
            }
            folderName = folderPath;
            this.cardName = cardName;
            this.cardImage = new WeakReference<Bitmap>(cardImage);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap cardImage = null;
            try {
                InputStream inputS = assetManager.open(folderPath + File.separator +
                        cardName);
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

        @Override
        protected void onPostExecute(Bitmap cardImage) {
            Bitmap cardBitmap = this.cardImage.get();
            if(cardBitmap != null) {
                cardBitmap = cardImage;
            } else {
                Log.e(LOG_TAG, "cardBitmap is null");
            }
        }
    }
}
