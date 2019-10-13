package com.popular.movies.Adapter;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.popular.movies.DetailActivity;
import com.popular.movies.Model.Movies;
import com.popular.movies.R;
import com.popular.movies.Utils.Constant;
import java.util.ArrayList;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final Context context;
    public ArrayList<Movies> movies;

    public MoviesRecyclerAdapter(Context context, ArrayList<Movies> movies) {
        this.context = context;
        this.movies  = movies;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return movies.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof MoviesViewHolder) {
            final Movies mMovies = movies.get(position);

            // Set Data
            Glide.with(context).load(mMovies.Pic)
                    .placeholder(R.drawable.img_loader).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).override(200, 200)
                    .dontAnimate().into(((MoviesViewHolder) vh).imageView);
            ((MoviesViewHolder) vh).lblName.setText(mMovies.Title);

            ((MoviesViewHolder) vh).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("MOVIES", mMovies);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final RecyclerView.ViewHolder vh;

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_movies, viewGroup, false);
        vh = new MoviesViewHolder(viewGroup.getContext(), itemView);

        return vh;

    }

    public static class MoviesViewHolder extends RecyclerView.ViewHolder {
        protected CardView cardView;
        protected ImageView imageView;
        protected TextView lblName;

        public MoviesViewHolder(Context ctx, View convertView) {
            super(convertView);

            cardView  = (CardView) convertView.findViewById(R.id.cardView);
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            lblName   = (TextView) convertView.findViewById(R.id.lblName);
        }
    }
}
