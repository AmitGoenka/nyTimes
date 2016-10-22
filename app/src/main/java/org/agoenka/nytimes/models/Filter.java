package org.agoenka.nytimes.models;

import android.content.Context;
import android.widget.CheckBox;

import org.agoenka.nytimes.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.agoenka.nytimes.utils.AppUtils.isEmpty;
import static org.agoenka.nytimes.utils.DateUtils.FORMAT_MMDDYY;
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

    public Date getBeginDate() {
        return beginDate;
    }

    public String getBeginDate(String format) {
        return formatDate(beginDate, format);
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public List<String> getNewsDesks() {
        return newsDesks;
    }

    public String getNewsDesks(boolean flattened) {
        if (flattened && !isEmpty((newsDesks))) {
            StringBuilder target = new StringBuilder();
            target.append("news_desk:(");
            for (String newsDesk : newsDesks) {
                target.append(newsDesk).append(" ");
            }
            return target.toString().trim() + ")";
        } else {
            return "";
        }
    }

    public static List<String> getNewsDesks(final Context context, final CheckBox arts, final CheckBox fashionStyle, final CheckBox sports) {
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

    public static void setNewsDesks(final Context context, List<String> newsDesks, final CheckBox arts, final CheckBox fashionStyle, final CheckBox sports) {
        if (newsDesks.contains(context.getResources().getString(R.string.arts))) {
            arts.setChecked(true);
        }
        if (newsDesks.contains(context.getResources().getString(R.string.fashion_style))) {
            fashionStyle.setChecked(true);
        }
        if (newsDesks.contains(context.getResources().getString(R.string.sports))) {
            sports.setChecked(true);
        }
    }

}