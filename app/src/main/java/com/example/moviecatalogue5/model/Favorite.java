package com.example.moviecatalogue5.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Favorite implements Parcelable {
    public static final String TYPE_MOVIE = "movie";
    public static final String TYPE_TV_SHOW = "tvshow";

    private String filmId;
    private String title;
    private String releaseDate;
    private String posterPath;
    private String type;

    public Favorite() {}

    protected Favorite(Parcel in) {
        filmId = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        type = in.readString();
    }

    public static final Creator<Favorite> CREATOR = new Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel in) {
            return new Favorite(in);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };

    public String getFilmId() {
        return filmId;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String year) {
        this.releaseDate = year;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(filmId);
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeString(type);
    }
}
