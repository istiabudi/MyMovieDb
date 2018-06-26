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
import android.widget.EditText;
import android.widget.TextView;

import com.istiabudi.mymoviedb.DetailActivity;
import com.istiabudi.mymoviedb.Movie;
import com.istiabudi.mymoviedb.R;
import com.istiabudi.mymoviedb.SearchMovieAdapter;
import com.istiabudi.mymoviedb.SearchTask;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements SearchTask.ISearch, SearchMovieAdapter.IMovieAdapterListener, View.OnClickListener {

    private View view;
    private RecyclerView rvResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.view = inflater.inflate( R.layout.fragment_search, container, false);
        this.view.findViewById(R.id.btn_search).setOnClickListener(this);
        this.view.findViewById(R.id.v_searching).setVisibility(View.GONE);
        this.rvResult = this.view.findViewById(R.id.rv_result);
        return this.view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                String keyword = ((EditText) this.view.findViewById(R.id.et_keyword)).getText().toString();
                ((TextView) this.view.findViewById(R.id.tv_search_keyword)).setText(
                        getString(R.string.searching_for, keyword)
                );
                new SearchTask(this).execute(keyword);
                this.view.findViewById(R.id.v_searching).setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onSearchResult(ArrayList<Movie> movies) {
        this.view.findViewById(R.id.v_searching).setVisibility(View.GONE);
        SearchMovieAdapter adapter = new SearchMovieAdapter(this.getActivity(), movies, this);
        this.rvResult.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        this.rvResult.setAdapter(adapter);
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

}
