package com.gustiawandicoding.cataloguemoviedb;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gustiawandicoding.cataloguemoviedb.DB.DatabaseContract;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gustiawandicoding.cataloguemoviedb.DB.DatabaseContract.FavoriteColumns.CONTENT_URI;

public class DetailMovieActivity extends AppCompatActivity {

    String img, judul, desc, tgl;

    @BindView(R.id.poster) ImageView tvImg;
    @BindView(R.id.judul) TextView tvJudul ;
    @BindView(R.id.desc) TextView tvDesc;
    @BindView(R.id.tgl) TextView tvTgl;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.love)FloatingActionButton fvFav;

    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        ButterKnife.bind(this);

        setMovie();
        setFavorite();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Detail Movie");
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setMovie(){
        img = getIntent().getStringExtra("poster_path");
        judul = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("overview");
        tgl = getIntent().getStringExtra("release_date");

        Glide.with(getApplicationContext())
                .load("http://image.tmdb.org/t/p/w185"+img)
                .placeholder(R.drawable.def)
                .into(tvImg);
        tvJudul.setText(judul);
        tvDesc.setText(desc);
        tvTgl.setText(tgl);
        fvFav.setImageResource(R.drawable.ic_star_unchecked);
    }

    public boolean setFavorite(){
        Uri uri = Uri.parse(CONTENT_URI+"");
        boolean favorite = false;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        String getTitle;
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getLong(0);
                getTitle = cursor.getString(1);
                if (getTitle.equals(getIntent().getStringExtra("title"))){
                    fvFav.setImageResource(R.drawable.ic_star_checked);
                    favorite = true;

                }
            } while (cursor.moveToNext());

        }
        return favorite;
    }

    private void showSnackbarMessage(int message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    public void favorite (View view) {
        if(setFavorite()){
            Uri uri = Uri.parse(CONTENT_URI+"/"+id);
            getContentResolver().delete(uri, null, null);
            fvFav.setImageResource(R.drawable.ic_star_unchecked);
        }
        else{
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.FavoriteColumns.NAME, judul);
            values.put(DatabaseContract.FavoriteColumns.POSTER, img);
            values.put(DatabaseContract.FavoriteColumns.RELEASE_DATE, tgl);
            values.put(DatabaseContract.FavoriteColumns.DESCRIPTION, desc);

            getContentResolver().insert(CONTENT_URI, values);
            setResult(101);

            fvFav.setImageResource(R.drawable.ic_star_checked);
            showSnackbarMessage(R.string.addto);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home : {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
