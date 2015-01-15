package com.higgins.mtgmultiplayer.test;

import android.test.AndroidTestCase;

import com.higgins.mtgmultiplayer.CardQueue;
import com.higgins.mtgmultiplayer.R;

/**
 * Created by Evan on 1/14/2015.
 */
public class CardQueueTests extends AndroidTestCase {
    public void testRemoveMethod() {
        CardQueue cardDeck = new CardQueue(getContext(),
                getContext().getString(R.string.folder_archenemy));
        int deckLength = cardDeck.getQueueLength();
        cardDeck.remove(0);
        assertEquals(deckLength-1, cardDeck.getQueueLength());
    }

    /**
     * Tests that the cardQueue constructor loads all files
     * from the designated folder.
     */
    public void testCardLoad() {
        CardQueue cardDeck = new CardQueue(getContext(),
                getContext().getString(R.string.folder_test));
        assertEquals(cardDeck.getQueueLength(), 5);
    }
}