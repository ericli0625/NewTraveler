package com.example.eric.newtraveler.adapter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.eric.newtraveler.MainActivity;

import org.json.JSONException;

public class FavoriteListAdapter extends BaseAdapter {

    public FavoriteListAdapter() {

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String string = null;
        try {
            string = (String) getJsonArray().get(position);
        } catch (JSONException e) {
            Log.e(MainActivity.TAG, "FavoriteListAdapter onBindViewHolder, JSONException");
        }
        viewHolder.mTextView.setText(string);
    }

}
