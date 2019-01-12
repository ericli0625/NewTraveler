package com.example.eric.newtraveler.mvp;

public interface IBaseAdapterClickListener {
    void onItemClick(String string, int position);
    boolean onItemLongClick(String string, int position);
}
