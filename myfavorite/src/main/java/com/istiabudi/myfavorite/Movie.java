package com.istiabudi.myfavorite;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.istiabudi.myfavorite.db.DatabaseContract;


public class Movie implements Parcelable {

    private int _id;
    public int tmdbid;
    public String title;
    public String originalTitle;
    public String overview;
    public String releaseDate;
    public String poster;

    public Movie(int tmdbid, String title, String originalTitle, String overview,
                 String releaseDate, String poster) {
        this.tmdbid = tmdbid;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.poster = poster;
    }

    public Movie(int _id, int tmdbid, String title, String originalTitle, String overview,
                 String releaseDate, String poster) {
        this(tmdbid, title, originalTitle, overview, releaseDate, poster);
        this._id = _id;
    }

    private Bitmap posterBitmap;

    public Movie(Cursor cursor) {
        this._id = DatabaseContract.getColumnInt(cursor, DatabaseContract.FaveColumns.ID);
        this.tmdbid = DatabaseContract.getColumnInt(cursor, DatabaseContract.FaveColumns.TMDBID);
        this.title = DatabaseContract.getColumnString(cursor, DatabaseContract.FaveColumns.TITLE);
        this.originalTitle = DatabaseContract.getColumnString(cursor, DatabaseContract.FaveColumns.ORIGINALTITLE);
        this.overview = DatabaseContract.getColumnString(cursor, DatabaseContract.FaveColumns.OVERVIEW);
        this.releaseDate = DatabaseContract.getColumnString(cursor, DatabaseContract.FaveColumns.RELEASEDATE);
        this.poster = DatabaseContract.getColumnString(cursor, DatabaseContract.FaveColumns.POSTER);
    }

    protected Movie(Parcel in) {
        _id = in.readInt();
        tmdbid = in.readInt();
        title = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        poster = in.readString();
        posterBitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public void setPosterBitmap(Bitmap bitmap) {
        this.posterBitmap = bitmap;
    }

    public Bitmap getPosterBitmap() {
        return posterBitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeInt(tmdbid);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(poster);
        dest.writeParcelable(posterBitmap, flags);
    }
}
