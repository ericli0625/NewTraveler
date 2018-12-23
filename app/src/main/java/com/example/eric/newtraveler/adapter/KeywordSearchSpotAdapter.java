package com.example.eric.newtraveler.adapter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.eric.newtraveler.MainActivity;

import org.json.JSONException;

public class KeywordSearchSpotAdapter extends BaseAdapter {

    public KeywordSearchSpotAdapter(String string) {
        super(string);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String string = null;
        try {
            string = mJsonArray.getString(position);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(MainActivity.TAG, "BaseAdapter onBindViewHolder, JSONException");
        }
        viewHolder.mTextView.setText(string);
    }

}
