package com.example.moviecatalogue5.network;

import com.example.moviecatalogue5.model.Credit;
import com.example.moviecatalogue5.model.FilmPageResult;
import com.example.moviecatalogue5.model.MovieDetail;
import com.example.moviecatalogue5.model.Trailer;
import com.example.moviecatalogue5.model.TvShowDetail;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // https://api.themoviedb.org/3/discover/movie?page=1&api_key=57107915a5d75b99a2b599620e92956e
    @GET("discover/movie")
    Call<FilmPageResult> getMovieList(@Query("page") int page, @Query("api_key") String userKey);

    // https://api.themoviedb.org/3/discover/tv?page=1&api_key=57107915a5d75b99a2b599620e92956e
    @GET("discover/tv")
    Call<FilmPageResult> getTvList(@Query("page") int page, @Query("api_key") String userKey);

    // https://api.themoviedb.org/3/movie/412117?api_key=57107915a5d75b99a2b599620e92956e
    @GET("movie/{movie_id}")
    Call<MovieDetail> getMovieDetail(@Path("movie_id") int movieId, @Query("api_key") String userKey);

    // https://api.themoviedb.org/3/tv/456?api_key=57107915a5d75b99a2b599620e92956e
    @GET("tv/{tv_id}")
    Call<TvShowDetail> getTvShowDetail(@Path("tv_id") int tvId, @Query("api_key") String userKey);

    // https://api.themoviedb.org/3/movie/423204/credits?api_key=57107915a5d75b99a2b599620e92956e
    @GET("movie/{movie_id}/credits")
    Call<Credit> getMovieCredit(@Path("movie_id") int movieId, @Query("api_key") String userKey);

    // https://api.themoviedb.org/3/tv/456/credits?api_key=57107915a5d75b99a2b599620e92956e
    @GET("tv/{tv_id}/credits")
    Call<Credit> getTvShowCredit(@Path("tv_id") int tvId, @Query("api_key") String userKey);

    // https://api.themoviedb.org/3/movie/423204/videos?api_key=57107915a5d75b99a2b599620e92956e
    @GET("movie/{movie_id}/videos")
    Call<Trailer> getMovieTrailer(@Path("movie_id") int movieId, @Query("api_key") String userKey);

    // https://api.themoviedb.org/3/tv/456/videos?api_key=57107915a5d75b99a2b599620e92956e
    @GET("tv/{tv_id}/videos")
    Call<Trailer> getTvShowTrailer(@Path("tv_id") int tvId, @Query("api_key") String userKey);

    // https://api.themoviedb.org/3/search/movie?query=Avenger&api_key=57107915a5d75b99a2b599620e92956e
    @GET("search/movie")
    Call<FilmPageResult> getSearchMovie(@Query("query") String keyword, @Query("api_key") String userKey);

    // https://api.themoviedb.org/3/search/tv?query=Avenger&api_key=57107915a5d75b99a2b599620e92956e
    @GET("search/tv")
    Call<FilmPageResult> getSearchTv(@Query("query") String keyword, @Query("api_key") String userKey);

    // https://api.themoviedb.org/3/discover/movie?primary_release_date.gte=2019-09-28&primary_release_date.lte=2019-09-28&api_key=57107915a5d75b99a2b599620e92956e
    @GET("discover/movie")
    Call<FilmPageResult> getMovieReleaseList(@Query("primary_release_date.gte") String dateReleaseGTE, @Query("primary_release_date.lte") String dateReleaseLTE, @Query("api_key") String userKey);
}
