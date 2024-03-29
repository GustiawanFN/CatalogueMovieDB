package com.gustiawandicoding.cataloguemoviedb.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gustiawandicoding.cataloguemoviedb.DetailMovieActivity;
import com.gustiawandicoding.cataloguemoviedb.Entity.Movies;
import com.gustiawandicoding.cataloguemoviedb.R;

import java.util.List;

/**
 * Created by Gustiawan on 10/7/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context context;
    private List<Movies> listMovies;

    public MovieAdapter(Context context, List<Movies> listMovies) {
        this.context = context;
        this.listMovies = listMovies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_movie, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movies movies = listMovies.get(position);
        Glide.with(context)
                .load("http://image.tmdb.org/t/p/w500" + movies.getPosterPath())
                .placeholder(R.drawable.def)
                .into(holder.gambar);
        holder.judul.setText(movies.getTitle());
        holder.desc.setText(movies.getOverview());
        holder.tgl.setText(movies.getReleaseDate());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailMovieActivity.class);
                i.putExtra("title", movies.getTitle());
                i.putExtra("poster_path", movies.getPosterPath());
                i.putExtra("overview", movies.getOverview());
                i.putExtra("release_date", movies.getReleaseDate());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView gambar;
        private CardView cv;
        private TextView judul, tgl, desc;

        ViewHolder(View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.card_view);
            gambar = itemView.findViewById(R.id.movie_poster);
            judul = itemView.findViewById(R.id.movie_name);
            desc = itemView.findViewById(R.id.movie_desc);
            tgl = itemView.findViewById(R.id.movie_date);
        }
    }
}