package com.example.eric.newtraveler.adapter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.eric.newtraveler.MainActivity;

import org.json.JSONException;

public class NormalListAdapter extends BaseAdapter {

    public NormalListAdapter() {

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull NormalListAdapter.ViewHolder viewHolder, int position) {
        String string = null;
        try {
            string = getJsonArray().getString(position);
        } catch (JSONException e) {
            Log.e(MainActivity.TAG, "NormalListAdapter onBindViewHolder, JSONException");
        }
        viewHolder.mTextView.setText(string);
    }

}
