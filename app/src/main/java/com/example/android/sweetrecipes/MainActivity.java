package com.example.android.sweetrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.sweetrecipes.adapter.RecipeAdapter;
import com.example.android.sweetrecipes.loader.RecipesLoader;
import com.example.android.sweetrecipes.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>>,
        RecipeAdapter.RecipeAdapterListener{

    // Constants
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 101;
    public static final String RECIPE_EXTRA = "recipe_extra";

    // Member variables
    private List<Recipe> mRecipes = new ArrayList<>();
    private RecipeAdapter mAdapter;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get reference to the views
        mRecyclerView = findViewById(R.id.recipe_list_rv);

        // Create an adapter
        mAdapter = new RecipeAdapter(this, mRecipes, this);

        // Create a layout manager
        /*LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);*/

        GridLayoutManager layoutManager = new GridLayoutManager(this, R.integer.nb_of_colum_recipe_list,
                GridLayoutManager.VERTICAL, false);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        // Initialize the loader
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    // ############################
    // [START - LOADER FOR RECIPES]
    // ############################
    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {
        return new RecipesLoader(this);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> data) {
        mRecipes.clear();
        mRecipes.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) {

    }
    // ##########################
    // [END - LOADER FOR RECIPES]
    // ##########################

    @Override
    public void onRecipeSelected(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, RecipeDetailsActivity.class);
        intent.putExtra(RECIPE_EXTRA, recipe);
        startActivity(intent);
    }
}
