package com.example.jamesnguyen.taskcycle.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "items")
public class ItemEntity  {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    @ColumnInfo(name="date_in_millisecond")
    private long date;

    @ColumnInfo(name="has_date")
    private boolean hasDate;

    @ColumnInfo(name="has_time")
    private boolean hasTime;

    public ItemEntity(String title, long date, boolean hasDate, boolean hasTime) {
        this.title = title;
        this.date = date;
        this.hasDate = hasDate;
        this.hasTime = hasTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHasDate() {
        return hasDate;
    }

    public void setHasDate(boolean hasDate) {
        this.hasDate = hasDate;
    }

    public boolean isHasTime() {
        return hasTime;
    }

    public void setHasTime(boolean hasTime) {
        this.hasTime = hasTime;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
