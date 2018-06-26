package com.example.android.sweetrecipes;


import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.sweetrecipes.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.sweetrecipes.RecipeDetailsActivity.STEPS_LIST_EXTRA;
import static com.example.android.sweetrecipes.RecipeDetailsActivity.STEP_INDEX_EXTRA;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment implements ExoPlayer.EventListener {

    // Constants
    private static final String APPLICATION_NAME = "SweetRecipes";
    private static final String STEP_KEY = "step_key";
    private static final String STEPS_LIST_KEY = "steps_list_key";
    private static final String STEP_INDEX_KEY = "step_index_key";
    private static final String IS_TWO_PANE_KEY = "is_two_pane_key";
    private static final String PLAYER_POSITION_KEY = "player_position_key";
    private static final String PLAYER_STATE_KEY = "get_player_state";
    private static final String LOG_TAG = StepFragment.class.getSimpleName();

    // Member variables
    private List<Step> mSteps;
    private int mStepIndex;
    private Step mStep;
    private Long mPlayerPosition;
    private boolean mGetPlayerStateWhenReady;
    private TextView mDescription;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private Button mPreviousStepButton;
    private Button mNextStepButton;
    private boolean mTwoPane;

    public StepFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(STEP_KEY);
            mSteps = savedInstanceState.getParcelableArrayList(STEPS_LIST_KEY);
            mStepIndex = savedInstanceState.getInt(STEP_INDEX_KEY);
            mTwoPane = savedInstanceState.getBoolean(IS_TWO_PANE_KEY);
            mPlayerPosition = savedInstanceState.getLong(PLAYER_POSITION_KEY);
            Log.d(LOG_TAG, ">>>>>>>>>> PLAYER POSITION IS: " + mPlayerPosition);
            // mGetPlayerStateWhenReady = savedInstanceState.getBoolean(PLAYER_STATE_KEY);
        } else {
            if (mSteps == null) {
                // check if we get the list<Steps> and initialize data
                initializeStepFromIntent();
            }
        }
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        mPlayerView = rootView.findViewById(R.id.step_player_view);
        mDescription = rootView.findViewById(R.id.step_description);
        mPreviousStepButton = rootView.findViewById(R.id.step_previous_button);
        mNextStepButton = rootView.findViewById(R.id.step_next_button);

        // Launch the video player if there's a video to play
        launchPlayer();

        // Initialize the text description
        initializeTextDescription();

        // Initialize step navigation buttons
        mPreviousStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPreviousStep();
            }
        });

        mNextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNextStep();
            }
        });

        if (mTwoPane) {
            hideButtons();
        }

        return rootView;
    }

    private void initializeTextDescription() {
        if (mStep != null) {
            mDescription.setText(mStep.getDescription());
        }
    }

    private void showNextStep() {
        if (mSteps != null) {
            if (mStepIndex < mSteps.size() - 1) {
                mStepIndex += 1;
                mStep = mSteps.get(mStepIndex);
                releasePlayer();
                launchPlayer();
                initializeTextDescription();
            }
        }
    }

    private void showPreviousStep() {
        if (mSteps != null) {
            if (mStepIndex > 0) {
                mStepIndex -= 1;
                mStep = mSteps.get(mStepIndex);
                releasePlayer();
                launchPlayer();
                initializeTextDescription();
            }
        }
    }

    private void showButtons(){
        mPreviousStepButton.setVisibility(View.VISIBLE);
        mNextStepButton.setVisibility(View.VISIBLE);
    }

    public void hideButtons(){
        mPreviousStepButton.setVisibility(View.GONE);
        mNextStepButton.setVisibility(View.GONE);
    }

    private void launchPlayer() {
        if (mStep != null) {
            if (mStep.getVideoURL() != null && !TextUtils.isEmpty(mStep.getVideoURL())) {
                Uri mediaUri = Uri.parse(mStep.getVideoURL());
                initializePlayer(mediaUri);
            }
        }
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), APPLICATION_NAME);
            MediaSource mediaSource = new ExtractorMediaSource(
                    mediaUri,
                    new DefaultDataSourceFactory(getActivity(), userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    /**
     * Inspired from https://stackoverflow.com/questions/46713761/how-to-play-video-full-screen-in-landscape-using-exoplayer
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mExoPlayer.setPlayWhenReady(false);
            mDescription.setVisibility(View.GONE);
            hideButtons();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mPlayerView.setLayoutParams(params);
            hideSystemUI();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mExoPlayer.setPlayWhenReady(false);
            mDescription.setVisibility(View.VISIBLE);
            showButtons();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = 0;
            params.weight = 4;
            mPlayerView.setLayoutParams(params);
            showSystemUI();
        }
    }

    /**
     * Enables regular immersive mode.
     *
     * credit: https://developer.android.com/training/system-ui/immersive
     */
    private void hideSystemUI() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    /**
     * Shows the system bars by removing all the flags
     * except for the ones that make the content appear under the system bars.
     *
     * credit: https://developer.android.com/training/system-ui/immersive
     */
    private void showSystemUI() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void initializeStepFromIntent() {
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(STEPS_LIST_EXTRA) && intent.hasExtra(STEP_INDEX_EXTRA)) {
            mSteps = intent.getParcelableArrayListExtra(STEPS_LIST_EXTRA);
            mStepIndex = intent.getIntExtra(STEP_INDEX_EXTRA, 0);
            mTwoPane = false;
        }

        if (mSteps != null) {
            mStep = mSteps.get(mStepIndex);
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.d(LOG_TAG, ">>>>>>>>>>>> playWhenReady: " + playWhenReady);
        Log.d(LOG_TAG, ">>>>>>>>>>>> playbackState: " + playbackState);
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExoPlayer != null) releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEP_KEY, mStep);
        outState.putParcelableArrayList(STEPS_LIST_KEY, (ArrayList<? extends Parcelable>) mSteps);
        outState.putInt(STEP_INDEX_KEY, mStepIndex);
        outState.putBoolean(IS_TWO_PANE_KEY, mTwoPane);
        outState.putLong(PLAYER_POSITION_KEY, mExoPlayer.getCurrentPosition());
        // outState.putBoolean(PLAYER_STATE_KEY, mExoPlayer.getPlayWhenReady());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) mExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer != null) mExoPlayer.setPlayWhenReady(true);
    }

    public void setSteps(List<Step> steps) {
        this.mSteps = steps;
    }

    public void setStepIndex(int stepIndex) {
        this.mStepIndex = stepIndex;
    }

    public void setStep() {
        if (mSteps != null) {
            this.mStep = mSteps.get(mStepIndex);
        }
    }

    public void setIsTwoPane(boolean isTwoPane) {
        this.mTwoPane = isTwoPane;
    }
}
