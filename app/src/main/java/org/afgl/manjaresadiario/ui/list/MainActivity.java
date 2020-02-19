package org.afgl.manjaresadiario.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.afgl.manjaresadiario.R;
import org.afgl.manjaresadiario.lib.ImageLoader;

import org.afgl.manjaresadiario.ui.about.AboutActivity;
import org.afgl.manjaresadiario.ui.detail.VideoActivity;
import org.afgl.manjaresadiario.utilities.InjectorUtils;

public class MainActivity extends AppCompatActivity implements RecipeListAdapterOnItemClickHandler{

    private static final String VIDEO_ID_EXTRA = "videoId";
    private static final String KEY_POSITION = "key_position";
    private static final String KEY_LAUNCHED = "key_launched";
    private static final int PHONE = 1;
    private static final int TABLET_LANDSCAPE = 5;

    private int mPosition = RecyclerView.NO_POSITION;

    private RecipeListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;

    private boolean mLaunched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_recipes);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        if(savedInstanceState != null){
            mPosition = savedInstanceState.getInt(KEY_POSITION);
            mLaunched = savedInstanceState.getBoolean(KEY_LAUNCHED);
        }

        setupToolbar();
        setupRecyclerView();
        loadImageCollapsingToolbar();
        setupViewModel();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout cltLayout = findViewById(R.id.collapsingToolbar);
        cltLayout.setTitle(getString(R.string.app_name));
    }

    private void loadImageCollapsingToolbar() {
        ImageView imageView = findViewById(R.id.portada_image);
        ImageLoader loader = mAdapter.getImageLoader();
        loader.load(imageView, R.drawable.portada);
    }

    private void setupViewModel() {
        MainViewModelFactory factory = InjectorUtils.provideMainActivityViewModelFactory(this.getApplicationContext());
        MainActivityViewModel viewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);

        viewModel.getListaRecetas().observe(this, listRecipeEntries -> {
            mAdapter.swapRecipeList(listRecipeEntries);
            if(listRecipeEntries != null && listRecipeEntries.size()!= 0 ) showRecipesDataView();
            else showLoading();
        });
    }

    private void setupRecyclerView() {
        int numColumns = getResources().getInteger(R.integer.num_colums);
        switch(numColumns){
            case PHONE:
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
                mRecyclerView.setLayoutManager(linearLayoutManager);
                break;
            default:
                GridLayoutManager gridlayoutManager = new GridLayoutManager(this, numColumns);
                mRecyclerView.setLayoutManager(gridlayoutManager);
                break;
        }
        mRecyclerView.setHasFixedSize(true);
        mAdapter = InjectorUtils.provideRecipeListAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showRecipesDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(String videoId) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra(VIDEO_ID_EXTRA, videoId);
        mLaunched = true;
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean tablet_lanscape = getResources().getInteger(R.integer.num_colums) == TABLET_LANDSCAPE;
        if(!tablet_lanscape) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main_activity, menu);
            return true;
        } else{
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                mLaunched = true;
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LinearLayoutManager layoutManager= (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        outState.putInt(KEY_POSITION, position);
        outState.putBoolean(KEY_LAUNCHED, mLaunched);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mLaunched) {
            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            mRecyclerView.scrollToPosition(mPosition);
        }else{
            mLaunched = false;
        }
    }
}
