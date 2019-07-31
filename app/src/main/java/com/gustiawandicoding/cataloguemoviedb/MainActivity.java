package com.gustiawandicoding.cataloguemoviedb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.gustiawandicoding.cataloguemoviedb.API.BaseApiService;
import com.gustiawandicoding.cataloguemoviedb.API.Server;
import com.gustiawandicoding.cataloguemoviedb.Adapter.MovieAdapter;
import com.gustiawandicoding.cataloguemoviedb.Category.FavoriteActivity;
import com.gustiawandicoding.cataloguemoviedb.Category.NowPlayingActivity;
import com.gustiawandicoding.cataloguemoviedb.Category.UpComingActivity;
import com.gustiawandicoding.cataloguemoviedb.Entity.Movies;
import com.gustiawandicoding.cataloguemoviedb.Entity.ResponseMovies;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView rvMovies;
    private MovieAdapter adapter;
    List<Movies> listMovies = new ArrayList<>();
    ProgressDialog loading;
    BaseApiService apiService;

    private final String api_key = BuildConfig.MOVIE_DB_API_KEY;
    private final String language = "en-US";
    private final String include_adult = "false";
    private final String page = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvMovies = findViewById(R.id.rv_movies);

        apiService = Server.getAPIService();

        adapter = new MovieAdapter(getApplicationContext(), listMovies);

        rvMovies.setHasFixedSize(true);
        rvMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvMovies.setAdapter(adapter);

        refresh();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void refresh() {
        loading = ProgressDialog.show(this, null, "Loading", true, false);
        apiService.getAllMovies(api_key, language,  include_adult,  page).enqueue(new Callback<ResponseMovies>() {

            @Override
            public void onResponse(Call<ResponseMovies> call, Response<ResponseMovies> response) {
                if (response.isSuccessful()){
                    loading.dismiss();
                    listMovies = response.body().getMovies();
                    rvMovies.setAdapter(new MovieAdapter(getApplicationContext(), listMovies));
                    adapter.notifyDataSetChanged();
                }else {
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setQueryHint(getResources().getString(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMovie(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    refresh();
                    return false;
                }
                return false;
            }
        });
        return true;
    }

    private void searchMovie(String keyword) {
        apiService.getSearchMovie(api_key, language, keyword, page, include_adult).enqueue(new Callback<ResponseMovies>() {
            @Override
            public void onResponse(Call<ResponseMovies> call, Response<ResponseMovies> response) {
                if (response.isSuccessful()){
                    listMovies = response.body().getMovies();
                    rvMovies.setAdapter(new MovieAdapter(getApplicationContext(), listMovies));
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getApplicationContext(), "Failed to load data", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseMovies> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            //pengaturan bahasa
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.now) {
            Intent now = new Intent(getApplicationContext(), NowPlayingActivity.class);
            startActivity(now);
            Toast.makeText(getApplicationContext(), "Load Now Playing Movies", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.up) {
            Intent up = new Intent(getApplicationContext(), UpComingActivity.class);
            startActivity(up);
            Toast.makeText(getApplicationContext(), "Load Upcoming Movies", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.fav){
            Intent fav = new Intent(getApplicationContext(), FavoriteActivity.class);
            startActivity(fav);
            Toast.makeText(getApplicationContext(), "Load Fav Movies", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
