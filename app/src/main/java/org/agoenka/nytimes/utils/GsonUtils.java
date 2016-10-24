package org.agoenka.nytimes.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.agoenka.nytimes.models.Article;
import org.agoenka.nytimes.models.ArticleDeserializer;
import org.agoenka.nytimes.models.Multimedia;
import org.agoenka.nytimes.models.MultimediaDeserializer;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Author: agoenka
 * Created At: 10/24/2016
 * Version: ${VERSION}
 */

public class GsonUtils {

    private GsonUtils() {
        //no instance
    }

    public static GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Type multimediaListType = new TypeToken<List<Multimedia>>() {}.getType();
        gsonBuilder.registerTypeAdapter(multimediaListType, new MultimediaDeserializer());
        gsonBuilder.registerTypeAdapter(Article.class, new ArticleDeserializer());
        return gsonBuilder;
    }
}
