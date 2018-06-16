package com.example.android.sweetrecipes.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sweetrecipes.R;
import com.example.android.sweetrecipes.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipes;
    private Context mContext;

    public interface RecipeAdapterListener {
        void onRecipeSelected(Recipe recipe);
    }

    private RecipeAdapterListener mListener;

    public RecipeAdapter(Context context, List<Recipe> recipes, RecipeAdapterListener listener) {
        this.mContext = context;
        mRecipes = new ArrayList<>();
        this.mRecipes = recipes;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        if (recipe != null) {
            // Set the recipe name
            holder.mRecipeName.setText(recipe.getName());
            // Set the recipe image
            if (recipe.getImage() == null || TextUtils.isEmpty(recipe.getImage())) {
                holder.mRecipeImage.setImageResource(R.drawable.cup_cake);
            } else {
                Picasso
                        .get()
                        .load(recipe.getImage())
                        .error(R.drawable.cup_cake)
                        .into(holder.mRecipeImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mRecipeImage;
        private TextView mRecipeName;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            mRecipeImage = itemView.findViewById(R.id.recipe_image_item_iv);
            mRecipeName = itemView.findViewById(R.id.recipe_name_item_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mListener.onRecipeSelected(mRecipes.get(position));
        }
    }
}
