package com.example.jamesnguyen.taskcycle.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ClipData;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "items")
public class ItemEntity implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    @ColumnInfo(name="date_in_millisecond")
    private long date;

    @ColumnInfo(name="has_date")
    private boolean hasDate;

    @ColumnInfo(name="has_time")
    private boolean hasTime;

    @ColumnInfo(name="place_name")
    private String placeName;

    @ColumnInfo(name="readable_access")
    private String readableAddress;

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
        dest.writeString(placeName);
        dest.writeString(readableAddress);
        dest.writeLong(date);
        dest.writeByte((byte)(hasDate?1:0));
        dest.writeByte((byte)(hasTime?1:0));
    }

    public ItemEntity(Parcel in){
        this.id = in.readInt();
        this.title = in.readString();
        this.date = in.readLong();
        this.hasDate = in.readByte()!=0;
        this.hasTime = in.readByte()!=0;
        this.placeName = in.readString();
        this.readableAddress = in.readString();
    }
    public ItemEntity(String title, long date, boolean hasDate, boolean hasTime, String placeName, String readableAddress) {
        this.title = title;
        this.date = date;
        this.hasDate = hasDate;
        this.hasTime = hasTime;
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

    public void setReadableAddress(String readableAddress) {
        this.readableAddress = readableAddress;
    }

}
