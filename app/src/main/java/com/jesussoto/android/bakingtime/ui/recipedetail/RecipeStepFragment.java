package com.jesussoto.android.bakingtime.ui.recipedetail;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
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
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.jesussoto.android.bakingtime.R;
import com.jesussoto.android.bakingtime.db.entity.BakingStep;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

public class RecipeStepFragment extends Fragment {

    /**
     * Fragment arguments.
     */
    private static final String ARG_STEP = "arg_step";

    /**
     * State restoration constants.
     */
    private static final String STATE_PLAYER_WINDOW_INDEX = "state_player_window";
    private static final String STATE_PLAYER_PLAYBACK_POSITION = "state_player_playback_position";
    private static final String STATE_PLAYER_AUTO_PLAY = "state_player_auto_play";

    public static RecipeStepFragment newInstance(BakingStep step) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_STEP, step);

        RecipeStepFragment fragment = new RecipeStepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    Picasso mPicasso;

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

    private int mCurrentWindowIndex;

    private long mPlaybackPosition;

    private boolean mAutoPlay = false;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
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

        // If we have saved player state, restore it.
        if (savedInstanceState != null) {
            mCurrentWindowIndex = savedInstanceState.getInt(STATE_PLAYER_WINDOW_INDEX, 0);
            mPlaybackPosition = savedInstanceState.getLong(STATE_PLAYER_PLAYBACK_POSITION, 0L);
            mAutoPlay = savedInstanceState.getBoolean(STATE_PLAYER_AUTO_PLAY, false);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT <= 23) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > 23) {
            releasePlayer();
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_PLAYER_WINDOW_INDEX, mCurrentWindowIndex);
        outState.putLong(STATE_PLAYER_PLAYBACK_POSITION, mPlaybackPosition);
        outState.putBoolean(STATE_PLAYER_AUTO_PLAY, mAutoPlay);
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
        mExoPlayer.setPlayWhenReady(mAutoPlay);

        // Resume playback position.
        mExoPlayer.seekTo(mCurrentWindowIndex, mPlaybackPosition);

        MediaSource mediaSource = new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("UserAgent"))
                .createMediaSource(Uri.parse(mVideoUrl));

        mExoPlayer.prepare(mediaSource);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            mCurrentWindowIndex = mExoPlayer.getCurrentWindowIndex();
            mAutoPlay = mExoPlayer.getPlayWhenReady();

            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void bindStep(BakingStep step) {
        mStepNumberView.setText(getString(R.string.step_number,step.getStepNumber()));
        mShortDescriptionView.setText(step.getShortDescription());
        mDescriptionView.setText(step.getDescription());

        boolean isVideoVisible = !TextUtils.isEmpty(step.getVideoUrl());
        boolean isImageVisible = !isVideoVisible && !TextUtils.isEmpty(step.getThumbnailUrl());

        mPlayerContainer.setVisibility(isVideoVisible ? View.VISIBLE : View.GONE);
        mStepImageView.setVisibility(isImageVisible ? View.VISIBLE : View.GONE);
        mVideoUrl = step.getVideoUrl();

        if (isImageVisible) {
            loadImage(step.getThumbnailUrl());
        }
    }

    private void loadImage(String imageUrl) {
        mPicasso.load(imageUrl).into(mStepImageView);
    }
}
