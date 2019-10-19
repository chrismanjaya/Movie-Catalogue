package com.example.moviecatalogue5.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.moviecatalogue5.R;
import com.example.moviecatalogue5.model.Credit;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ListCrewAdapter extends RecyclerView.Adapter<ListCrewAdapter.ListViewHolder> {

    private ArrayList<Credit.crew> crews;

    public ListCrewAdapter(ArrayList<Credit.crew> data) {
        this.crews = data;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Credit.crew crew = crews.get(position);
        setImage(holder.imgPhoto, crew.getProfilePath());
        holder.txtName.setText(crew.getName());
        holder.txtJob.setText(crew.getJob());
    }

    @Override
    public int getItemCount() {
        return crews.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;
        TextView txtName, txtJob;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            txtName = itemView.findViewById(R.id.tv_item_name);
            txtJob = itemView.findViewById(R.id.tv_item_detail);
        }
    }

    private void setImage(ImageView image, String path) {
        Picasso.with(image.getContext())
                .load(movieImagePathBuilder(path))
                .fit().centerCrop()
                .error(image.getContext().getResources().getDrawable(R.drawable.ic_profile_picture))
                .into(image);
    }

    private String movieImagePathBuilder(String imagePath) {
        String IMAGE_URL = "https://image.tmdb.org/t/p/";
        String POSTER_SIZE = "w500";
        return IMAGE_URL + POSTER_SIZE + imagePath;
    }
}
