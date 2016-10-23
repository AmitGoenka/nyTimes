package org.agoenka.nytimes.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.agoenka.nytimes.R;
import org.agoenka.nytimes.helpers.DynamicHeightImageView;
import org.agoenka.nytimes.models.Article;
import org.agoenka.nytimes.utils.PicassoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: agoenka
 * Created At: 10/23/2016
 * Version: ${VERSION}
 */
// This is a basic adapter extending from RecyclerView.Adapter
// Note that the custom ViewHolder is specified which gives access to the views
public class SearchResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Store a member variable for the articles
    private List<Article> mArticles;
    // Store the context for easy access
    private Context mContext;
    private Picasso picasso;
    // Identifiers to classify te view type
    private final int STANDARD = 0, TEXTONLY = 1;

    // Pass in the articles list into the constructor
    public SearchResultsAdapter(Context context, List<Article> articles) {
        mArticles = articles;
        mContext = context;
    }

    // Easy access to the context object in the RecyclerView
    private Context getContext() {
        return mContext;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (!TextUtils.isEmpty(mArticles.get(position).getThumbnail()))
            return STANDARD;
        else
            return TEXTONLY;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        RecyclerView.ViewHolder holder = null;

        switch (viewType) {
            case STANDARD:
                view = inflater.inflate(R.layout.item_article_result, parent, false);
                holder = new StandardViewHolder(view);
                break;
            case TEXTONLY:
                view = inflater.inflate(R.layout.item_article_noimage, parent, false);
                holder = new TextOnlyViewHolder(view);
                break;
        }

        // Return a new holder instance
        return holder;
    }

    // Involves populating data into the item through holder
    // Set item views based on views and data model
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // get the data item for position
        Article article = mArticles.get(position);

        switch (holder.getItemViewType()) {
            case STANDARD:
                StandardViewHolder standardViewHolder = (StandardViewHolder) holder;
                configureStandardViewHolder(standardViewHolder, article);
                break;
            case TEXTONLY:
                TextOnlyViewHolder textOnlyViewHolder = (TextOnlyViewHolder) holder;
                configureTextOnlyViewHolder(textOnlyViewHolder, article);
                break;
        }
    }

    private void configureStandardViewHolder(StandardViewHolder holder, Article article) {
        holder.ivTitle.setText(article.getHeadline());
        // clear out recycled image from last time
        holder.ivThumbnail.setImageResource(0);

        if (picasso == null) picasso = PicassoUtils.newInstance(getContext());

        // populate the thumbnail image
        // remote download the image in the background
        picasso.load(article.getThumbnail())
                .error(R.mipmap.ic_launcher)
                .into(holder);
    }

    private void configureTextOnlyViewHolder(TextOnlyViewHolder holder, Article article) {
        holder.ivTitle.setText(article.getHeadline());
    }

    // A direct reference is provided to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    static class StandardViewHolder extends RecyclerView.ViewHolder implements Target {
        // Holder should contain member variables for any view that will be set as a row is rendered
        @BindView(R.id.ivThumbnail) DynamicHeightImageView ivThumbnail;
        @BindView(R.id.ivTitle) TextView ivTitle;

        // A constructor is also created that accepts the entire item row
        // and does the view lookups to find each subview
        StandardViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            // Calculate the image ratio of the loaded bitmap
            float ratio = (float) bitmap.getHeight() / (float) bitmap.getWidth();
            Log.d("DEBUG", "Width: " + bitmap.getWidth() + ", Height: " + bitmap.getHeight());
            // Set the ratio for the image
            ivThumbnail.setHeightRatio(ratio);
            // Load the image into the view
            ivThumbnail.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d("DEBUG", "In onBitmapFailed");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.d("DEBUG", "In onPrepareLoad");
        }
    }

    static class TextOnlyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivTitle) TextView ivTitle;

        TextOnlyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
