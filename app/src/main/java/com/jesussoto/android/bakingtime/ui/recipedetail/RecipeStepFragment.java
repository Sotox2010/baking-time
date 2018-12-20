package com.jesussoto.android.bakingtime.ui.recipedetail;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.jesussoto.android.bakingtime.R;
import com.jesussoto.android.bakingtime.db.entity.BakingStep;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeStepFragment extends Fragment {

    private static final String ARG_STEP = "arg_step";

    public static RecipeStepFragment newInstance(BakingStep step) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_STEP, step);

        RecipeStepFragment fragment = new RecipeStepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.player_container)
    ViewGroup mPlayerContainer;

    @BindView(R.id.player_view)
    PlayerView mPlayerView;

    @BindView(R.id.step_image)
    ImageView mStepImageView;

    @BindView(R.id.step_number)
    TextView mStepNumberView;

    @BindView(R.id.step_short_description)
    TextView mShortDescriptionView;

    @BindView(R.id.step_description)
    TextView mDescriptionView;

    private SimpleExoPlayer mExoPlayer;

    private String mVideoUrl;

    private Unbinder mUnbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BakingStep step = getArguments().getParcelable(ARG_STEP);
        bindStep(step);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    private void initializePlayer() {
        if (mExoPlayer != null || TextUtils.isEmpty(mVideoUrl)) {
            return;
        }

        TrackSelector trackSelector =  new DefaultTrackSelector();
        RenderersFactory renderersFactory = new DefaultRenderersFactory(requireContext());
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(requireContext(),
                renderersFactory, trackSelector, loadControl);
        mPlayerView.setPlayer(mExoPlayer);

        MediaSource mediaSource = new ExtractorMediaSource.Factory(
                new DefaultDataSourceFactory(requireContext(), "UserAgent"))
                .createMediaSource(Uri.parse(mVideoUrl));
        mExoPlayer.prepare(mediaSource);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void bindStep(BakingStep step) {
        mStepNumberView.setText("Step " + step.getStepNumber());
        mShortDescriptionView.setText(step.getShortDescription());
        mDescriptionView.setText(step.getDescription());

        boolean isVideoVisible = !TextUtils.isEmpty(step.getVideoUrl());
        boolean isImageVisible = !isVideoVisible && !TextUtils.isEmpty(step.getThumbnailUrl());

        mPlayerContainer.setVisibility(isVideoVisible ? View.VISIBLE : View.GONE);
        mStepImageView.setVisibility(isImageVisible ? View.VISIBLE : View.GONE);

        mVideoUrl = step.getVideoUrl();
    }
}
