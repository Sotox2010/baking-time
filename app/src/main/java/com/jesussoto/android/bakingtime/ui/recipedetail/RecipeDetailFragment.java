package com.jesussoto.android.bakingtime.ui.recipedetail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jesussoto.android.bakingtime.R;
import com.jesussoto.android.bakingtime.api.Resource;
import com.jesussoto.android.bakingtime.db.entity.BakingStep;
import com.jesussoto.android.bakingtime.db.entity.Ingredient;
import com.jesussoto.android.bakingtime.db.entity.Recipe;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeDetailFragment extends Fragment
        implements BakingStepViewHolder.OnStepSelectedListener {

    public static RecipeDetailFragment newInstance() {
        return new RecipeDetailFragment();
    }

    @BindView(R.id.recipe_name)
    TextView mRecipeNameView;

    @BindView(R.id.servings)
    TextView mServingsView;

    @BindView(R.id.ingredients)
    TextView mIngredientsView;

    @BindView(R.id.steps_container)
    LinearLayout mStepsContainer;

    private RecipeDetailViewModel mViewModel;

    private Unbinder mUnbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(requireActivity()).get(RecipeDetailViewModel.class);
        mViewModel.getRecipe().observe(this, this::updateRecipe);
        mViewModel.getIngredients().observe(this, this::fillIngredients);
        mViewModel.getBakingSteps().observe(this, this::fillSteps);
    }

    private void updateRecipe(@Nullable Resource<Recipe> recipeResource) {
        if (recipeResource == null || recipeResource.getStatus() != Resource.Status.SUCCESS) {
            return;
        }

        Recipe recipe = recipeResource.getData();
        mRecipeNameView.setText(recipe.getName());
        mServingsView.setText(getString(R.string.recipe_servings, recipe.getServings()));
    }

    private void fillIngredients(Resource<List<Ingredient>> resource) {
        if (resource == null || resource.getStatus() != Resource.Status.SUCCESS) {
            return;
        }

        List<Ingredient> ingredients = resource.getData();
        SpannableStringBuilder builder = new SpannableStringBuilder();
        int gapWidth = getResources().getDimensionPixelSize(R.dimen.spacing_large);
        int bulletColor = mIngredientsView.getCurrentTextColor();
        int bulletRadius = 7;
        int spanFlags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        int start = 0;

        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            BulletSpan span = new BulletSpan(gapWidth, bulletColor, bulletRadius);
            builder.append(buildIngredientText(ingredient, i < ingredients.size() - 1));
            builder.setSpan(span, start, builder.length(), spanFlags);
            start = builder.length();
        }

        mIngredientsView.setText(builder);
    }

    private String buildIngredientText(@NonNull Ingredient ingredient, boolean insertBreak) {
        int whole = (int) ingredient.getQuantity();
        float fraction = ingredient.getQuantity() - whole;
        StringBuilder sb = new StringBuilder();

        if (whole > 0) {
            sb.append(whole);
            sb.append(' ');
        }

        if (fraction > 0) {
            sb.append(getMeasurementByFraction(fraction));
            sb.append(' ');
        }

        sb.append(ingredient.getMeasure());
        sb.append(getString(R.string.ingredient_of));
        sb.append(ingredient.getIngredient());

        if (insertBreak) {
            sb.append('\n');
        }

        return sb.toString();
    }

    private String getMeasurementByFraction(float fraction) {
        if (fraction == 0.25) {
            return getString(R.string.one_quarter);
        } else if (fraction == 0.50) {
            return getString(R.string.one_half);
        } else if (fraction == 0.75) {
            return getString(R.string.three_quarters);
        } else {
            return "";
        }
    }

    private void fillSteps(Resource<List<BakingStep>> resource) {
        mStepsContainer.removeAllViews();

        if (resource == null || resource.getStatus() != Resource.Status.SUCCESS) {
            return;
        }

        List<BakingStep> steps = resource.getData();
        Collections.sort(steps, (o1, o2) -> o1.getStepNumber() - o2.getStepNumber());
        for (BakingStep step : steps) {
            BakingStepViewHolder holder = BakingStepViewHolder.create(mStepsContainer, this);
            holder.bindStep(step);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void onStepSelected(BakingStep step) {
        mViewModel.selectStep(step);
    }
}
