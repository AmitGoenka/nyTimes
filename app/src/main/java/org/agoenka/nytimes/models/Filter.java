package org.agoenka.nytimes.models;

import android.content.Context;
import android.widget.CheckBox;

import org.agoenka.nytimes.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.agoenka.nytimes.utils.DateUtils.FORMAT_MMDDYY;
import static org.agoenka.nytimes.utils.DateUtils.FORMAT_YYYYMMDD;
import static org.agoenka.nytimes.utils.DateUtils.formatDate;
import static org.agoenka.nytimes.utils.DateUtils.getDate;

/**
 * Author: agoenka
 * Created At: 10/22/2016
 * Version: ${VERSION}
 */

public class Filter implements Serializable {

    private Date beginDate;
    private String sortOrder;
    private List<String> newsDesks;

    public Filter(String beginDate, String sortOrder, List<String> newsDesks) {
        this.beginDate = getDate(beginDate, FORMAT_MMDDYY);
        this.sortOrder = sortOrder;
        this.newsDesks = newsDesks;
    }

    public static List<String> getNewsDesks (final Context context, final CheckBox arts, final CheckBox fashionStyle, final CheckBox sports) {
        List<String> newsDesks = new ArrayList<>();
        if (arts.isChecked()) {
            newsDesks.add(context.getResources().getString(R.string.arts));
        }
        if (fashionStyle.isChecked()) {
            newsDesks.add(context.getResources().getString(R.string.fashion_style));
        }
        if (sports.isChecked()) {
            newsDesks.add(context.getResources().getString(R.string.sports));
        }
        return newsDesks;
    }

    public String getBeginDate() {
        return formatDate(beginDate, FORMAT_YYYYMMDD);
    }

    public String getNewsDesks() {
        if (newsDesks != null && newsDesks.size() > 0) {
            StringBuilder target = new StringBuilder();
            for (String newsDesk: newsDesks) {
                target.append(newsDesk).append(" ");
            }
            return target.toString().trim();
        }
        else {
            return "";
        }
    }

    public String getSortOrder() {
        return sortOrder;
    }
}
