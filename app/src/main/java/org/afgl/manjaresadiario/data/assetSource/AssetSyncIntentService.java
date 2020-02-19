package org.afgl.manjaresadiario.data.assetSource;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import org.afgl.manjaresadiario.utilities.InjectorUtils;

/**
 * Created by arturo on 15/02/2018.
 * Servicio que lee el archivo json y lo guarda en base de datos
 */

public class AssetSyncIntentService extends IntentService {

    public AssetSyncIntentService() {
        super("AssetSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        RecipeAssetDataSource assetDataSource = InjectorUtils.provideAssetDataSource(this.getApplicationContext());
        assetDataSource.fetchRecipes();
    }
}
