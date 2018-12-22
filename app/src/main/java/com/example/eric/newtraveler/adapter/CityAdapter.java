package com.example.eric.newtraveler.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eric.newtraveler.MainActivity;
import com.example.eric.newtraveler.R;

import org.json.JSONArray;
import org.json.JSONException;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private JSONArray mJsonArray;

    public CityAdapter(String string) {
        try {
            mJsonArray = new JSONArray(string);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(MainActivity.TAG, "CityAdapter constructer, JSONException");
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        // each data item is just a string in this case
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.cityItem);
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.city_item_view, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull CityAdapter.ViewHolder viewHolder, int position) {
        String string = null;
        try {
            Log.v(MainActivity.TAG, "CityAdapter onBindViewHolder position = " + position);
            string = mJsonArray.getString(position);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.v(MainActivity.TAG, "CityAdapter onBindViewHolder, JSONException");
        }
        viewHolder.mTextView.setText(string);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mJsonArray == null ? 0 : mJsonArray.length();
    }
}
