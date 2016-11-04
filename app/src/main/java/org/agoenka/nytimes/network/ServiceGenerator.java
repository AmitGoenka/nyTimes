package org.agoenka.nytimes.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.agoenka.nytimes.BuildConfig;
import org.agoenka.nytimes.models.Article;
import org.agoenka.nytimes.models.ArticleDeserializer;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.agoenka.nytimes.network.ArticleApiClient.API_BASE_URL;
import static org.agoenka.nytimes.network.ArticleApiClient.API_KEY;
import static org.agoenka.nytimes.network.ArticleApiClient.API_KEY_PARAM;

/**
 * Artist: agoenka
 * Event Date: 9/22/2016.
 */
class ServiceGenerator {

    private ServiceGenerator() {
        //no instance
    }

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(getGson()));

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(newApiKeyInterceptor())
            .addInterceptor(newLoggingInterceptor())
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    static Retrofit retrofit() {
        return builder
                .client(httpClient)
                .build();
    }

    private static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Article.class, new ArticleDeserializer())
                .create();
    }

    private static Interceptor newApiKeyInterceptor() {
        return chain -> {
            Request original = chain.request();

            // Customize the URL
            HttpUrl url = original.url()
                    .newBuilder()
                    .addQueryParameter(API_KEY_PARAM, API_KEY)
                    .build();

            // Customize the Request
            Request request = original.newBuilder()
                    .url(url)
                    .build();

            // Customize or Return the Response
            return chain.proceed(request);
        };
    }

    private static Interceptor newLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG
                ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.BASIC);
    }

}