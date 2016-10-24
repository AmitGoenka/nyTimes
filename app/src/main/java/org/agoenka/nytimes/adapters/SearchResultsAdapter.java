package org.agoenka.nytimes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import org.agoenka.nytimes.R;
import org.agoenka.nytimes.databinding.ItemArticleNoimageBinding;
import org.agoenka.nytimes.databinding.ItemArticleResultBinding;
import org.agoenka.nytimes.helpers.DynamicHeightImageView;
import org.agoenka.nytimes.models.Article;

import java.util.List;

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
                standardViewHolder.binding.setArticle(article);
                standardViewHolder.binding.executePendingBindings();
                configureStandardViewHolder(standardViewHolder, article);
                break;
            case TEXTONLY:
                TextOnlyViewHolder textOnlyViewHolder = (TextOnlyViewHolder) holder;
                textOnlyViewHolder.binding.setArticle(article);
                textOnlyViewHolder.binding.executePendingBindings();
                break;
        }
    }

    private void configureStandardViewHolder(StandardViewHolder holder, Article article) {
        // clear out recycled image from last time
        holder.binding.ivThumbnail.setImageResource(0);

        // populate the thumbnail image
        // remote download the image in the background
        Glide.with(getContext())
                .load(article.getThumbnail())
                .error(R.mipmap.ic_launcher)
                .into(holder.binding.ivThumbnail);
    }

    // A direct reference is provided to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    static class StandardViewHolder extends RecyclerView.ViewHolder {

        // this will be used by onBindViewHolder()
        final ItemArticleResultBinding binding;

        ViewTarget<DynamicHeightImageView, GlideDrawable> viewTarget;

        // A constructor is also created that accepts the entire item row
        // and does the view lookups to find each subview
        StandardViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            // Since the layout was already inflated within onCreateViewHolder(),
            // we can use this bind() method to associate the layout variables with the layout.
            binding = ItemArticleResultBinding.bind(itemView);

            initializeViewTarget(binding.ivThumbnail);
        }

        void initializeViewTarget(DynamicHeightImageView view) {
            viewTarget = new ViewTarget<DynamicHeightImageView, GlideDrawable>(view) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    // Calculate the image ratio of the loaded bitmap
                    float ratio = (float) resource.getBounds().height() / (float) resource.getBounds().width();
                    Log.d("DEBUG", "Width: " + resource.getBounds().width() + ", Height: " + resource.getBounds().height());
                    // Set the ratio for the image
                    binding.ivThumbnail.setHeightRatio(ratio);
                    // Load the image into the view
                    binding.ivThumbnail.setImageDrawable(resource.getCurrent());
                }
            };
        }
    }

    static class TextOnlyViewHolder extends RecyclerView.ViewHolder {

        final ItemArticleNoimageBinding binding;

        TextOnlyViewHolder(View itemView) {
            super(itemView);
            binding = ItemArticleNoimageBinding.bind(itemView);
        }
    }
}
