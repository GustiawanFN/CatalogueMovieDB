package com.gustiawandicoding.favoritemovies.Entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.gustiawandicoding.favoritemovies.DB.DatabaseContract;

import static android.provider.BaseColumns._ID;
import static com.gustiawandicoding.favoritemovies.DB.DatabaseContract.getColumnInt;
import static com.gustiawandicoding.favoritemovies.DB.DatabaseContract.getColumnString;

/**
 * Created by Gustiawan on 10/9/2018.
 */

public class MovieItem implements Parcelable {

    private int id;
    private String name;
    private String poster;
    private String date;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.date);
        dest.writeString(this.poster);
    }

    public MovieItem(Cursor cursor){
        this.id = getColumnInt(cursor, _ID);
        this.name = getColumnString(cursor, DatabaseContract.FavoriteColumns.NAME);
        this.poster = getColumnString(cursor, DatabaseContract.FavoriteColumns.POSTER);
        this.date = getColumnString(cursor, DatabaseContract.FavoriteColumns.RELEASE_DATE);
        this.description = getColumnString(cursor, DatabaseContract.FavoriteColumns.DESCRIPTION);
    }

    private MovieItem(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.description = in.readString();
        this.date = in.readString();
        this.poster = in.readString();
    }

    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };
}
