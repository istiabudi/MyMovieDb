package com.istiabudi.mymoviedb;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.istiabudi.mymoviedb.Fragments.FaveFragment;
import com.istiabudi.mymoviedb.Fragments.NowPlayingFragment;
import com.istiabudi.mymoviedb.Fragments.SearchFragment;
import com.istiabudi.mymoviedb.Fragments.UpcomingFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        PagerAdapter pagerAdapter = new PagerAdapter(this.getSupportFragmentManager());
        ViewPager pager = this.findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
        TabLayout tabs = this.findViewById(R.id.tabs);
        tabs.setupWithViewPager(pager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_language_settings:
                Intent mIntent = new Intent( Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                break;
            case R.id.action_favorite:

                FragmentManager fm = getSupportFragmentManager();
                Fragment faveFrag = fm.findFragmentByTag("faveFragment");
                if (faveFrag != null)
                    fm.beginTransaction().remove(faveFrag).commit();

                FaveFragment faveFragment = new FaveFragment();
                faveFragment.show(fm, "faveFragment");

                break;
        }
        return true;
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments = new ArrayList<>();

        PagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments.add(new UpcomingFragment());
            this.fragments.add(new NowPlayingFragment());
            this.fragments.add(new SearchFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.upcoming);
                case 1: return getString(R.string.now_playing);
                case 2: return getString(R.string.movie_search);
                default: return getString(R.string.movie_database);
            }
        }
    }
}
