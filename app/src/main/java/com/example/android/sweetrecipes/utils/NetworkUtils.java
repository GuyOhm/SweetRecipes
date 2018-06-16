package com.example.android.sweetrecipes.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.android.sweetrecipes.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {

    private static final String BAKING_RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    /**
     * Handle HTTP request and get response from it using OkHttp library
     * http://square.github.io/okhttp/
     *
     * @param url to request
     * @return String as a response
     * @throws IOException if an error occurred
     */
    private static String getResponseFromHttpUrl(URL url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        return (response.body() != null) ? response.body().string() : null;
    }

    /**
     *
     * @param json
     * @return
     */
    private static List<Recipe> deserializeJsonToRecipes(String json){
        Type recipesType = new TypeToken<ArrayList<Recipe>>(){}.getType();
        List<Recipe> recipes = new Gson().fromJson(json, recipesType);

        return recipes;
    }

    /**
     *
     * @return
     */
    public static List<Recipe> getRecipes() {
        String recipesJson;
        List<Recipe> recipes = new ArrayList<>();
        URL url;

        try {
            url = new URL(BAKING_RECIPES_URL);
            recipesJson = getResponseFromHttpUrl(url);
            recipes = deserializeJsonToRecipes(recipesJson);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return recipes;
    }

    /**
     * This method checks network connectivity
     *
     * @return boolean whether or not the device is connected
     */
    public static boolean isNetworkActive(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
