package org.agoenka.nytimes.utils;

import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.Collection;

/**
 * Author: agoenka
 * Created At: 10/22/2016
 * Version: ${VERSION}
 */

public class AppUtils {

    private AppUtils() {
        //no instance
    }

    public static void setSpinnerToValue(Spinner spinner, String value) {
        SpinnerAdapter adapter = spinner.getAdapter();
        int index = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                index = i;
                break; // terminate loop
            }
        }
        spinner.setSelection(index);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }
}
