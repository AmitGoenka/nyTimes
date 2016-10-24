package org.agoenka.nytimes.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.agoenka.nytimes.R;
import org.agoenka.nytimes.adapters.SearchResultsAdapter;
import org.agoenka.nytimes.databinding.ActivitySearchBinding;
import org.agoenka.nytimes.fragments.FilterSettingsFragment;
import org.agoenka.nytimes.helpers.EndlessRecyclerViewScrollListener;
import org.agoenka.nytimes.helpers.ItemClickSupport;
import org.agoenka.nytimes.models.Article;
import org.agoenka.nytimes.models.Filter;
import org.agoenka.nytimes.models.ResponseWrapper;
import org.agoenka.nytimes.network.ArticleApiClient;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.agoenka.nytimes.network.NetworkUtils.isConnected;

public class SearchActivity extends AppCompatActivity {

    RecyclerView rvResults;
    SearchView searchView;
    private ActivitySearchBinding binding;

    List<Article> articles;
    SearchResultsAdapter adapter;
    StaggeredGridLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener scrollListener;
    Filter filter;
    String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        setSupportActionBar(binding.toolbar);

        configureRecyclerView();

        searchQuery = "celebrities";
        fetchArticles(0);
    }

    private void configureRecyclerView() {
        rvResults = binding.search.rvResults;
        articles = new ArrayList<>();
        adapter = new SearchResultsAdapter(this, articles);
        // Attach the adapter to the recyclerview to populate items
        rvResults.setAdapter(adapter);
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        // Attach the layout manager to the recycler view to position the items
        rvResults.setLayoutManager(layoutManager);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Append new items to the bottom of the list
                loadMoreArticles(page);
            }
        };
        // Attach the Scroll Listener to the Recycler View
        rvResults.addOnScrollListener(scrollListener);

        // register listener for grid on click
        ItemClickSupport.addTo(rvResults).setOnItemClickListener(
                (recyclerView, position, v) -> {
                    // get the article to display
                    Article article = articles.get(position);
                    // create an intent to display the article
                    Intent intent = new Intent(SearchActivity.this, ArticleActivity.class);
                    // pass in that article into the intent
                    intent.putExtra("article", Parcels.wrap(article));
                    // launch the activity
                    startActivity(intent);
                }
        );
    }

    // This method sends out a network request and appends new data items to your adapter.
    public void loadMoreArticles(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the list of items
        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
        fetchArticles(offset);
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
                // 1. First, clear the list of records
                articles.clear();
                // 2. Notify the adapter of the update
                adapter.notifyItemRangeRemoved(0, adapter.getItemCount());
                // 3. Reset endless scroll listener when performing a new search
                scrollListener.resetState();

                searchQuery = query;
                fetchArticles(0);
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
            FilterSettingsFragment filterDialog = FilterSettingsFragment.newInstance("Filters", filter);
            filterDialog.setFilterSettingsDialogListener(selectedFiler -> filter = selectedFiler);
            FragmentManager fragmentManager = getSupportFragmentManager();
            filterDialog.show(fragmentManager, "Filter Settings");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchArticles(int page) {
        if (isConnected(this)) {
            new ArticleApiClient().searchArticles(searchQuery, filter, page, new Callback<ResponseWrapper>() {
                @Override
                public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                    if (response.isSuccessful()) {
                        int currentSize = adapter.getItemCount();
                        articles.addAll(response.body().getResponse().getDocs());
                        adapter.notifyItemRangeInserted(currentSize, articles.size() - currentSize);
                        Log.d("DEBUG", articles.toString());
                    } else {
                        Log.d("DEBUG", response.errorBody().toString());
                        Toast.makeText(SearchActivity.this, "Error occurred while retrieving articles!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                    if (call.isCanceled()) {
                        Toast.makeText(SearchActivity.this, "Request Cancelled!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("DEBUG", t.getLocalizedMessage());
                        Toast.makeText(SearchActivity.this, "Error occurred while retrieving articles.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

}