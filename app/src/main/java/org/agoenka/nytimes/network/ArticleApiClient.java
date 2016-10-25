package org.agoenka.nytimes.network;

import org.agoenka.nytimes.models.Filter;
import org.agoenka.nytimes.models.ResponseWrapper;
import org.agoenka.nytimes.network.interfaces.ArticleService;
import org.agoenka.nytimes.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.text.TextUtils.isEmpty;
import static org.agoenka.nytimes.utils.DateUtils.FORMAT_YYYYMMDD;

/**
 * Author: agoenka
 * Created At: 10/19/2016
 * Version: ${VERSION}
 */

public class ArticleApiClient {

    static final String API_BASE_URL = "https://api.nytimes.com/svc/";
    static final String API_KEY = "f96a3e8855874a938fa7c06c0b633b69";
    static final String API_KEY_PARAM = "api_key";

    private Retrofit retrofit;

    public ArticleApiClient() {
        this.retrofit = ServiceGenerator.retrofit();
    }

    public void searchArticles(String query, Filter filter, int page, Callback<ResponseWrapper> callback) {
        String newsDesks = null;
        String beginDate = null;
        String sortOrder = null;

        if (filter != null) {
            if (AppUtils.isNotEmpty(filter.getNewsDesks())) {
                newsDesks = filter.getNewsDesks(true);
            }
            if (filter.getBeginDate() != null) {
                beginDate = filter.getBeginDate(FORMAT_YYYYMMDD);
            }
            if (!isEmpty(filter.getSortOrder())) {
                sortOrder = filter.getSortOrder();
            }
        }

        ArticleService articleService = retrofit.create(ArticleService.class);
        Call<ResponseWrapper> call = articleService.search(query, newsDesks, beginDate, sortOrder, page);
        call.enqueue(callback);
    }

}