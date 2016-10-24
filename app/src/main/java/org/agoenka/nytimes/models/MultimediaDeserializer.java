package org.agoenka.nytimes.models;

import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: agoenka
 * Created At: 10/23/2016
 * Version: ${VERSION}
 */

public class MultimediaDeserializer implements JsonDeserializer<List<Multimedia>> {

    @Override
    public List<Multimedia> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Multimedia> multimediaList = new ArrayList<>();
        String jsonString = null;

        try {
            jsonString = json.getAsString();
        } catch (IllegalStateException | UnsupportedOperationException e) {
           // Do Nothing
        }

        if (jsonString == null || !TextUtils.isEmpty(jsonString)) {
            JsonArray jsonArray = json.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                multimediaList.add(context.deserialize(jsonArray.get(i), Multimedia.class));
            }
        }

        return multimediaList;
    }
}
