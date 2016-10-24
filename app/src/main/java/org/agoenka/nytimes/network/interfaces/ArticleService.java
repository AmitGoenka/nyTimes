package org.agoenka.nytimes.network.interfaces;

import org.agoenka.nytimes.models.ResponseWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ArticleService {

    @GET("search/v2/articlesearch.json")
    Call<ResponseWrapper> search(@Query("q") String query,
                                 @Query("fq") String newsDesks,
                                 @Query("begin_date") String beginDate,
                                 @Query("sort") String sortOrder,
                                 @Query("page") int page);
}
