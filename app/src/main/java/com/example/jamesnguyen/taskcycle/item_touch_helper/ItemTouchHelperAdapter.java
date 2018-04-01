package com.example.jamesnguyen.taskcycle.item_touch_helper;

public interface ItemTouchHelperAdapter {
    void onItemMove(int from, int to);
    void onItemRemoved(int position);
}
