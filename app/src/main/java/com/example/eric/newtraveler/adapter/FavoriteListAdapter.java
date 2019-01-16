package com.example.eric.newtraveler.adapter;

import android.support.annotation.NonNull;

import com.example.eric.newtraveler.mvp.IBaseAdapterClickListener;

import java.util.ArrayList;

public class FavoriteListAdapter extends BaseAdapter {

    public FavoriteListAdapter(ArrayList<String> arrayList, IBaseAdapterClickListener listener) {
        super(arrayList, listener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String content = getArrayList().get(position);
        viewHolder.getTextView().setText(content);
    }

    @Override
    public int getItemCount() {
        return getArrayList() == null ? 0 : getArrayList().size();
    }

}
