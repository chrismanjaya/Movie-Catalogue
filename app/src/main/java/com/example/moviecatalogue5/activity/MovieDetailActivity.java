package com.example.moviecatalogue5.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.moviecatalogue5.R;
import com.example.moviecatalogue5.adapter.ListCastAdapter;
import com.example.moviecatalogue5.adapter.ListCrewAdapter;
import com.example.moviecatalogue5.custom.TextViewRate;
import com.example.moviecatalogue5.model.Credit;
import com.example.moviecatalogue5.model.Favorite;
import com.example.moviecatalogue5.model.MovieDetail;
import com.example.moviecatalogue5.model.Trailer;
import com.example.moviecatalogue5.viewmodel.CreditVM;
import com.example.moviecatalogue5.viewmodel.FavoriteVM;
import com.example.moviecatalogue5.viewmodel.MovieDetailVM;
import com.example.moviecatalogue5.viewmodel.TrailerVM;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import static com.example.moviecatalogue5.model.Favorite.TYPE_MOVIE;
import static com.example.moviecatalogue5.utility.utils.formatDate;

public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String EXTRA_MOVIE_ID = "extra_movie_id";
    public static final String EXTRA_MOVIE_TITLE = "extra_movie_title";
    public static final String EXTRA_MOVIE_POSTER = "extra_movie_poster";
    public static final String EXTRA_MOVIE_DATE = "extra_movie_date";

    private ProgressBar progressBar;
    private ImageView imgBackdrop, imgPoster;
    private TextView txtTitle, txtDate, txtOverview, txtGenre;
    private TextViewRate txtRate;
    private Button btnTrailer;
    private RecyclerView rvCast, rvCrew;

    private Boolean isLoadMovieDetail = false;
    private Boolean isLoadCredit = false;
    private Boolean isLoadTrailer = false;
    private Boolean isLoadFavorite = false;

    private MovieDetailVM movieDetailVM;
    private CreditVM creditVM;
    private TrailerVM trailerVM;
    private FavoriteVM favoriteVM;

    private String uriTrailer;

    private boolean isFavorite = false;
    private Menu menu;

    private Favorite favorite = new Favorite();
    private static String FAVORITE_TYPE = TYPE_MOVIE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        int movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID,0);
        String movieTitle = getIntent().getStringExtra(EXTRA_MOVIE_TITLE);
        String moviePoster = getIntent().getStringExtra(EXTRA_MOVIE_POSTER);
        String movieDate = getIntent().getStringExtra(EXTRA_MOVIE_DATE);

        setTitle(movieTitle);
        favoriteVM = new FavoriteVM();
        favorite.setFilmId(String.valueOf(movieId));
        favorite.setTitle(movieTitle);
        favorite.setPosterPath(moviePoster);
        favorite.setReleaseDate(movieDate);
        favorite.setType(FAVORITE_TYPE);

        init();

        movieDetailVM = ViewModelProviders.of(this).get(MovieDetailVM.class);
        movieDetailVM.getMovieDetail().observe(this, getDataMovie);
        movieDetailVM.setDataMovieDetail(movieId);

        creditVM = ViewModelProviders.of(this).get(CreditVM.class);
        creditVM.getCredit().observe(this, getDataCredit);
        creditVM.setDataCredit(movieId, true);

        trailerVM = ViewModelProviders.of(this).get(TrailerVM.class);
        trailerVM.getTrailer().observe(this, getDataTrailer);
        trailerVM.setDataTrailer(movieId, true);
    }

    private void init() {
        progressBar = findViewById(R.id.progress_bar_movie);
        progressBar.setVisibility(View.VISIBLE);

        imgBackdrop = findViewById(R.id.img_backdrop_movie);
        imgPoster = findViewById(R.id.img_poster_movie);

        txtTitle = findViewById(R.id.text_title_movie);
        txtDate = findViewById(R.id.text_date_movie);
        txtRate = findViewById(R.id.text_rate_movie);
        txtOverview = findViewById(R.id.text_overview_movie);
        txtGenre = findViewById(R.id.text_genre_movie);

        btnTrailer = findViewById(R.id.btn_trailer_movie);
        btnTrailer.setOnClickListener(this);

        rvCast = findViewById(R.id.rv_cast_movie);
        rvCast.setHasFixedSize(true);
        rvCast.setLayoutManager(new LinearLayoutManager(this));
        rvCast.setNestedScrollingEnabled(false);

        rvCrew = findViewById(R.id.rv_crew_movie);
        rvCrew.setHasFixedSize(true);
        rvCrew.setLayoutManager(new LinearLayoutManager(this));
        rvCrew.setNestedScrollingEnabled(false);
    }

    private Observer<MovieDetail> getDataMovie = new Observer<MovieDetail>() {
        @Override
        public void onChanged(MovieDetail movieDetail) {
            if (movieDetail != null) {
                txtTitle.setText(movieDetail.getTitle());
                txtDate.setText(formatDate(movieDetail.getReleaseDate()));
                txtRate.setTextWithColor(movieDetail.getVoteAverage());
                txtOverview.setText(movieDetail.getOverview());

                String textGenre = "";
                ArrayList<MovieDetail.genre> genres = movieDetail.getGenres();
                for (MovieDetail.genre genre : genres) {
                    textGenre = textGenre + genre.getName() + ", ";
                }
                if (textGenre.length() > 2) {
                    textGenre = textGenre.substring(0, textGenre.length() - 2);
                    txtGenre.setText(textGenre);
                }

                setPoster(movieDetail.getPosterPath());
                setBackdrop(movieDetail.getBackdropPath());

                isLoadCredit = true;
                showLoading();
            }
        }
    };

    private Observer<Credit> getDataCredit = new Observer<Credit>() {
        @Override
        public void onChanged(Credit credit) {
            if (credit != null) {
                ArrayList<Credit.cast> cast = credit.getCasts();
                TextView txtCast = findViewById(R.id.text_cast);
                if (cast.size() > 0) {
                    txtCast.setVisibility(View.VISIBLE);
                    ListCastAdapter listCastAdapter = new ListCastAdapter(cast);
                    rvCast.setAdapter(listCastAdapter);
                } else {
                    txtCast.setVisibility(View.GONE);
                    View vLine = findViewById(R.id.view_line);
                    vLine.setVisibility(View.GONE);
                }

                ArrayList<Credit.crew> crew = credit.getCrews();
                TextView txtCrew = findViewById(R.id.text_crew);
                if (crew.size() > 0) {
                    txtCrew.setVisibility(View.VISIBLE);
                    ListCrewAdapter listCrewAdapter = new ListCrewAdapter(crew);
                    rvCrew.setAdapter(listCrewAdapter);
                } else {
                    txtCrew.setVisibility(View.GONE);
                    View vLine = findViewById(R.id.view_line);
                    vLine.setVisibility(View.GONE);
                }

                isLoadCredit = true;
                showLoading();
            }
        }
    };

    private Observer<Trailer> getDataTrailer = new Observer<Trailer>() {
        @Override
        public void onChanged(Trailer trailer) {
            if (trailer != null) {
                ArrayList<Trailer.video> videos = trailer.getVideos();
                if (videos.size() > 0) {
                    uriTrailer = "https://youtu.be/" + videos.get(0).getKey();
                    btnTrailer.setEnabled(true);
                } else {
                    btnTrailer.setEnabled(false);
                }
                isLoadTrailer = true;
                showLoading();
            }
        }
    };

    private void showLoading() {
        if (isLoadMovieDetail && isLoadCredit && isLoadTrailer && isLoadFavorite) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void setPoster(String path) {
        Picasso.with(imgPoster.getContext())
                .load(movieImagePathBuilder(path))
                .error(imgPoster.getContext().getResources().getDrawable(R.drawable.image_not_found))
                .fit().centerCrop().into(imgPoster);
    }

    private void setBackdrop(String path) {
        Picasso.with(imgBackdrop.getContext())
                .load(movieImagePathBuilder(path))
                .error(imgBackdrop.getContext().getResources().getDrawable(R.drawable.image_not_found))
                .fit().centerCrop().into(imgBackdrop);
    }

    private String movieImagePathBuilder(String imagePath) {
        String IMAGE_URL = "https://image.tmdb.org/t/p/";
        String POSTER_SIZE = "w500";
        return IMAGE_URL + POSTER_SIZE + imagePath;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_trailer_movie) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriTrailer));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage("com.google.android.youtube");
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        isFavorite = (boolean) favoriteVM.getFavoriteStatus(favorite, this);
        MenuItem item = menu.findItem(R.id.menuFavorite);
        if (isFavorite) {
            item.setIcon(R.drawable.ic_favorite_yes);
        } else {
            item.setIcon(R.drawable.ic_favorite_no);
        }
        isLoadFavorite = true;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuFavorite) {
            setFavoriteStatus();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFavoriteStatus() {
        String message;
        if (menu != null) {
            MenuItem item = menu.findItem(R.id.menuFavorite);
            if (item != null) {
                boolean result = favoriteVM.setFavoriteStatus(favorite, isFavorite,this);
                if (result != isFavorite) {
                    isFavorite = result;
                    if (isFavorite) {
                        message = getString(R.string.isFavoriteMovie);
                        item.setIcon(R.drawable.ic_favorite_yes);
                    } else {
                        message = getString(R.string.isNotFavoriteMovie);
                        item.setIcon(R.drawable.ic_favorite_no);
                    }

                    showToastMessage(favorite.getTitle(), message, this);
                }
            }
        }
    }

    private void showToastMessage(String messageTitle, String messageDesc, Context context) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layoutToast = layoutInflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.layout_toast));
        TextView txtToastTitle = layoutToast.findViewById(R.id.text_toast_title);
        TextView txtToastDesc = layoutToast.findViewById(R.id.text_toast_desc);
        txtToastTitle.setText(messageTitle);
        txtToastDesc.setText(messageDesc);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layoutToast);
        toast.show();
    }
}