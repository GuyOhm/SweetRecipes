package com.example.android.sweetrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.sweetrecipes.model.Ingredient;
import com.example.android.sweetrecipes.model.Recipe;
import com.example.android.sweetrecipes.model.Step;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.sweetrecipes.MainActivity.RECIPE_EXTRA;

public class RecipeDetailsActivity extends AppCompatActivity implements DetailsMasterListFragment.OnDetailClickListener {

    public static final String INGREDIENTS_LIST_EXTRA = "ingredients_list_extra";
    public static final String STEPS_LIST_EXTRA = "steps_list_extra";
    public static final String STEP_INDEX_EXTRA = "step_index_extra";
    public boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        if (findViewById(R.id.detail_container_fl) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Create an Ingredients fragment
                IngredientsFragment ingredientsFragment = new IngredientsFragment();
                Intent intent = getIntent();
                if (intent.hasExtra(RECIPE_EXTRA)) {
                    Recipe recipe = intent.getParcelableExtra(RECIPE_EXTRA);
                    List<Ingredient> ingredients = recipe.getIngredients();
                    ingredientsFragment.setIngredients(ingredients);
                }
                fragmentManager.beginTransaction()
                        .add(R.id.detail_container_fl, ingredientsFragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }


    @Override
    public void onDetailIngredientSelected(List<Ingredient> ingredients) {

        if (mTwoPane){
            // Replace current fragment by an Ingredients fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setIngredients(ingredients);
            fragmentManager.beginTransaction()
                    .replace(R.id.detail_container_fl, ingredientsFragment)
                    .commit();
        } else {
            // Launch activity for displaying the list of ingredients
            Intent ingredientsIntent = new Intent(RecipeDetailsActivity.this, IngredientsActivity.class);
            ingredientsIntent.putParcelableArrayListExtra(INGREDIENTS_LIST_EXTRA, (ArrayList<? extends Parcelable>) ingredients);
            startActivity(ingredientsIntent);
        }
    }

    @Override
    public void onDetailStepSelected(List<Step> steps, int stepIndex) {
        if (mTwoPane) {
            // Replace current fragment by a Step fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepFragment stepFragment = new StepFragment();
            stepFragment.setSteps(steps);
            stepFragment.setStepIndex(stepIndex);
            stepFragment.setStep();
            stepFragment.setIsTwoPane(true);
            fragmentManager.beginTransaction()
                    .replace(R.id.detail_container_fl, stepFragment)
                    .commit();
        } else {
            // Launch the activity for displaying the step
            Intent stepIntent = new Intent(RecipeDetailsActivity.this, StepActivity.class);
            stepIntent.putExtra(STEP_INDEX_EXTRA, stepIndex);
            stepIntent.putParcelableArrayListExtra(STEPS_LIST_EXTRA, (ArrayList<? extends Parcelable>) steps);
            startActivity(stepIntent);
        }
    }
}
