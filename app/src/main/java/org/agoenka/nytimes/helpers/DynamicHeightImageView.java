package org.agoenka.nytimes.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Author: agoenka
 * Created At: 10/23/2016
 * Version: ${VERSION}
 */

/**
 * An {@link android.widget.ImageView} layout that maintains a consistent width to height aspect ratio.
 * Sources: {@link 'https://github.com/etsy/AndroidStaggeredGrid/blob/master/library/src/main/java/com/etsy/android/grid/util/DynamicHeightImageView.java'}
 */
public class DynamicHeightImageView extends ImageView {

    private double mHeightRatio;

    public DynamicHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightImageView(Context context) {
        super(context);
    }

    public void setHeightRatio(double ratio) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            requestLayout();
        }
    }

    @SuppressWarnings("unused")
    public double getHeightRatio() {
        return mHeightRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightRatio > 0.0) {
            // set the image views size
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * mHeightRatio);
            setMeasuredDimension(width, height);
        }
        else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
