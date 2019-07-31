package com.gustiawandicoding.cataloguemoviedb.API;

import com.gustiawandicoding.cataloguemoviedb.Entity.ResponseMovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Gustiawan on 10/7/2018.
 */

public interface BaseApiService {
    @GET("3/discover/movie")
    Call<ResponseMovies> getAllMovies(@Query("api_key") String api_key,
                                      @Query("language") String language,
                                      @Query("include_adult") String include_adult,
                                      @Query("page") String page );
    @GET("/3/search/movie")
    Call<ResponseMovies> getSearchMovie(@Query("api_key") String api_key,
                                        @Query("language") String language,
                                        @Query("query") String query,
                                        @Query("page") String page,
                                        @Query("include_adult") String include_adult);

    @GET("/3/movie/now_playing")
    Call<ResponseMovies> getNowPlayingMovie(@Query("api_key") String api_key,
                                            @Query("language") String language);

    @GET("/3/movie/upcoming")
    Call<ResponseMovies> getUpComingMovie(@Query("api_key") String api_key,
                                          @Query("language") String language);

}
