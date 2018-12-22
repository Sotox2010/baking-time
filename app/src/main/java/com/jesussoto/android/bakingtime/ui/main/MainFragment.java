package com.jesussoto.android.bakingtime.ui.main;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.jesussoto.android.bakingtime.R;
import com.jesussoto.android.bakingtime.db.entity.Recipe;
import com.jesussoto.android.bakingtime.ui.recipedetail.RecipeDetailActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class MainFragment extends Fragment implements OnRecipeTappedListener {

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    @Inject
    Picasso mPicasso;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recipes_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress)
    ProgressBar mProgressBar;

    private RecipesAdapter mAdapter;

    private Unbinder mUnbinder;

    private MainViewModel mViewModel;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        ((MainActivity) context).getIdlingResource().increment();
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();

        ((AppCompatActivity) requireActivity()).setSupportActionBar(mToolbar);

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            mToolbar.post(() -> actionBar.setTitle(null));
        }

        int end = requireActivity().getResources()
                .getDimensionPixelSize(R.dimen.refresh_layout_progress_top);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, end);
        mSwipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainViewModel.class);

        // Subscribe to ui model emissions.
        mViewModel.getUiModel().observe(this, this::updateView);

        // Subscribe to refreshing state emissions.
        mViewModel.getRefreshingState().observe(this, this::updateRefreshState);
    }

    private void setupRecyclerView() {
        int spanCount = getResources().getInteger(R.integer.recipe_grid_span_count);

        mAdapter = new RecipesAdapter(null, mPicasso);
        mAdapter.setOnRecipeTappedListener(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), spanCount));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void updateView(MainUiModel uiModel) {
        CountingIdlingResource resource = ((MainActivity) requireActivity()).getIdlingResource();
        if (uiModel.getRecipes() != null && !uiModel.getRecipes().isEmpty() && !resource.isIdleNow()) {
            resource.decrement();
        }

        int recipeListVisibility = uiModel.isRecipeListVisible() ? View.VISIBLE : View.GONE;
        int loadingVisibility = uiModel.isLoadingVisible() ? View.VISIBLE : View.GONE;

        mAdapter.replaceData(uiModel.getRecipes());
        mRecyclerView.setVisibility(recipeListVisibility);
        mProgressBar.setVisibility(loadingVisibility);
    }

    private void updateRefreshState(boolean isRefreshing) {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(isRefreshing));
    }

    @Override
    public void onRecipeTapped(Recipe recipe) {
        RecipeDetailActivity.start(requireActivity(), recipe.getId());
    }
}
