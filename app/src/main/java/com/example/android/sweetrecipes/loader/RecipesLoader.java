package com.example.android.sweetrecipes.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.sweetrecipes.model.Recipe;
import com.example.android.sweetrecipes.utils.NetworkUtils;

import java.util.List;

public class RecipesLoader extends AsyncTaskLoader<List<Recipe>> {

    private static final String LOG_TAG = RecipesLoader.class.getSimpleName();

    public RecipesLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Recipe> loadInBackground() {

        return NetworkUtils.getRecipes();
    }
}
