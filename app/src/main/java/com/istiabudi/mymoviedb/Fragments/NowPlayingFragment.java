package com.istiabudi.mymoviedb.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.istiabudi.mymoviedb.DetailActivity;
import com.istiabudi.mymoviedb.Movie;
import com.istiabudi.mymoviedb.NowPlayingMovieAdapter;
import com.istiabudi.mymoviedb.NowPlayingTask;
import com.istiabudi.mymoviedb.R;

import java.util.ArrayList;

public class NowPlayingFragment extends Fragment
        implements NowPlayingMovieAdapter.INowPlayingMovieAdapterListener, NowPlayingTask.INowPlayingListener {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate( R.layout.fragment_now_playing, container, false);
        return this.view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new NowPlayingTask(this).execute();
        this.view.findViewById(R.id.ll_loading).setVisibility(View.VISIBLE);
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
    public void onItemShared(com.istiabudi.mymoviedb.Movie movie) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, movie.title);
        share.putExtra(Intent.EXTRA_TEXT, movie.title + " - https://www.themoviedb.org/movie/" + movie.tmdbid);
        startActivity(Intent.createChooser(share, getString(R.string.share_to)));
    }

    @Override
    public void onNowPlayingResult(ArrayList<Movie> movies) {
        this.view.findViewById(R.id.ll_loading).setVisibility(View.GONE);
        RecyclerView rv = this.view.findViewById(R.id.rv_now_playing);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rv.setAdapter(new NowPlayingMovieAdapter(this.getActivity(), movies, this));
    }
}
