package com.higgins.mtgmultiplayer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by EvanHiggins on 1/16/15.
 */
public class DeckWriter {

    private final String LOG_TAG = DeckWriter.class.getSimpleName();

    Context thisContext;


    public DeckWriter(Context c) {
        thisContext = c;
    }

    private void saveDeckDialog() {

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(thisContext);
        alertBuilder.setTitle("Save Deck");

        //Text field to type in name
        final EditText input = new EditText(thisContext);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        alertBuilder.setView(input);

        //Creates "ok" and "cancel" button which write the input string as the
        //deck name and cancel the dialog, respectively
        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(LOG_TAG, "OK was pressed");
                writeDeckToDevice(input.getText().toString());
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertBuilder.show();
    }

    private void writeDeckToDevice(String deckName) {

        FragmentManager fragmentManager = thisContext.getSupportFragmentManager();

        DeckFragment deckFragment = (DeckFragment)fragmentManager.findFragmentById(R.id.container);
        Log.v(LOG_TAG, deckName);
        List<String> deckList = deckFragment.getDeckAsList();
        try {
            OutputStreamWriter output = new OutputStreamWriter(
                    openFileOutput(deckName + ".txt", 0));
            for(String cardName : deckList) {
                output.write(cardName + "\n");
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
