package com.istiabudi.mymoviedb.db;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.istiabudi.mymoviedb.Movie;
import com.istiabudi.mymoviedb.R;
import com.istiabudi.mymoviedb.Utility.DCM;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class FaveAdapter extends RecyclerView.Adapter<FaveAdapter.ViewHolder> {

    private Context context;
    private IFaveMovieAdapterListener listener;
    private Cursor cursor;

    public FaveAdapter(Context context, IFaveMovieAdapterListener listener) {
        this.context = context;
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPoster;
        TextView tvTitle;
        TextView tvOriginalTitle;
        TextView tvOverview;
        TextView tvReleaseDate;

        ViewHolder(View itemView) {
            super(itemView);
            this.ivPoster = itemView.findViewById( R.id.iv_poster);
            this.tvTitle = itemView.findViewById(R.id.tv_title);
            this.tvOriginalTitle = itemView.findViewById(R.id.tv_original_title);
            this.tvOverview = itemView.findViewById(R.id.tv_overview);
            this.tvReleaseDate = itemView.findViewById(R.id.tv_release_date);
        }

        void bind(final Movie movie, final FaveAdapter.IFaveMovieAdapterListener movieAdapterListener) {
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

    public void setListFave(Cursor cursor) {
        this.cursor = cursor;
    }

    private Movie getItem(int position) throws Exception {
        if(!this.cursor.moveToPosition(position)) {
            throw new Exception("Invalid position");
        }
        return new Movie(this.cursor);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_fave, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        try {
            final Movie movie = getItem(position);
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
            holder.bind(movie, this.listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(cursor == null) return 0;
        return cursor.getCount();
    }

    public interface IFaveMovieAdapterListener {
        void onItemClicked(Movie movie);
        void onItemShared(Movie movie);
    }
}
