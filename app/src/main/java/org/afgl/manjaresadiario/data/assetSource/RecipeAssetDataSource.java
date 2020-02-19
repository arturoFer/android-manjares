package org.afgl.manjaresadiario.data.assetSource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;

import org.afgl.manjaresadiario.AppExecutors;
import org.afgl.manjaresadiario.data.database.RecipeEntry;

import java.lang.ref.WeakReference;


/**
 * Created by arturo on 15/02/2018.
 * Leemos los datos del archivo json situado en carpeta assets y los guardamos en database
 */

public class RecipeAssetDataSource {

    // Para instanciar el singleton
    // Al ser el contexto de aplicacion no pasa nada pues sobrevive y no hay memory leak
    private static final Object LOCK = new Object();

    //Al ser application context no tiene ciclo de vida y no hay memory leaks.  Tambien he probado
    // con WeakREference y eel IDE no se queja
    private static RecipeAssetDataSource sInstance;

    //private final Context mContext;
    private final WeakReference<Context> mContext;

    private final MutableLiveData<RecipeEntry[]> mAssetData;
    private final AppExecutors mExecutors;

    private RecipeAssetDataSource(Context context, AppExecutors executors){
        //mContext = context;
        mContext = new WeakReference<>(context);
        mExecutors = executors;
        mAssetData = new MutableLiveData<>();
    }

    public static RecipeAssetDataSource getInstance(Context context, AppExecutors executors){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new RecipeAssetDataSource(context.getApplicationContext(), executors);
            }
        }
        return sInstance;
    }

    public LiveData<RecipeEntry[]> getCurrentAssetData(){
        return mAssetData;
    }

    public void startFetchAssetService(){
        Intent intentToFetch = new Intent(mContext.get(), AssetSyncIntentService.class);
        mContext.get().startService(intentToFetch);
        //Intent intentToFetch = new Intent(mContext, AssetSyncIntentService.class);
        //mContext.startService(intentToFetch);
    }

    void fetchRecipes(){
        mExecutors.networkIO().execute(()->{
            try {
                //String jsonString = AssetUtils.getJSONString(mContext);
                String jsonString = AssetUtils.getJSONString(mContext.get());
                RecipesResponse response = RecipesJsonParser.parse(jsonString);
                if (response.getRecipeEntry().length != 0) {
                    mAssetData.postValue(response.getRecipeEntry());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        });
    }

}
