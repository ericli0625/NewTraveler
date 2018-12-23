package com.example.eric.newtraveler.adapter;

import android.support.annotation.NonNull;
import android.util.Log;


import com.example.eric.newtraveler.MainActivity;

import org.json.JSONException;

public class CityListAdapter extends BaseAdapter {

    public CityListAdapter(String string) {
        super(string);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull CityListAdapter.ViewHolder viewHolder, int position) {
        String string = null;
        try {
            string = getJsonArray().getString(position);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(MainActivity.TAG, "BaseAdapter onBindViewHolder, JSONException");
        }
        viewHolder.mTextView.setText(string);
    }

}
