package org.agoenka.nytimes.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.agoenka.nytimes.models.Filter;
import org.agoenka.nytimes.utils.AppUtils;

import static android.text.TextUtils.isEmpty;
import static org.agoenka.nytimes.utils.DateUtils.FORMAT_YYYYMMDD;

/**
 * Author: agoenka
 * Created At: 10/22/2016
 * Version: ${VERSION}
 */

public class ArticleSearchAPIClient {

    private static final String API_KEY = "f96a3e8855874a938fa7c06c0b633b69";
    private static final String API_BASE_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

    private AsyncHttpClient client;

    public ArticleSearchAPIClient () {
        this.client = getClient();
    }

    private static AsyncHttpClient getClient() {
        return new AsyncHttpClient();
    }

    private static RequestParams getParams(String query, Filter filter, int page) {
        RequestParams params = new RequestParams();
        params.put("api_key", API_KEY);
        params.put("q", query);
        if (filter != null) {
            if (filter.getBeginDate() != null) {
                params.put("begin_date", filter.getBeginDate(FORMAT_YYYYMMDD));
            }
            if (!isEmpty(filter.getSortOrder())) {
                params.put("sort", filter.getSortOrder());
            }
            if (!AppUtils.isEmpty(filter.getNewsDesks())) {
                params.put("fq", filter.getNewsDesks(true));
            }
        }
        params.put("page", page);
        return params;
    }

    public void getArticles(String query, Filter filter, int page, TextHttpResponseHandler handler) {
        String url = API_BASE_URL;
        RequestParams params = getParams(query, filter, page);
        client.get(url, params, handler);
    }
}
