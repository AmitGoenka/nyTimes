package org.agoenka.nytimes.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.agoenka.nytimes.R;
import org.agoenka.nytimes.databinding.ActivityArticleBinding;
import org.agoenka.nytimes.models.Article;
import org.parceler.Parcels;

public class ArticleActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityArticleBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_article);
        setSupportActionBar(binding.toolbar);

        webView = binding.article.wvArticle;

        Article article = Parcels.unwrap(getIntent().getParcelableExtra("article"));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });
        webView.loadUrl(article.getWebUrl());
        Log.d("DEBUG", "Article Info: Headline: " + article.getHeadline() + ", Thumbnail: " + article.getThumbnail() + ", Url: " + article.getWebUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);

        MenuItem item = menu.findItem(R.id.action_share);
        ShareActionProvider miShare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());

        miShare.setShareIntent(shareIntent);
        return super.onCreateOptionsMenu(menu);
    }

}