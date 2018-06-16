package com.example.android.sweetrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import com.example.android.sweetrecipes.model.Ingredient;
import com.example.android.sweetrecipes.model.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity implements DetailsMasterListFragment.OnDetailClickListener {

    public static final String INGREDIENTS_LIST_EXTRA = "ingredients_list_extra";
    public static final String STEP_EXTRA = "step_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
    }


    @Override
    public void onDetailIngredientSelected(List<Ingredient> ingredients) {
        // Launch activity for displaying the list of ingredients
        Intent ingredientsIntent = new Intent(RecipeDetailsActivity.this, IngredientsActivity.class);
        ingredientsIntent.putParcelableArrayListExtra(INGREDIENTS_LIST_EXTRA, (ArrayList<? extends Parcelable>) ingredients);
        startActivity(ingredientsIntent);
    }

    @Override
    public void onDetailStepSelected(Step step) {
        // Launch the activity for displaying the step
        Intent stepIntent = new Intent(RecipeDetailsActivity.this, StepActivity.class);
        stepIntent.putExtra(STEP_EXTRA, step);
        startActivity(stepIntent);
    }
}
