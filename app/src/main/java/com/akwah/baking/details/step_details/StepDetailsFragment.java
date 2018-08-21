package com.akwah.baking.details.step_details;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akwah.baking.R;
import com.akwah.baking.Recipe;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

public class StepDetailsFragment extends Fragment {
    public static final String STEP_KEY = "step_key";
    private static final String POSITION_KEY = "position_key";

    private Recipe.Step mStep;
    private ExoPlayer mExoPlayer;
    private PlayerView playerView;
    private long mStartPosition;

    public StepDetailsFragment() {
    }

    public static StepDetailsFragment newInstance(Recipe.Step step) {
        StepDetailsFragment fragment = new StepDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(STEP_KEY, step);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStep = getArguments().getParcelable(STEP_KEY);
        }
        if (savedInstanceState != null) {
            mStartPosition = savedInstanceState.getLong(POSITION_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        TextView description = ((TextView) rootView.findViewById(R.id.step_description));
        if (description != null)
            description.setText(mStep.getDescription());
        String videoUrl = mStep.getVideoURL();
        String imageUrl = mStep.getThumbnailURL();
        playerView = rootView.findViewById(R.id.step_details_player);
        ImageView imageView = rootView.findViewById(R.id.step_details_image);
        if (videoUrl != null && !videoUrl.equals("")) {
            imageView.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);
        } else if (imageUrl != null && !imageUrl.equals("")) {
            imageView.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
            Picasso.get().load(imageUrl).error(R.drawable.default_food_image).placeholder(R.drawable.default_food_image).into(imageView);
        } else {
            TextView noMedia = rootView.findViewById(R.id.no_media);
            if(noMedia != null)
                noMedia.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (playerView.getVisibility() == View.VISIBLE && mExoPlayer == null)
            initPlayer(Uri.parse(mStep.getVideoURL()));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (playerView.getVisibility() == View.VISIBLE)
            releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STEP_KEY, true);
        updateStartPosition();
        outState.putLong(POSITION_KEY, mStartPosition);

    }

    private void initPlayer(Uri uri) {
        if (mExoPlayer != null)
            return;
        Context context = getContext();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
        playerView.setPlayer(mExoPlayer);
        DataSource.Factory factory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, getString(R.string.app_name)));
        MediaSource mediaSource = new ExtractorMediaSource.Factory(factory).createMediaSource(uri);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.seekTo(mStartPosition);
//        mExoPlayer.setPlayWhenReady(true);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            updateStartPosition();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void updateStartPosition() {
        if (mExoPlayer != null) {
            mStartPosition = Math.max(0, mExoPlayer.getContentPosition());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

}
