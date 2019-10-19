package com.example.moviecatalogue5.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.moviecatalogue5.R;
import com.example.moviecatalogue5.model.Film;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import static com.example.moviecatalogue5.utility.utils.formatYear;

public class GridFilmAdapter extends RecyclerView.Adapter<GridFilmAdapter.ViewHolder> {

    private OnItemClickCallback onItemClickCallback;
    private ArrayList<Film> listFilm = new ArrayList<>();

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(Film film);
    }

    public void setData(ArrayList<Film> films) {
        if (films != null) {
            listFilm.clear();
            listFilm.addAll(films);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
        return new GridFilmAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Film film = this.listFilm.get(position);
        if (film.getTitle() != null) {
            holder.txtTitle.setText(film.getTitle());
        } else {
            holder.txtTitle.setText(film.getName());
        }
        if (film.getReleaseDate() != null) {
            holder.txtDate.setText(formatYear(film.getReleaseDate()));
        } else {
            holder.txtDate.setText(formatYear(film.getFirstAirDate()));
        }

        holder.bind(film);
        holder.imgItemPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(film);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.listFilm.size();
    }

    @Override
    public void onViewRecycled(GridFilmAdapter.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgItemPoster;
        ProgressBar progressBar;
        TextView txtTitle;
        TextView txtDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemPoster = itemView.findViewById(R.id.img_item_poster);
            progressBar = itemView.findViewById(R.id.progress_bar_grid);
            txtTitle = itemView.findViewById(R.id.text_title);
            txtDate = itemView.findViewById(R.id.text_date);
        }

        public void bind(final Film film) {
            Picasso.with(imgItemPoster.getContext())
                    .load(movieImagePathBuilder(film.getPosterPath()))
                    .fit().centerCrop()
                    .error(imgItemPoster.getContext().getResources().getDrawable(R.drawable.image_not_found))
                    .into(imgItemPoster, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }

        private String movieImagePathBuilder(String imagePath) {
            String IMAGE_URL = "https://image.tmdb.org/t/p/";
            String POSTER_SIZE = "w500";
            return IMAGE_URL + POSTER_SIZE + imagePath;
        }
    }
}
