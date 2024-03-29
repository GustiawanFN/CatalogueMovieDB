package com.gustiawandicoding.cataloguemoviedb.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gustiawan on 10/7/2018.
 */

public class ResponseMovies {
    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("total_results")
    @Expose
    private int totalResults;

    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    @SerializedName("results")
    @Expose
    private List<Movies> movies = null;

    public ResponseMovies(int page, int totalResults, int totalPages, List<Movies> movies) {
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.movies = movies;
    }

    public int getPage() {
        return page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Movies> getMovies() {
        return movies;
    }
}
