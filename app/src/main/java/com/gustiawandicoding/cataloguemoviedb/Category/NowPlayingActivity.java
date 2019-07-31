package com.gustiawandicoding.cataloguemoviedb.Category;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.gustiawandicoding.cataloguemoviedb.API.BaseApiService;
import com.gustiawandicoding.cataloguemoviedb.API.Server;
import com.gustiawandicoding.cataloguemoviedb.Adapter.MovieAdapter;
import com.gustiawandicoding.cataloguemoviedb.BuildConfig;
import com.gustiawandicoding.cataloguemoviedb.Entity.Movies;
import com.gustiawandicoding.cataloguemoviedb.Entity.ResponseMovies;
import com.gustiawandicoding.cataloguemoviedb.MainActivity;
import com.gustiawandicoding.cataloguemoviedb.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NowPlayingActivity extends AppCompatActivity {RecyclerView rvMovies;
    private MovieAdapter adapter;
    List<Movies> listMovies = new ArrayList<>();
    ProgressDialog loading;
    BaseApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);



        rvMovies = findViewById(R.id.rv_movies);

        apiService = Server.getAPIService();

        adapter = new MovieAdapter(getApplicationContext(), listMovies);

        rvMovies.setHasFixedSize(true);
        rvMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvMovies.setAdapter(adapter);

        refresh();

        //membuat back button toolbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void refresh() {

        loading = ProgressDialog.show(this, null, "Loading", true, false);
        String api_key = BuildConfig.MOVIE_DB_API_KEY;
        String language = "en-US";
        apiService.getNowPlayingMovie(api_key, language).enqueue(new Callback<ResponseMovies>() {
            @Override
            public void onResponse(Call<ResponseMovies> call, Response<ResponseMovies> response) {
                if (response.isSuccessful()){
                    loading.dismiss();
                    listMovies = response.body().getMovies();
                    rvMovies.setAdapter(new MovieAdapter(getApplicationContext(), listMovies));
                    adapter.notifyDataSetChanged();
                }
                else {
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMovies> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
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

}
