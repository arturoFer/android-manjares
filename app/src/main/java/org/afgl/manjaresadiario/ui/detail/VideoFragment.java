package org.afgl.manjaresadiario.ui.detail;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.afgl.manjaresadiario.BuildConfig;
import org.afgl.manjaresadiario.R;
import org.afgl.manjaresadiario.ui.about.AboutFragment;
import org.afgl.manjaresadiario.utilities.InjectorUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * A placeholder fragment containing a simple view.
 */
public class VideoFragment extends Fragment implements YouTubePlayer.OnInitializedListener,
        YouTubePlayer.PlayerStateChangeListener,
        YouTubePlayer.OnFullscreenListener,
        View.OnClickListener {

    private static final String VIDEO_ID_EXTRA = "videoId";
    private static final String VIDEO_TIME = "videoTime";

    private static final String URL_SHARE = "https://youtu.be/";
    private static final String CHOOSER_TITLE = "Compartir Receta";
    private static final int PHONE_NUM_COLUMNS = 1;

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private YouTubePlayer mPlayer;
    private String mVideoId;
    private TextView mTitle;
    private TextView mDescription;

    private boolean mInitialized;
    private int mTime;
    private boolean mShared;
    private boolean mWebPressed;
    private boolean mIsFullscreen;
    private boolean mPhone;

    private OnCuedVideoLoadedListener listener;

    public VideoFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnCuedVideoLoadedListener){
            listener = (OnCuedVideoLoadedListener) context;
        } else{
            throw new ClassCastException(context.toString() + "Debes implementar OnButtonWebListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTime = 0;
        mInitialized = false;
        mShared = false;
        mWebPressed = false;
        mIsFullscreen = false;
        if(savedInstanceState != null){
            mTime = savedInstanceState.getInt(VIDEO_TIME);
            mVideoId = savedInstanceState.getString(VIDEO_ID_EXTRA);
        }else if(getActivity() != null){
            mVideoId = getActivity().getIntent().getStringExtra(VIDEO_ID_EXTRA);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPhone = isPhone();
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mTitle = view.findViewById(R.id.title);
        mDescription = view.findViewById(R.id.description);

        ImageButton buttonHome = view.findViewById(R.id.home_button);
        buttonHome.setOnClickListener(this);
        ImageButton shareButton = view.findViewById(R.id.share_button);
        shareButton.setOnClickListener(this);
        // Inserta e inicia el fragmento de YouTube
        insertNestedFragment();
        setupViewModel();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mPhone) forceLandscapePortrait(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        doLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPlayer != null && mInitialized){
            Handler handler = new Handler();
            if(!mShared && !mWebPressed) {
                handler.post(() -> mPlayer.loadVideo(mVideoId, mTime));

            } else{
                mWebPressed = false;
                mShared = false;
                handler.postDelayed(()->mPlayer.play(), 100);
            }
        }
    }

    @Override
    public void onStop() {
        if(mPhone) forceLandscapePortrait(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if(mPlayer != null){
            mPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if(mPlayer != null) mTime = mPlayer.getCurrentTimeMillis();
        outState.putInt(VIDEO_TIME, mTime);
        outState.putString(VIDEO_ID_EXTRA, mVideoId);
        super.onSaveInstanceState(outState);
    }

    private void setupViewModel() {
        Context context = null;
        if(getActivity() != null) {
            context = getActivity().getApplicationContext();
        }

        VideoViewModelFactory factory = InjectorUtils.provideVideoViewModelFactory(context, mVideoId);
        VideoActivityViewModel viewModel = ViewModelProviders.of(this, factory)
                .get(VideoActivityViewModel.class);

        viewModel.getRecipe().observe(this, recipe->{
            if(recipe != null){
                mTitle.setText(recipe.getTitle());
                mDescription.setText(recipe.getDescription());
            }
        });
    }

    private void insertNestedFragment() {
        YouTubePlayerSupportFragment youTubeFragment = new YouTubePlayerSupportFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_player_fragment, youTubeFragment).commit();
        youTubeFragment.initialize(BuildConfig.YOUTUBE_API_KEY, this);
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer,
                                        boolean wasRestored) {
        mPlayer = youTubePlayer;
        mInitialized = true;
        if(mPhone) {
            mPlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION |
                    YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE |
                    YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        } else {
            mPlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        }
        mPlayer.setPlayerStateChangeListener(this);
        mPlayer.setOnFullscreenListener(this);
        if(!wasRestored){
            mPlayer.loadVideo(mVideoId, mTime);
        } else{
            mPlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult youTubeInitializationResult) {
        if(youTubeInitializationResult.isUserRecoverableError()) {
            Activity activity;
            if (getActivity() != null) {
                activity = getActivity();
                youTubeInitializationResult.getErrorDialog(activity, RECOVERY_DIALOG_REQUEST).show();
            }
        } else{
            String error = youTubeInitializationResult.toString();
            showYouTubeError(error);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.home_button){
            if(getActivity() != null) getActivity().finish();
        } else if(id == R.id.share_button){
            mShared = true;
            if(mPlayer != null) {
                mTime = mPlayer.getCurrentTimeMillis();
                mPlayer.cueVideo(mVideoId, mTime);
            }
            // shareIt(); LLamamos a shareIt en el listener  de cueVideo cuando este termina
        }
    }

    private void shareIt(){
        String urlShare = URL_SHARE + mVideoId;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, urlShare);
        if(getActivity() != null) {
            if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(Intent.createChooser(shareIntent, CHOOSER_TITLE));
            }
        }
    }

    private void showYouTubeError(String youTubeError){
        String error = String.format(getString(R.string.youtube_error), youTubeError);
        View view = getView();
        if(view != null){
            Snackbar.make(view, error, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {
        if(mShared){
            shareIt();
        } else if(mWebPressed){
            listener.onVideoLoaded();
        }
    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {

    }

    @Override
    public void onVideoEnded() {

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        if(errorReason == YouTubePlayer.ErrorReason.UNEXPECTED_SERVICE_DISCONNECTION){
            if(mPlayer != null){
                mPlayer.release();
                mPlayer = null;
            }
        }
        showYouTubeError(errorReason.toString());
    }

    @Override
    public void onFullscreen(boolean isFullscreen) {
        this.mIsFullscreen = isFullscreen;
        doLayout();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        doLayout();
    }

    private void doLayout() {
        boolean isPortrait = getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_PORTRAIT;
        if(mIsFullscreen){
            hideStatusbar();
            showHideViews(View.GONE);
            setLayoutSize(MATCH_PARENT, MATCH_PARENT);
            if(!mPhone){
                if(!isPortrait) {
                    showHideFragment(false);
                }
            }
        } else{
            showStatusbar();
            showHideViews(View.VISIBLE);
            setLayoutSize(MATCH_PARENT, WRAP_CONTENT);
            if(!mPhone){
                if(isPortrait){
                    showHideFragment(false);
                } else{
                    showHideFragment(true);
                }
            }
        }
    }

    private void hideStatusbar() {
        if(getActivity() != null) {
            View view = getActivity().getWindow().getDecorView();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                view.setSystemUiVisibility(View.GONE);
            } else {
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
                view.setSystemUiVisibility(uiOptions);
            }
        }
    }

    private void showStatusbar() {
        if(getActivity() != null) {
            View view = getActivity().getWindow().getDecorView();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                view.setSystemUiVisibility(View.VISIBLE);
            } else {
                view.setSystemUiVisibility(0);
            }
        }
    }

    private void showHideFragment(boolean show) {
        if(getActivity() != null){
            FragmentManager fm = getActivity().getSupportFragmentManager();
            AboutFragment about = (AboutFragment) fm.findFragmentById(R.id.about_fragment);
            if(about != null){
                if (show) {
                    fm.beginTransaction().show(about).commit();
                } else {
                    fm.beginTransaction().hide(about).commit();
                }
            }
        }
    }

    private void setLayoutSize(int width, int height) {
        if(getView() != null){
            FrameLayout container = getView().findViewById(R.id.youtube_player_fragment);
            ViewGroup.LayoutParams params = container.getLayoutParams();
            params.width = width;
            params.height = height;
            container.setLayoutParams(params);
        }
    }

    private void forceLandscapePortrait(int orientation) {
        if(getActivity() != null){
            getActivity().setRequestedOrientation(orientation);
        }
    }

    private void showHideViews(int mostrar) {
        if(getView() != null) {
            LinearLayout contenido = getView().findViewById(R.id.content_text_buttons);
            contenido.setVisibility(mostrar);
        }
    }

    private boolean isPhone(){
        int numColumns = getResources().getInteger(R.integer.num_colums);
        return (numColumns == PHONE_NUM_COLUMNS);
    }

    public void onWebPressed(){
        mWebPressed = true;
        if (mPlayer != null) {
            mTime = mPlayer.getCurrentTimeMillis();
            mPlayer.cueVideo(mVideoId, mTime);
        }
        // LLamamos a forwardTobrowser del fragment about en el listener  de cueVideo cuando este termina
    }
}
