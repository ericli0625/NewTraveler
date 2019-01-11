package com.example.eric.newtraveler.adapter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.eric.newtraveler.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SpotDetailAdapter extends BaseAdapter {

    public SpotDetailAdapter() {

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String string = null;
        try {
            JSONObject jsonobject = getJsonArray().getJSONObject(position);
            string = jsonobject.getString("name");
        } catch (JSONException e) {
            Log.e(MainActivity.TAG, "SpotDetailAdapter onBindViewHolder, JSONException");
        }
        viewHolder.mTextView.setText(string);
    }

}
