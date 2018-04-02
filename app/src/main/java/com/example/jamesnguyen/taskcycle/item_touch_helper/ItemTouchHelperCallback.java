package com.example.jamesnguyen.taskcycle.item_touch_helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.dialogs_fragments.OpenLocationDialog;
import com.example.jamesnguyen.taskcycle.recycler_view.ReminderAdapter;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    Context context;
    ReminderAdapter mAdapter;
    ColorDrawable background;
    Drawable checkIcon;
    Drawable locationIcon;
    int checkIconWidth;
    int checkIconHeight;
    final int greenBackgroundColor = Color.parseColor("#6DA34D");
    final int whiteBackgroundColor = Color.parseColor("#FFFFFF");
    final int orangeBackgroundColor = Color.parseColor("#D35400");
    Paint clearPaint;

    int iconTop;
    int iconBottom;
    int iconRight;
    int iconLeft;
    int iconMargin;

    View itemView;
    int itemHeight;

    public ItemTouchHelperCallback(Context context, ReminderAdapter mAdapter) {
        this.mAdapter = mAdapter;
        this.context = context;
        background = new ColorDrawable();
        clearPaint = new Paint();
        clearPaint.setColor(whiteBackgroundColor);
        checkIcon = ContextCompat.getDrawable(context, R.drawable.ic_check_white_24dp);
        locationIcon = ContextCompat.getDrawable(context, R.drawable.ic_location_on_white_24dp);
        checkIconWidth = checkIcon.getIntrinsicWidth();
        checkIconHeight = checkIcon.getIntrinsicHeight();
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //int dragFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView
            , RecyclerView.ViewHolder viewHolder
            , RecyclerView.ViewHolder target) {
//        //mAdapter.onItemMove
//        Log.d("ItemTouchHelperCallback", "Moved");
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder
            , int direction) {
        //Log.d("ItemTouchHelperCallback", Integer.toString(direction));
        if(direction==ItemTouchHelper.RIGHT)
            mAdapter.onDeleteItem( viewHolder.getAdapterPosition());
        else if(direction==ItemTouchHelper.LEFT){
            //Start edit fragment and update that sole item if use update the item
            mAdapter.onUpdate( viewHolder.getAdapterPosition());
        }
    }



    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        //TODO implement
        itemView = viewHolder.itemView;
        itemHeight = itemView.getHeight();

        //boolean isCancelel = dX==0 && !isCurrentlyActive;
        //Log.d(getClass().getSimpleName(), "dX = " + Float.toString(dX));
        if(dX!=0){
            //Draw the button
            iconMargin = ( itemView.getHeight() - checkIconHeight ) /2 ;

            if(dX<0){
                background.setColor(orangeBackgroundColor);
                background.setBounds(itemView.getRight() + (int)dX,
                        itemView.getTop(),
                        itemView.getRight(),
                        itemView.getBottom());
                background.draw(c);

                iconTop = itemView.getTop() + iconMargin;
                iconRight = itemView.getRight() - iconMargin;
                iconLeft = iconRight - checkIconWidth;
                iconBottom = iconTop + checkIconHeight;

                locationIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                locationIcon.draw(c);

            } else if(dX>0) { // swipe right
                background.setColor(greenBackgroundColor);
                background.setBounds(itemView.getLeft(),
                        itemView.getTop(),
                        itemView.getLeft() + (int)dX,
                        itemView.getBottom());
                background.draw(c);

                //draw check icon
                //iconMargin = (itemHeight - checkIconHeight)/2;

                iconTop = itemView.getTop() + iconMargin;
                iconLeft = itemView.getLeft() + iconMargin;
                iconRight = iconLeft + checkIconWidth;
                iconBottom = iconTop +  checkIconHeight;

                //Log.d(getClass().getSimpleName(), "iconHeight = " + checkIconHeight + " iconWidth = "  + checkIconWidth + ", icon top = " +iconTop + ", iconLeft =" + iconLeft + ", iconRight =" + iconRight + ", iconBottom" + iconBottom );
                checkIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                checkIcon.draw(c);
            }

        } else {
            //Clear the button

        }
    }
}
