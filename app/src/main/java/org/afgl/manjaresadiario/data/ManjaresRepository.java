package org.afgl.manjaresadiario.data;

import android.arch.lifecycle.LiveData;

import org.afgl.manjaresadiario.AppExecutors;
import org.afgl.manjaresadiario.data.assetSource.RecipeAssetDataSource;
import org.afgl.manjaresadiario.data.database.ListRecipeEntry;
import org.afgl.manjaresadiario.data.database.RecipeDao;
import org.afgl.manjaresadiario.data.database.RecipeEntry;
import org.afgl.manjaresadiario.data.database.VideoRecipeEntry;

import java.util.List;

/**
 * Repositorio, maneja y orquesta el acceso a datos de la aplicacion
 */

public class ManjaresRepository {

    //Para el singleton
    private static final Object LOCK = new Object();
    private static ManjaresRepository sInstance;

    private final RecipeDao mRecipeDao;
    private final RecipeAssetDataSource mRecipeAssetDataSource;
    private final AppExecutors mExecutors;
    private boolean mInitialized = false;

    private ManjaresRepository(RecipeDao recipeDao,
                               RecipeAssetDataSource recipeAssetDataSource,
                               AppExecutors executors) {
        this.mRecipeDao = recipeDao;
        this.mRecipeAssetDataSource = recipeAssetDataSource;
        this.mExecutors = executors;

        // Mientras el repositorio exista observamos los datos del asset por si cambian y los
        // guardamos en la base de datos
        final LiveData<RecipeEntry[]> assetData = mRecipeAssetDataSource.getCurrentAssetData();
        assetData.observeForever((RecipeEntry[] newAssetData) -> mExecutors.diskIO().execute(()-> mRecipeDao.bulkInsert(newAssetData)));
    }

    public synchronized static ManjaresRepository getInstance(RecipeDao recipeDao,
                                                               RecipeAssetDataSource recipeAssetDataSource,
                                                               AppExecutors executors){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new ManjaresRepository(recipeDao, recipeAssetDataSource, executors);
            }
        }
        return sInstance;
    }

    private synchronized void initializeData(){
        if(mInitialized) return;
        mInitialized = true;

        mExecutors.diskIO().execute(()->{
            if(isFetchNeeded()){
                startFetchAssetService();
            }
        });
    }

    public LiveData<List<ListRecipeEntry>> getAllRecipes(){
        initializeData();
        return  mRecipeDao.getAllRecipes();
    }

    public LiveData<VideoRecipeEntry> getRecipeByVideoId(String videoId){
        initializeData();
        return mRecipeDao.getRecipeByVideoId(videoId);
    }

    private boolean isFetchNeeded(){
        int count = mRecipeDao.countAllEntries();
        return count <= 0;
    }

    private void startFetchAssetService(){
        mRecipeAssetDataSource.startFetchAssetService();
    }
}
