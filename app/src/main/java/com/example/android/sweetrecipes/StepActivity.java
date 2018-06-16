package com.example.android.sweetrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.sweetrecipes.model.Step;

import static com.example.android.sweetrecipes.RecipeDetailsActivity.STEP_EXTRA;

public class StepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Intent intent = getIntent();
        Step step;
        if (intent.hasExtra(STEP_EXTRA)) {
            step = intent.getParcelableExtra(STEP_EXTRA);
        }
    }
}
