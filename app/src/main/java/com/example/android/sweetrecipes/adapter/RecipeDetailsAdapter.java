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
import com.example.android.sweetrecipes.model.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecipeDetailsAdapter.RecipeDetailsViewHolder> {

    private List<Step> mSteps;
    private List<Ingredient> mIngredients;
    private Context mContext;

    public interface RecipeDetailsAdapterListener {
        void onIngredientsSelected(List<Ingredient> ingredients);
        void onStepSelected(Step step);
    }

    private RecipeDetailsAdapterListener mListener;

    public RecipeDetailsAdapter(Context context, List<Ingredient> ingredients, List<Step> steps,
                                RecipeDetailsAdapterListener listener) {
        this.mContext = context;
        mSteps = new ArrayList<>();
        mIngredients = new ArrayList<>();
        this.mIngredients = ingredients;
        this.mSteps = steps;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecipeDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_details_item, parent, false);
        return new RecipeDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailsViewHolder holder, int position) {
        if (position == 0) {
            holder.mDetailName.setText(mContext.getString(R.string.recipe_ingredients_label));
        } else {
            if (position > 0) {
                Step step = mSteps.get(position - 1);
                if (step != null) {
                    holder.mDetailName.setText(step.getShortDescription());
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mSteps.size() + 1;
    }

    public class RecipeDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mDetailName;

        public RecipeDetailsViewHolder(View itemView) {
            super(itemView);
            mDetailName = itemView.findViewById(R.id.recipe_details_item_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position == 0) {
                mListener.onIngredientsSelected(mIngredients);
            } else {
                mListener.onStepSelected(mSteps.get(position - 1));
            }
        }
    }
}
