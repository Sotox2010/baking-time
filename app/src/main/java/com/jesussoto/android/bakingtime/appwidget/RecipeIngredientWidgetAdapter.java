package com.jesussoto.android.bakingtime.appwidget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jesussoto.android.bakingtime.R;
import com.jesussoto.android.bakingtime.db.entity.Recipe;
import com.jesussoto.android.bakingtime.ui.main.OnRecipeTappedListener;

import java.util.List;

public class RecipeIngredientWidgetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements OnRecipeTappedListener {

    private static final int VIEW_TYPE_TITLE = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    @Nullable
    private List<Recipe> mRecipes;

    @Nullable
    private OnRecipeTappedListener mRecipeTappedListener;

    private String mTitleText;

    RecipeIngredientWidgetAdapter(@Nullable List<Recipe> recipes, String titleText) {
        mRecipes = recipes;
        mTitleText = titleText;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return viewType == VIEW_TYPE_TITLE
                ? TitleViewHolder.create(parent)
                : ItemViewHolder.create(parent, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mRecipes != null) {
            if (getItemViewType(position) == VIEW_TYPE_TITLE) {
                ((TitleViewHolder) holder).bindTitle(mTitleText);
            } else {
                ((ItemViewHolder) holder).bindRecipe(mRecipes.get(position - 1));
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mRecipes != null) ? mRecipes.size() + 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_TITLE : VIEW_TYPE_ITEM;
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

    static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Recipe mRecipe;

        @Nullable
        private OnRecipeTappedListener mRecipeTappedListener;

        static ItemViewHolder create(ViewGroup parent, @Nullable OnRecipeTappedListener listener) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.list_item_recipe_appwidget, parent, false);
            return new ItemViewHolder(itemView, listener);
        }

        ItemViewHolder(@NonNull View itemView, @Nullable OnRecipeTappedListener listener) {
            super(itemView);
            itemView.setOnClickListener(this);
            mRecipeTappedListener = listener;
        }

        void bindRecipe(Recipe recipe) {
            mRecipe = recipe;
            ((TextView) itemView).setText(recipe.getName());
        }

        @Override
        public void onClick(View v) {
            if (mRecipeTappedListener != null) {
                mRecipeTappedListener.onRecipeTapped(mRecipe);
            }
        }
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {

        static TitleViewHolder create(ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.view_title, parent, false);
            return new TitleViewHolder(itemView);
        }

        TitleViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bindTitle(String titleText) {
            ((TextView) itemView).setText(titleText);
        }
    }
}
