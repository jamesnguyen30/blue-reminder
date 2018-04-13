package com.myapp.jamesnguyen.bluereminder.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

@Entity(tableName = "items")
public class ItemEntity implements Parcelable {

    public static final int PRIORITY_DEFAULT =0;
    public static final int PRIORITY_IMPORTANT = 1;
    public static final int PRIORITY_URGENT = 2;
    public static final int PRIORITY_URGENT_AND_IMPORTANT = 3;

    public static final int REMINDER_ITEM_TYPE = 0;
    public static final int HEADER_TYPE = 1;


    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    @ColumnInfo(name="date_in_millisecond")
    private long date;

    private int priority;

    @ColumnInfo(name="has_date")
    private boolean hasDate;

    @ColumnInfo(name="has_time")
    private boolean hasTime;

    @ColumnInfo(name="place_name")
    private String placeName;

    @ColumnInfo(name="has_alarm")
    private boolean hasAlarm;

    @ColumnInfo(name="readable_access")
    private String readableAddress;


    @Ignore
    int viewType; // for reminder adapter


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<ItemEntity>(){
        @Override
        public ItemEntity createFromParcel(Parcel source) {
            return new ItemEntity(source);
        }

        @Override
        public ItemEntity[] newArray(int size) {
            return new ItemEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(priority);
        dest.writeString(placeName);
        dest.writeString(readableAddress);
        dest.writeLong(date);
        dest.writeByte((byte)(hasDate?1:0));
        dest.writeByte((byte)(hasTime?1:0));
        dest.writeByte((byte)(hasAlarm?1:0));
    }

    public ItemEntity(Parcel in){
        this.id = in.readInt();
        this.title = in.readString();
        this.priority = in.readInt();
        this.date = in.readLong();
        this.hasDate = in.readByte()!=0;
        this.hasTime = in.readByte()!=0;
        this.hasAlarm = in.readByte()!=0;
        this.placeName = in.readString();
        this.readableAddress = in.readString();
    }

    public ItemEntity(String title,
                      int priority, long date, boolean hasDate,
                      boolean hasTime, boolean hasAlarm,
                      String placeName,
                      String readableAddress) {
        this.title = title;
        this.priority = priority;
        this.date = date;
        this.hasDate = hasDate;
        this.hasTime = hasTime;
        this.hasAlarm = hasAlarm;
        this.placeName = placeName;
        this.readableAddress = readableAddress;
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

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getReadableAddress() {
        return readableAddress;
    }

    public void setReadableAddress(String readableAddress) { this.readableAddress = readableAddress; }

    public boolean isHasAlarm() { return hasAlarm; }

    public void setHasAlarm(boolean hasAlarm) {
        this.hasAlarm = hasAlarm;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public ItemEntity deepCopy(){
        ItemEntity  item = new ItemEntity(
                title,
                priority, date, hasDate, hasTime, hasAlarm,
                placeName, readableAddress
        );
        //Log.d("ItemEntity", item.hasAlarm?"True":"False");
        item.setId(id);
        return item;
    }
}
