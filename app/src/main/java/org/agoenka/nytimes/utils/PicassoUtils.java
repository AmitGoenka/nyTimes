package org.agoenka.nytimes.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.squareup.picasso.Picasso;

/**
 * Author: agoenka
 * Created At: 10/22/2016
 * Version: ${VERSION}
 */

@SuppressWarnings("unused")
public class PicassoUtils {

    private PicassoUtils () {}

    public static Picasso newInstance (Context context) {
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Log.d("DEBUG", exception.getLocalizedMessage());
                exception.printStackTrace();
            }
        });
        return builder.build();
    }

}
