package org.afgl.manjaresadiario.data.assetSource;

import android.support.annotation.NonNull;

import org.afgl.manjaresadiario.data.database.RecipeEntry;

/**
 * Created by arturo on 15/02/2018.
 * Almacena la lista de recetas obtenida desde el archivo json
 */

class RecipesResponse {
    @NonNull
    private final RecipeEntry[] mRecipeEntry;

    RecipesResponse(@NonNull RecipeEntry[] mRecipeEntry) {
        this.mRecipeEntry = mRecipeEntry;
    }

    @NonNull
    RecipeEntry[] getRecipeEntry() {
        return mRecipeEntry;
    }
}
