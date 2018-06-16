package com.example.android.sweetrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.sweetrecipes.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.sweetrecipes.RecipeDetailsActivity.INGREDIENTS_LIST_EXTRA;

public class IngredientsActivity extends AppCompatActivity {

    List<Ingredient> mIngredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        // check if I get the list<Ingredient>
        mIngredients = extractIngredientsFromIntent();

        if(mIngredients != null) {
            showIngredientsList(mIngredients);
        } else {
            finish();
        }
    }

    private List<Ingredient> extractIngredientsFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(INGREDIENTS_LIST_EXTRA)) {
            return intent.getParcelableArrayListExtra(INGREDIENTS_LIST_EXTRA);
        }
        return null;
    }

    private void showIngredientsList(List<Ingredient> ingredients) {
        
    }
}
