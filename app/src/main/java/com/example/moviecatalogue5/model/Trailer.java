package com.example.moviecatalogue5.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class Trailer implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private ArrayList<video> videos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<video> videos) {
        this.videos = videos;
    }

    public class video implements Serializable {
        @SerializedName("key")
        private String key;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
