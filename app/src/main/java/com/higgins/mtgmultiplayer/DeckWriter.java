package com.higgins.mtgmultiplayer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by EvanHiggins on 1/16/15.
 *
 * Writes a planechase/archenemy decklist to a .txt file on internal storage.
 *
 * Methods:
 *
 * saveDeck(): Public method to abstract what the writer is doing. Just calls saveDeckDialog()
 * public
 * void
 *
 * saveDeckDialog(): Creates and shows a dialog prompting the user for a file name.
 * private           When the positive button is pressed writeDeckToDevice() is called.
 * void
 *
 * writeDeckToDevice(String deckName): Writes a .txt file to the device with each card name
 * private                             taking up a seperate line.
 * void
 */
public class DeckWriter {

    private final String LOG_TAG = DeckWriter.class.getSimpleName();

    private Context thisContext;
    private DeckFragment deckFragment;
    private String deckTag;


    public DeckWriter(Context c, DeckFragment deckFragment, String folderName) {
        thisContext = c;
        this.deckFragment = deckFragment;
        deckTag = folderName + "_";

        //The Activities fragment manager is needed to get the current
        //deck fragment so that the deck can be accessed and saved.
    }

    /**
     * saveDeck wraps all the necessary methods to save a deck into a single
     * public call.
     */
    public void saveDeck() {
        saveDeckDialog();
    }

    private void saveDeckDialog() {

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(thisContext);
        alertBuilder.setTitle("Save Deck");

        final EditText input = new EditText(thisContext);

        //Sets the text field type to normal text. i.e. not password field, multi-choice box, etc.
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        alertBuilder.setView(input);

        //Creates "ok" and "cancel" button which write the input string as the
        //deck name and cancel the dialog, respectively
        alertBuilder.setPositiveButton(thisContext.getString(R.string.alert_positive),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                writeDeckToDevice(input.getText().toString());
            }
        });
        alertBuilder.setNegativeButton(thisContext.getString(R.string.alert_negative),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertBuilder.show();
    }

    private void writeDeckToDevice(String deckName) {

        List<String> deckList = deckFragment.getDeckAsList();

        try {
            //Prepending folder name to the file name allows the loader to distinguish between
            //each type of deck.
            OutputStreamWriter output = new OutputStreamWriter(
                    thisContext.openFileOutput(deckTag + deckName + ".txt", 0));
            for(String cardName : deckList) {
                output.write(cardName + thisContext.getString(R.string.saved_deck_delimiter));
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
