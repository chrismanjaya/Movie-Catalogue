package com.example.moviecatalogue5.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class FilmPageResult implements Serializable {

    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int totalResult;
    @SerializedName("total_pages")
    private int totalPage;
    @SerializedName("results")
    private ArrayList<Film> filmResult;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public ArrayList<Film> getFilmResult() {
        return filmResult;
    }

    public void setFilmResult(ArrayList<Film> filmResult) {
        this.filmResult = filmResult;
    }

    public int getCountResult() {
        return  filmResult.size();
    }
}
