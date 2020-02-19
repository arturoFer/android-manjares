package org.afgl.manjaresadiario.ui.about;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.afgl.manjaresadiario.R;
import org.afgl.manjaresadiario.ui.detail.OnButtonWebListener;

public class AboutActivity extends AppCompatActivity implements OnButtonWebListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setUpNavigationButton();
    }

    private void setUpNavigationButton() {
        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onButtonWebCliked() {
        FragmentManager fm = getSupportFragmentManager();
        AboutFragment aboutFragment = (AboutFragment) fm.findFragmentById(R.id.about_fragment);
        aboutFragment.forwardToBrowser();
    }

}
