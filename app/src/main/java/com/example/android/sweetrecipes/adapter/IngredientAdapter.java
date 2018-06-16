package com.example.android.sweetrecipes.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sweetrecipes.R;
import com.example.android.sweetrecipes.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private Context mContext;
    private List<Ingredient> mIngredients;

    public IngredientAdapter(Context context, List<Ingredient> ingredients) {
        this.mContext = context;
        mIngredients = new ArrayList<>();
        this.mIngredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = mIngredients.get(position);
        // Set text for each item text views
        if (ingredient != null) {
            holder.mIngredientName.setText(ingredient.getIngredient());
            holder.mIngredientQuantity.setText(String.valueOf(ingredient.getQuantity()));
            holder.mIngredientMeasure.setText(ingredient.getMeasure());
        }
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder{

        private TextView mIngredientName;
        private TextView mIngredientQuantity;
        private TextView mIngredientMeasure;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            mIngredientName = itemView.findViewById(R.id.ingredient_name_tv);
            mIngredientQuantity = itemView.findViewById(R.id.ingredient_quantity_tv);
            mIngredientMeasure = itemView.findViewById(R.id.ingredient_measure);
        }
    }
}
