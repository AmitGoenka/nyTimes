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

import java.util.List;

/**
 * Author: agoenka
 * Created At: 10/21/2016
 * Version: ${VERSION}
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the data item for position
        Article article = getItem(position);

        // check to see if existing view is being reused
        // not using a recycled view -> inflate the layout
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_article_result, parent, false);
        }
        // find the image view
        ImageView ivThumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);

        // clear out recycled image from convertView from last time
        ivThumbnail.setImageResource(0);

        TextView ivTitle = (TextView) convertView.findViewById(R.id.ivTitle);
        ivTitle.setText(article.getHeadline());

        // populate the thumbnail image
        // remote download the image in the background
        String thumbnail = article.getThumbnail();

        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).into(ivThumbnail);
        }

        return convertView;
    }
}
