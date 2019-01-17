package com.example.eric.newtraveler.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eric.newtraveler.ui.MainActivity;
import com.example.eric.newtraveler.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.ViewHolder> {

    private ArrayList<String> mArrayList;
    private JSONArray mJsonArray;
    private IBaseAdapterClickListener mListener;

    public BaseAdapter(ArrayList<String> arrayList, IBaseAdapterClickListener listener) {
        this.mArrayList = arrayList;
        this.mListener = listener;
    }

    public BaseAdapter(String string, IBaseAdapterClickListener listener) {
        try {
            mJsonArray = new JSONArray(string);
        } catch (JSONException e) {
            Log.e(MainActivity.TAG, "BaseAdapter construct, JSONException");
        }
        this.mListener = listener;
    }

    protected ArrayList<String> getArrayList() {
        return mArrayList;
    }

    protected JSONArray getJsonArray() {
        return mJsonArray;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view mViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final TextView mTextView;
        private IBaseAdapterClickListener mListener;

        // each data item is just a string in this case
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.recycle_view_item);
            mTextView.setOnLongClickListener(this);
            mTextView.setOnClickListener(this);
        }

        public TextView getTextView() {
            return mTextView;
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(((TextView) v).getText().toString());
        }

        @Override
        public boolean onLongClick(View v) {
            return mListener.onItemLongClick(((TextView) v).getText().toString());
        }

        public void setListener(IBaseAdapterClickListener listener) {
            this.mListener = listener;
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public BaseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.city_item_view, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        holder.setListener(mListener);
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder viewHolder, int position) {
        String string = null;
        try {
            string = getJsonArray().getString(position);
        } catch (JSONException e) {
            Log.e(MainActivity.TAG, "BaseAdapter onBindViewHolder, JSONException");
        }
        viewHolder.getTextView().setText(string);
    }

    // Return the size of your mJsonArray (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mJsonArray == null ? 0 : mJsonArray.length();
    }
}
