package com.jesussoto.android.bakingtime.ui.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jesussoto.android.bakingtime.R;
import com.jesussoto.android.bakingtime.db.entity.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder>
        implements OnRecipeTappedListener {

    @Nullable
    private List<Recipe> mRecipes;

    @Nullable
    private OnRecipeTappedListener mRecipeTappedListener;

    @NonNull
    private Picasso mPicasso;

    RecipesAdapter(@Nullable List<Recipe> recipes, @NonNull Picasso picasso) {
        mRecipes = recipes;
        mPicasso = picasso;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.create(parent, mPicasso,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mRecipes != null) {
            holder.bindRecipe(mRecipes.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return (mRecipes != null) ? mRecipes.size() : 0;
    }

    void replaceData(@Nullable List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    void setOnRecipeTappedListener(@Nullable OnRecipeTappedListener listener) {
        mRecipeTappedListener = listener;
    }

    @Override
    public void onRecipeTapped(Recipe recipe) {
        if (mRecipeTappedListener != null) {
            mRecipeTappedListener.onRecipeTapped(recipe);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_image)
        ImageView mRecipeImage;

        @BindView(R.id.recipe_name)
        TextView mRecipeName;

        @BindView(R.id.recipe_cooking_time)
        TextView mServingsAndSteps;

        private Recipe mRecipe;

        private Picasso mPicasso;

        @Nullable
        private OnRecipeTappedListener mRecipeTappedListener;

        static ViewHolder create(ViewGroup parent, @NonNull Picasso picasso, @Nullable OnRecipeTappedListener listener) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.list_item_recipe, parent, false);
            return new ViewHolder(itemView, picasso, listener);
        }

        ViewHolder(@NonNull View itemView, @NonNull Picasso picasso, @Nullable OnRecipeTappedListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mRecipeTappedListener = listener;
            mPicasso = picasso;
        }

        void bindRecipe(Recipe recipe) {
            mRecipe = recipe;
            mRecipeName.setText(recipe.getName());
            mServingsAndSteps.setText(itemView.getContext().getString(
                    R.string.recipe_servings_grid, recipe.getServings()));

            if (!TextUtils.isEmpty(recipe.getImage())) {
                mPicasso.load(recipe.getImage()).into(mRecipeImage);
            } else {
                mRecipeImage.setImageResource(R.drawable.default_recipe_image);
            }
        }

        @Override
        public void onClick(View v) {
            if (mRecipeTappedListener != null) {
                mRecipeTappedListener.onRecipeTapped(mRecipe);
            }
        }
    }
}
