package com.example.android.sweetrecipes;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.sweetrecipes.adapter.IngredientAdapter;
import com.example.android.sweetrecipes.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.sweetrecipes.RecipeDetailsActivity.INGREDIENTS_LIST_EXTRA;


/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment {

    List<Ingredient> mIngredients;
    IngredientAdapter mAdapter;
    RecyclerView mRecyclerView;


    public IngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null){
            mIngredients = savedInstanceState.getParcelableArrayList(INGREDIENTS_LIST_EXTRA);
        } else {
            if (mIngredients == null) {
                mIngredients = extractIngredientsFromIntent();
            }
        }
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ingredient, container, false);

        mRecyclerView = rootView.findViewById(R.id.ingredients_list_rv);
        mAdapter = new IngredientAdapter(getActivity(), mIngredients);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    private List<Ingredient> extractIngredientsFromIntent() {
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(INGREDIENTS_LIST_EXTRA)) {
            return intent.getParcelableArrayListExtra(INGREDIENTS_LIST_EXTRA);
        }
        return null;
    }

    public void setIngredients(List<Ingredient> ingredients){
        this.mIngredients = ingredients;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(INGREDIENTS_LIST_EXTRA, (ArrayList<? extends Parcelable>) mIngredients);
    }
}
