package com.popular.movies.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movies implements Parcelable {
    public String Id;
    public String Title;
    public String Genre;
    public String Synopsis;
    public String Pic;
    public String Rating;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(Title);
        dest.writeString(Genre);
        dest.writeString(Synopsis);
        dest.writeString(Pic);
        dest.writeString(Rating);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            Movies movies   = new Movies();
            movies.Id       = in.readString();
            movies.Title    = in.readString();
            movies.Genre    = in.readString();
            movies.Synopsis = in.readString();
            movies.Pic      = in.readString();
            movies.Rating   = in.readString();

            return movies;
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
}
