package com.deeplock.shoppinglistapp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nabinbhattarai on 8/5/16.
 */
public class ShoppingItem extends RealmObject
{
    private String name;
    private String quantity;
    private boolean completed;

    @PrimaryKey
    private String id; //To uniquely identify the shopping items

    private long timeStamp; //For recently added

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
