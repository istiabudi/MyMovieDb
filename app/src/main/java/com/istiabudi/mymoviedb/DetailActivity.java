package com.istiabudi.mymoviedb;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.istiabudi.mymoviedb.Utility.DCM;
import com.istiabudi.mymoviedb.db.DatabaseContract;
import com.istiabudi.mymoviedb.db.FaveHelper;

import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView ivPoster;
    private boolean isImageFitToScreen;
    private ViewGroup.LayoutParams originalLayoutParams;
    private FaveHelper faveHelper;
    private Movie movie;
    private boolean faved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_detail );

        Intent intent = this.getIntent();

        int tmdbid = intent.getIntExtra("tmdbid", 0);

        String title = intent.getStringExtra("title");
        String originalTitle = intent.getStringExtra("original_title");
        String overview = intent.getStringExtra("overview");
        String releaseDate = intent.getStringExtra("release");
        String posterPath = intent.getStringExtra("poster");


        this.movie = new Movie(
                tmdbid, title, originalTitle, overview, releaseDate, posterPath
        );

        Cursor cursor = this.getContentResolver().query(
                DatabaseContract.contentUriWithId(this.movie.tmdbid),
                null, null, null, null);

        if(cursor != null && cursor.getCount() > 0) {
            ((ImageView)this.findViewById( R.id.bt_fav)).setImageResource(R.drawable.ic_baseline_star);
            this.faved = true;
        } else ((ImageView)this.findViewById( R.id.bt_fav)).setImageResource(R.drawable.ic_baseline_star_border);
        if(cursor != null) cursor.close();

        this.ivPoster = this.findViewById( R.id.iv_poster_detail);
        ((TextView) this.findViewById( R.id.tv_title_detail)).setText(title);
        ((TextView) this.findViewById( R.id.tv_original_title_detail)).setText(originalTitle);
        ((TextView) this.findViewById( R.id.tv_overview_detail)).setText(overview);
        ((TextView) this.findViewById( R.id.tv_release_date_detail)).setText(getString(R.string.released, releaseDate));
        ((TextView) this.findViewById( R.id.tv_loading_poster)).setText(R.string.loading_poster);

        try {
            Picasso.with(this).load( DCM.PosterLargeBasePath + posterPath).into(this.ivPoster);
        } catch (Exception ex) {
            Log.e(DCM.AppTag, ex.getMessage());
        }

        this.ivPoster.setOnClickListener(this);
        this.findViewById( R.id.bt_fav).setOnClickListener(this);

        this.faveHelper = new FaveHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_poster_detail: // toggle poster fullscreen
                if (isImageFitToScreen) {
                    this.findViewById( R.id.fl_poster_container).setLayoutParams(originalLayoutParams);
                    this.findViewById( R.id.tv_large_toggle).setVisibility(View.VISIBLE);
                    this.ivPoster.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    this.isImageFitToScreen = false;
                } else {
                    this.originalLayoutParams = this.findViewById( R.id.fl_poster_container).getLayoutParams();
                    this.findViewById( R.id.fl_poster_container)
                            .setLayoutParams(
                                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                            );
                    this.findViewById( R.id.tv_large_toggle).setVisibility(View.GONE);
                    this.ivPoster.setScaleType(ImageView.ScaleType.FIT_XY);
                    this.isImageFitToScreen = true;
                }
                break;
            case R.id.bt_fav:

                this.faveHelper.open();
                if(this.faved) {

                    // delete via Helper
                    //this.faveHelper.delete(movie.tmdbid);

                    // delete via ContentProvider
                    this.getContentResolver().delete(DatabaseContract.contentUriWithId(movie.tmdbid), null, null);
                    Toast.makeText(this, getString(R.string.remove_from_fave, movie.title), Toast.LENGTH_SHORT).show();

                    this.faved = false;
                } else {

                    ContentValues values = new ContentValues();
                    values.put(DatabaseContract.FaveColumns.TMDBID, movie.tmdbid);
                    values.put(DatabaseContract.FaveColumns.TITLE, movie.title);
                    values.put(DatabaseContract.FaveColumns.ORIGINALTITLE, movie.originalTitle);
                    values.put(DatabaseContract.FaveColumns.OVERVIEW, movie.overview);
                    values.put(DatabaseContract.FaveColumns.RELEASEDATE, movie.releaseDate);
                    values.put(DatabaseContract.FaveColumns.POSTER, movie.poster);

                    // insert via ContentProvider
                    this.getContentResolver().insert(DatabaseContract.contentUri(), values);
                    Toast.makeText(this, getString(R.string.added_to_fave, movie.title), Toast.LENGTH_SHORT).show();

                    // insert via Helper
                    //this.faveHelper.insert(movie);

                    this.faved = true;
                }
                this.faveHelper.close();
                if(this.faved)
                    ((ImageView)this.findViewById( R.id.bt_fav)).setImageResource(R.drawable.ic_baseline_star);
                else ((ImageView)this.findViewById( R.id.bt_fav)).setImageResource(R.drawable.ic_baseline_star_border);
                break;
        }
    }
}
