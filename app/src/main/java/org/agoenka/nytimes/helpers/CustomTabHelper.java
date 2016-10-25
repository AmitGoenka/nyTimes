package org.agoenka.nytimes.helpers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import org.agoenka.nytimes.R;

/**
 * Author: agoenka
 * Created At: 10/24/2016
 * Version: ${VERSION}
 */

public class CustomTabHelper {

    private CustomTabHelper() {
        //no instance
    }

    public static CustomTabsIntent getCustomsTabIntent(Context context, String url) {
        return new CustomTabsIntent.Builder() // Initialize the Custom Tabs Intent Builder
                .setToolbarColor(ContextCompat.getColor(context, R.color.colorAccent)) // Customize with Toolbar color
                .setActionButton(getShareIconBitmap(context), "Share Link", getPendingIntent(context, url), true) // Customize with Action Button
                .build();
    }

    private static Bitmap getShareIconBitmap(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_share);
    }

    private static PendingIntent getPendingIntent(Context context, String url) {
        int requestCode = 100;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);

        return PendingIntent.getActivity(context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}