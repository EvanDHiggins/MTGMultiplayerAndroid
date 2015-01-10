package com.higgins.mtgmultiplayer;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class DeckImageAdapter extends PagerAdapter {

    private final String LOG_TAG = DeckImageAdapter.class.getSimpleName();

    Context context;
    CardQueue deck;

    /**
     * Instances of DeckImageAdapter should be instantiated
     * off of the UI thread. They load lots of bitmaps and thus
     * will cause crashes when not in their own thread.
     *
     * @param context
     * @param cardQueue: The "deck" that the card images should be
     *                 loaded from.
     */
    public DeckImageAdapter(Context context, CardQueue cardQueue){
        deck = cardQueue;
        this.context=context;
        if(this.context == null) {
            Log.v(LOG_TAG, "null context");
        }
    }

    @Override
    public int getCount() {
        return deck.getQueueLength();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //The ViewPager consists of several ImageViews, although it can be
        //any type of view.
        ImageView imageView = new ImageView(context);

        //Sets the padding around each side of the ImageView. 0 padding results in
        //images to be touching exactly at the edge.
        int padding = context.getResources().getDimensionPixelSize(R.dimen.deck_view_padding);
        imageView.setPadding(padding, padding, padding, padding);

        imageView.setImageBitmap(deck.getCardBitmap(position));
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}