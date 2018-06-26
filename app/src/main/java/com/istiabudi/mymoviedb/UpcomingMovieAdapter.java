package com.istiabudi.mymoviedb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.istiabudi.mymoviedb.Utility.DCM;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class UpcomingMovieAdapter extends RecyclerView.Adapter<UpcomingMovieAdapter.ViewHolder> {

    private Context context;
    private IUpcomingMovieAdapterListener movieAdapterListener;
    private ArrayList<Movie> movies;

    public UpcomingMovieAdapter(Context context, ArrayList<Movie> movies, IUpcomingMovieAdapterListener movieAdapterListener) {
        this.context = context;
        this.movies = movies;
        this.movieAdapterListener = movieAdapterListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_upcoming, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Movie movie = this.movies.get(position);
        if(movie.getPosterBitmap() != null)
            holder.ivPoster.setImageBitmap(movie.getPosterBitmap());
        else {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    movie.setPosterBitmap(bitmap);
                    holder.ivPoster.setImageBitmap(bitmap);
                }
                @Override
                public void onBitmapFailed(Drawable errorDrawable) {}
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {}
            };
            Picasso.with(this.context)
                    .load( DCM.PosterThumbnailBasePath + movie.poster)
                    .into(target);
        }
        holder.tvTitle.setText(movie.title);
        holder.tvOriginalTitle.setText(movie.originalTitle);
        holder.tvOverview.setText(movie.overview);
        holder.tvReleaseDate.setText(this.context.getString(R.string.released, movie.releaseDate));

        holder.bind(movie, this.movieAdapterListener);
    }

    @Override
    public int getItemCount() {
        return this.movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPoster;
        TextView tvTitle;
        TextView tvOriginalTitle;
        TextView tvOverview;
        TextView tvReleaseDate;

        ViewHolder(View itemView) {
            super(itemView);
            this.ivPoster = itemView.findViewById(R.id.iv_poster);
            this.tvTitle = itemView.findViewById(R.id.tv_title);
            this.tvOriginalTitle = itemView.findViewById(R.id.tv_original_title);
            this.tvOverview = itemView.findViewById(R.id.tv_overview);
            this.tvReleaseDate = itemView.findViewById(R.id.tv_release_date);
        }

        void bind(final Movie movie, final IUpcomingMovieAdapterListener movieAdapterListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(movieAdapterListener != null) {
                        movieAdapterListener.onItemClicked(movie);
                    }
                }
            });
            itemView.findViewById(R.id.bt_share).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(movieAdapterListener != null) {
                        movieAdapterListener.onItemShared(movie);
                    }
                }
            });
        }
    }

    public interface IUpcomingMovieAdapterListener {
        void onItemClicked(Movie movie);
        void onItemShared(Movie movie);
    }
}
