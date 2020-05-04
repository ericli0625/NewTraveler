package com.example.eric.newtraveler.adapters;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class NormalListAdapter extends BaseAdapter {

    public NormalListAdapter(ArrayList<String> arrayList, IBaseAdapterClickListener listener) {
        super(arrayList, listener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull NormalListAdapter.ViewHolder viewHolder, int position) {
        String content = getArrayList().get(position);
        viewHolder.getTextView().setText(content);
    }

    @Override
    public int getItemCount() {
        return getArrayList() == null ? 0 : getArrayList().size();
    }

}
