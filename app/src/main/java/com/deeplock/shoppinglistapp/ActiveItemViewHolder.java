package com.deeplock.shoppinglistapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by nabinbhattarai on 8/5/16.
 */
public class ActiveItemViewHolder extends RecyclerView.ViewHolder {
    public ActiveItemViewHolder(View itemView, CheckBox itemStatus, TextView itemName, TextView quantity, ImageView itemAction) {
        super(itemView);
        this.itemStatus = itemStatus;
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemAction = itemAction;
    }

    CheckBox itemStatus;
    TextView itemName;
    TextView quantity;
    ImageView itemAction;

}
