package org.afgl.manjaresadiario.ui.detail;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.afgl.manjaresadiario.R;
import org.afgl.manjaresadiario.ui.about.AboutFragment;

public class VideoActivity extends AppCompatActivity implements OnButtonWebListener, OnCuedVideoLoadedListener{

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private static final int PHONE_NUM_COLUMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RECOVERY_DIALOG_REQUEST){
            recreate();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onButtonWebCliked() {
        FragmentManager fm = getSupportFragmentManager();
        boolean isPortrait = getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_PORTRAIT;
        boolean isPhone = getResources().getInteger(R.integer.num_colums) == PHONE_NUM_COLUMS;
        if(!isPhone){
            if(!isPortrait) {
                VideoFragment videoFragment = (VideoFragment) fm.findFragmentById(R.id.fragment_video);
                videoFragment.onWebPressed();
            } else {
                AboutFragment aboutFragment = (AboutFragment) fm.findFragmentById(R.id.about_fragment);
                aboutFragment.forwardToBrowser();
            }
        }
    }

    @Override
    public void onVideoLoaded() {
        FragmentManager fm = getSupportFragmentManager();
        AboutFragment aboutFragment = (AboutFragment) fm.findFragmentById(R.id.about_fragment);
        aboutFragment.forwardToBrowser();
    }
}
