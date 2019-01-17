package com.example.eric.newtraveler.adapters;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class SpotDetailAdapter extends BaseAdapter {

    public SpotDetailAdapter(ArrayList<String> arrayList, IBaseAdapterClickListener listener) {
        super(arrayList, listener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String spotName = getArrayList().get(position);
        viewHolder.getTextView().setText(spotName);
    }

    @Override
    public int getItemCount() {
        return getArrayList() == null ? 0 : getArrayList().size();
    }

}
