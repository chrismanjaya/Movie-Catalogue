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

public class ListCastAdapter extends RecyclerView.Adapter<ListCastAdapter.ListViewHolder> {

    private ArrayList<Credit.cast> casts;

    public ListCastAdapter(ArrayList<Credit.cast> data) {
        this.casts = data;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Credit.cast cast = casts.get(position);
        setImage(holder.imgPhoto, cast.getProfilePath());
        holder.txtName.setText(cast.getName());
        holder.txtCharacter.setText(cast.getCharacter());
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;
        TextView txtName, txtCharacter;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            txtName = itemView.findViewById(R.id.tv_item_name);
            txtCharacter = itemView.findViewById(R.id.tv_item_detail);
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
