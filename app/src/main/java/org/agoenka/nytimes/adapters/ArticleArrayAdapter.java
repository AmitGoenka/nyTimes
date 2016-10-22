package org.agoenka.nytimes.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.agoenka.nytimes.R;
import org.agoenka.nytimes.models.Article;
import org.agoenka.nytimes.utils.PicassoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: agoenka
 * Created At: 10/21/2016
 * Version: ${VERSION}
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    private Picasso picasso;

    static class ViewHolder {
        @BindView(R.id.ivThumbnail) ImageView ivThumbnail;
        @BindView(R.id.ivTitle) TextView ivTitle;

        ViewHolder (View view) {
            ButterKnife.bind(this, view);
        }
    }

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the data item for position
        Article article = getItem(position);
        ViewHolder viewHolder;

        // check to see if existing view is being reused
        // not using a recycled view -> inflate the layout
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_article_result, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // clear out recycled image from convertView from last time
        viewHolder.ivThumbnail.setImageResource(0);
        viewHolder.ivTitle.setText(article.getHeadline());

        // populate the thumbnail image
        // remote download the image in the background
        String thumbnail = article.getThumbnail();
        if (!TextUtils.isEmpty(thumbnail)) {
            if (picasso == null) picasso = PicassoUtils.newInstance(getContext());
            picasso.load(thumbnail)
                    .error(R.mipmap.ic_launcher)
                    .into(viewHolder.ivThumbnail);
        }

        return convertView;
    }
}
