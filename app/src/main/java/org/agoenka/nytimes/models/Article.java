package org.agoenka.nytimes.models;

import org.parceler.Parcel;

import java.util.List;

/**
 * Author: agoenka
 * Created At: 10/21/2016
 * Version: ${VERSION}
 */

@Parcel(analyze = Article.class)
public class Article {

    String webUrl;
    String headline;
    String thumbnail;

    transient List<Multimedia> multimediaList;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Article() {}

}