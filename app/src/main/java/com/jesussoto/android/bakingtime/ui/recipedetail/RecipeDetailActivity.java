package com.jesussoto.android.bakingtime.ui.recipedetail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.jesussoto.android.bakingtime.R;
import com.jesussoto.android.bakingtime.db.entity.BakingStep;
import com.jesussoto.android.bakingtime.db.entity.Recipe;
import com.jesussoto.android.bakingtime.repository.RecipeRepository;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    public static Intent getStartIntent(Context launching, long recipeId) {
        Intent startIntent = new Intent(launching, RecipeDetailActivity.class);
        startIntent.putExtra(EXTRA_RECIPE_ID, recipeId);
        return startIntent;
    }

    public static void start(FragmentActivity launching, long recipeId) {
        launching.startActivity(getStartIntent(launching, recipeId));
    }

    @Inject
    RecipeRepository mRepository;

    private boolean mTwoPane = false;

    private RecipeDetailViewModel mViewModel;

    private long mRecipeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mViewModel = getViewModel();
        mViewModel.getSelectedStep().observe(this, this::navigateToStep);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.detail_container, RecipeDetailFragment.newInstance())
                    .commitNow();
        }

        mTwoPane = findViewById(R.id.step_container) != null;
    }

    private RecipeDetailViewModel getViewModel() {
        mRecipeId = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1L);
        RecipeDetailViewModelFactory factory =  new RecipeDetailViewModelFactory(mRecipeId, mRepository);
        return ViewModelProviders.of(this, factory).get(RecipeDetailViewModel.class);
    }

    private void navigateToStep(BakingStep step) {
        if (mTwoPane) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.step_container, RecipeStepFragment.newInstance(step))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_container, RecipeStepPagerFragment.newInstance(mRecipeId, step.getStepNumber()))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit();

        }
    }
}
