package com.jesussoto.android.bakingtime.ui.recipedetail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jesussoto.android.bakingtime.R;
import com.jesussoto.android.bakingtime.api.Resource;
import com.jesussoto.android.bakingtime.db.entity.BakingStep;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeStepPagerFragment extends Fragment {

    private static String ARG_RECIPE_ID = "arg_recipe";
    private static String ARG_STEP_NUMBER = "arg_step_number";

    public static RecipeStepPagerFragment newInstance(@NonNull long recipeId, int stepNumber) {
        Bundle args = new Bundle();
        args.putLong(ARG_RECIPE_ID, recipeId);
        args.putInt(ARG_STEP_NUMBER, stepNumber);

        RecipeStepPagerFragment fragment = new RecipeStepPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.steps_pager)
    ViewPager mStepsPager;

    @BindView(R.id.prev_step_button)
    Button mPrevButton;

    @BindView(R.id.next_step_button)
    Button mNextButton;

    private RecipeDetailViewModel mViewModel;

    private BakingStepPagerAdapter mPagerAdapter;

    private Unbinder mUnbinder;

    private long mRecipeId;

    private int mInitialStepNumber;

    private boolean mIsFreshLaunch = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipeId = getArguments().getLong(ARG_RECIPE_ID, -1L);
        mInitialStepNumber = getArguments().getInt(ARG_STEP_NUMBER);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_pager, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPrevButton.setOnClickListener(v -> previousStep());
        mNextButton.setOnClickListener(v -> nextStep());

        mPagerAdapter = new BakingStepPagerAdapter(getChildFragmentManager(), null);

        mStepsPager.setAdapter(mPagerAdapter);
        mStepsPager.addOnPageChangeListener(mPageChangeListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsFreshLaunch = savedInstanceState == null;

        mViewModel = ViewModelProviders.of(requireActivity()).get(RecipeDetailViewModel.class);
        mViewModel.getBakingSteps().observe(this, this::updateBakingSteps);
    }

    private void updateBakingSteps(Resource<List<BakingStep>> resource) {
        if (resource == null || resource.getStatus() != Resource.Status.SUCCESS) {
            return;
        }

        List<BakingStep> steps = resource.getData();
        mPagerAdapter.replaceData(steps);

        if (mIsFreshLaunch) {
            mStepsPager.setCurrentItem(mInitialStepNumber);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    private void previousStep() {
        int currentItem = mStepsPager.getCurrentItem();
        if (currentItem > 0) {
            mStepsPager.setCurrentItem(mStepsPager.getCurrentItem() - 1);
        }
    }

    private void nextStep() {
        int currentItem = mStepsPager.getCurrentItem();
        if (currentItem < mPagerAdapter.getCount() - 1) {
            mStepsPager.setCurrentItem(mStepsPager.getCurrentItem() + 1);
        }
    }

    static class BakingStepPagerAdapter extends FragmentStatePagerAdapter {
        private List<BakingStep> mBakingSteps;

        public BakingStepPagerAdapter(FragmentManager fm, @NonNull List<BakingStep> bakingSteps) {
            super(fm);
            mBakingSteps = bakingSteps;
        }

        @Override
        public Fragment getItem(int i) {
            return RecipeStepFragment.newInstance(mBakingSteps.get(i));
        }

        @Override
        public int getCount() {
            return mBakingSteps != null ? mBakingSteps.size() : 0;
        }

        void replaceData(List<BakingStep> steps) {
            if (steps != mBakingSteps) {
                mBakingSteps = steps;
                notifyDataSetChanged();
            }
        }
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            mPrevButton.setEnabled(i > 0);
            mNextButton.setEnabled(i < mPagerAdapter.getCount() - 1);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
