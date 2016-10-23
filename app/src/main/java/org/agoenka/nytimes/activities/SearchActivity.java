package org.agoenka.nytimes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.agoenka.nytimes.R;
import org.agoenka.nytimes.adapters.ArticleArrayAdapter;
import org.agoenka.nytimes.adapters.EndlessScrollListener;
import org.agoenka.nytimes.models.Article;
import org.agoenka.nytimes.models.Filter;
import org.agoenka.nytimes.network.ArticleSearchAPIClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import cz.msebera.android.httpclient.Header;

import static org.agoenka.nytimes.network.NetworkUtils.isConnected;

public class SearchActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_SELECT_FILTER = 1;

    @BindView(R.id.gvResults) GridView gvResults;
    @BindView(R.id.toolbar) Toolbar toolbar;
    SearchView searchView;

    List<Article> articles;
    ArticleArrayAdapter adapter;
    Filter filter;
    String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setupViews();
    }

    private void setupViews() {
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);
        setupScrollListener();
    }

    private void setupScrollListener() {
        // Attach the On Scroll Listener to the AdapterView
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemCount) {
                // Triggered only when new data needs to be appended to the list
                // Append new items to the AdapterView
                return loadMoreArticles(page);
            }
        });
    }

    // register listener for grid on click
    @OnItemClick(R.id.gvResults)
    public void OnClickArticle(int position) {
        // create an intent to display the article
        Intent intent = new Intent(SearchActivity.this, ArticleActivity.class);
        // get the article to display
        Article article = articles.get(position);
        // pass in that article into the intent
        intent.putExtra("article", article);
        // launch the activity
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                setupScrollListener();
                fetchArticles();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        // The action bar will automatically handle clicks on the Home/Up button,
        // As long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            Intent intent = new Intent(this, FilterActivity.class);
            if (filter != null) intent.putExtra("filter", filter);
            startActivityForResult(intent, REQUEST_CODE_SELECT_FILTER);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_FILTER) {
            if (resultCode == RESULT_OK) {
                filter = (Filter) data.getSerializableExtra("filter");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void fetchArticles() {
        if (isConnected(this)) {
            new ArticleSearchAPIClient().getArticles(searchQuery, filter, 0, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONArray articleJsonResults;
                    try {
                        articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        adapter.addAll(Article.fromJSONArray(articleJsonResults));
                        Log.d("DEBUG", articles.toString());
                    } catch (JSONException e) {
                        Log.d("DEBUG", e.getMessage());
                        Toast.makeText(SearchActivity.this, "Error occurred while parsing the search responses!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                    Log.d("DEBUG", throwable.getMessage());
                    Log.d("DEBUG", "StatusCode: " + statusCode + ", Error Message: " + jsonObject);
                    Toast.makeText(SearchActivity.this, "Error occurred while retrieving the articles.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Append more data into the adapter
    public boolean loadMoreArticles(int offset) {
        // This method sends out a network request and appends new data items to your adapter.
        // The offset value ise used as a parameter to the API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        if (isConnected(this)) {
            new ArticleSearchAPIClient().getArticles(searchQuery, filter, offset, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONArray articleJsonResults;
                    try {
                        articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                        adapter.addAll(Article.fromJSONArray(articleJsonResults));
                        Log.d("DEBUG", articles.toString());
                    } catch (JSONException e) {
                        Log.d("DEBUG", e.getMessage());
                        Toast.makeText(SearchActivity.this, "Error occurred while parsing the search responses!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                    Log.d("DEBUG", throwable.getMessage());
                    Log.d("DEBUG", "StatusCode: " + statusCode + ", Error Message: " + jsonObject);
                    Toast.makeText(SearchActivity.this, "Error occurred while retrieving more articles.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            return false;
        }
        return true; // ONLY if more data is actually being loaded; false otherwise.
    }
}