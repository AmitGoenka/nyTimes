package org.agoenka.nytimes.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

/**
 * Author: agoenka
 * Created At: 10/24/2016
 * Version: ${VERSION}
 */
public class ArticleDeserializer implements JsonDeserializer<Article> {
    @Override
    public Article deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Article article = new Article();

        article.webUrl = json.getAsJsonObject().get("web_url").getAsString();
        article.headline = json.getAsJsonObject().get("headline").getAsJsonObject().get("main").getAsString();
        article.multimediaList = context.deserialize(json.getAsJsonObject().get("multimedia"), new TypeToken<List<Multimedia>>() {}.getType());

        if (article.multimediaList.size() > 0) {
            int selectedImage = new Random().nextInt(article.multimediaList.size());
            String multimediaUrl = article.multimediaList.get(selectedImage).getUrl();
            article.thumbnail = "http://www.nytimes.com/" + multimediaUrl;
        } else {
            article.thumbnail = "";
        }

        return article;
    }
}