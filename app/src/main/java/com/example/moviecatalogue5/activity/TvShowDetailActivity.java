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
import com.example.moviecatalogue5.model.Trailer;
import com.example.moviecatalogue5.model.TvShowDetail;
import com.example.moviecatalogue5.viewmodel.CreditVM;
import com.example.moviecatalogue5.viewmodel.FavoriteVM;
import com.example.moviecatalogue5.viewmodel.TrailerVM;
import com.example.moviecatalogue5.viewmodel.TvShowDetailVM;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import static com.example.moviecatalogue5.model.Favorite.TYPE_TV_SHOW;
import static com.example.moviecatalogue5.utility.utils.formatDate;

public class TvShowDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_TV_ID = "extra_tv_id";
    public static final String EXTRA_TV_TITLE = "extra_tv_title";
    public static final String EXTRA_TV_POSTER = "extra_tv_poster";
    public static final String EXTRA_TV_DATE = "extra_tv_date";

    private ProgressBar progressBar;
    private ImageView imgBackdrop, imgPoster;
    private TextView txtName, txtDate, txtOverview, txtGenre;
    private TextViewRate txtRate;
    private Button btnTrailer;
    private RecyclerView rvCast, rvCrew;

    private Boolean isLoadMovieDetail = false;
    private Boolean isLoadCredit = false;
    private Boolean isLoadTrailer = false;
    private Boolean isLoadFavorite = false;

    private TvShowDetailVM tvShowDetailVM;
    private CreditVM creditVM;
    private TrailerVM trailerVM;
    private FavoriteVM favoriteVM;

    private String uriTrailer;

    private Boolean isFavorite = false;
    private Menu menu;

    private Favorite favorite = new Favorite();
    private static String FAVORITE_TYPE = TYPE_TV_SHOW;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_detail);

        int tvId = getIntent().getIntExtra(EXTRA_TV_ID, 0);
        String tvName = getIntent().getStringExtra(EXTRA_TV_TITLE);
        String tvPoster = getIntent().getStringExtra(EXTRA_TV_POSTER);
        String tvDate = getIntent().getStringExtra(EXTRA_TV_DATE);

        setTitle(tvName);

        favoriteVM = new FavoriteVM();
        favorite.setFilmId(String.valueOf(tvId));
        favorite.setTitle(tvName);
        favorite.setPosterPath(tvPoster);
        favorite.setReleaseDate(tvDate);
        favorite.setType(FAVORITE_TYPE);

        init();

        tvShowDetailVM = ViewModelProviders.of(this).get(TvShowDetailVM.class);
        tvShowDetailVM.getTvShowDetail().observe(this, getDataTvShow);
        tvShowDetailVM.setDataTvShowDetail(tvId);

        creditVM = ViewModelProviders.of(this).get(CreditVM.class);
        creditVM.getCredit().observe(this, getDataCredit);
        creditVM.setDataCredit(tvId, false);

        trailerVM = ViewModelProviders.of(this).get(TrailerVM.class);
        trailerVM.getTrailer().observe(this, getDataTrailer);
        trailerVM.setDataTrailer(tvId, false);
    }

    private void init(){
        progressBar = findViewById(R.id.progress_bar_tv);
        progressBar.setVisibility(View.VISIBLE);

        imgBackdrop = findViewById(R.id.img_backdrop_tv);
        imgPoster = findViewById(R.id.img_poster_tv);

        txtName = findViewById(R.id.text_title_tv);
        txtDate = findViewById(R.id.text_date_tv);
        txtRate = findViewById(R.id.text_rate_tv);
        txtOverview = findViewById(R.id.text_overview_tv);
        txtGenre = findViewById(R.id.text_genre_tv);

        btnTrailer = findViewById(R.id.btn_trailer_tv);
        btnTrailer.setOnClickListener(this);

        rvCast = findViewById(R.id.rv_cast_tv);
        rvCast.setHasFixedSize(true);
        rvCast.setLayoutManager(new LinearLayoutManager(this));
        rvCast.setNestedScrollingEnabled(false);

        rvCrew = findViewById(R.id.rv_crew_tv);
        rvCrew.setHasFixedSize(true);
        rvCrew.setLayoutManager(new LinearLayoutManager(this));
        rvCrew.setNestedScrollingEnabled(false);
    }

    private Observer<TvShowDetail> getDataTvShow = new Observer<TvShowDetail>() {
        @Override
        public void onChanged(TvShowDetail tvShowDetail) {
            if (tvShowDetail != null) {
                txtName.setText(tvShowDetail.getName());
                txtDate.setText(formatDate(tvShowDetail.getFirstAirDate()));
                txtRate.setTextWithColor(tvShowDetail.getVoteAverage());
                txtOverview.setText(tvShowDetail.getOverview());

                String textGenre = "";
                ArrayList<TvShowDetail.genre> genres = tvShowDetail.getGenres();
                for (TvShowDetail.genre genre : genres) {
                    textGenre = textGenre + genre.getName() + ", ";
                }
                if (textGenre.length() > 2) {
                    textGenre = textGenre.substring(0, textGenre.length() - 2);
                    txtGenre.setText(textGenre);
                }

                setPoster(tvShowDetail.getPosterPath());
                setBackdrop(tvShowDetail.getBackdropPath());

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
        if (view.getId() == R.id.btn_trailer_tv) {
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
        isFavorite = favoriteVM.getFavoriteStatus(favorite, this);
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
                        message = getString(R.string.isFavoriteTv);
                        item.setIcon(R.drawable.ic_favorite_yes);
                    } else {
                        message = getString(R.string.isNotFavoriteTv);
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
