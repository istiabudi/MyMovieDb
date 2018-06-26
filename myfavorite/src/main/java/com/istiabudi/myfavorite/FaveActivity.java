package com.istiabudi.myfavorite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fave);

        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new FaveFragment(), "faveFragment").commit();
    }
}
