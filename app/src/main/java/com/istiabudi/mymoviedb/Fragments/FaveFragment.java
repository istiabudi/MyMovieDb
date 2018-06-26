package com.istiabudi.mymoviedb.Fragments;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.istiabudi.mymoviedb.DetailActivity;
import com.istiabudi.mymoviedb.Movie;
import com.istiabudi.mymoviedb.R;
import com.istiabudi.mymoviedb.db.DatabaseContract;
import com.istiabudi.mymoviedb.db.FaveAdapter;

public class FaveFragment extends DialogFragment implements FaveAdapter.IFaveMovieAdapterListener {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_fave, container, false);
        return this.view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        Point size = new Point();

        if(window != null) {
            Display display = window.getWindowManager().getDefaultDisplay();
            display.getSize(size);
            window.setLayout((int) (size.x * 0.98), (int) (size.y * 0.9));
            window.setGravity( Gravity.CENTER);
        }

        if(this.getActivity() != null) {
            FaveAdapter faveAdapter = new FaveAdapter(this.getActivity(), this);
            faveAdapter.setListFave(this.getActivity().getContentResolver().query(
                    DatabaseContract.contentUri(),
                    null,
                    null,
                    null,
                    null));
            RecyclerView rvFave = this.view.findViewById( R.id.rv_fave);
            rvFave.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            rvFave.setAdapter(faveAdapter);
        }
    }

    @Override
    public void onItemClicked(Movie movie) {
        Intent i = new Intent(this.getActivity(), DetailActivity.class);

        //Bundle bundle = new Bundle();

        i.putExtra("tmdbid", movie.tmdbid);
        i.putExtra("title", movie.title);
        i.putExtra("original_title", movie.originalTitle);
        i.putExtra("overview", movie.overview);
        i.putExtra("poster", movie.poster);
        i.putExtra("release", movie.releaseDate);

        this.startActivity(i);
    }

    @Override
    public void onItemShared(Movie movie) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);

        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, movie.title);
        share.putExtra(Intent.EXTRA_TEXT, movie.title + " - https://www.themoviedb.org/movie/" + movie.tmdbid);

        startActivity(Intent.createChooser(share, getString(R.string.share_to)));
    }
}
