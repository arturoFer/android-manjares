package org.afgl.manjaresadiario.data.assetSource;

import android.support.annotation.NonNull;

import org.afgl.manjaresadiario.data.database.RecipeEntry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by arturo on 15/02/2018.
 * Convierte la cadena del json en una lista de recetas
 */

final class RecipesJsonParser {

    private static final String OWN_POSITION = "posicion";
    private static final String OWN_VIDEOID = "videoId";
    private static final String OWN_TITLE = "title";
    private static final String OWN_DESCRIPTION = "description";

    private static RecipeEntry[] fromJson(final JSONArray recipesJson) throws JSONException{
        RecipeEntry[] recipeEntries = new RecipeEntry[recipesJson.length()];
        for(int i=0; i<recipesJson.length(); i++){
            JSONObject recipeObject = recipesJson.getJSONObject(i);
            RecipeEntry recipe = fromJson(recipeObject);
            recipeEntries[i] = recipe;
        }
        return recipeEntries;
    }


    @NonNull
    private static RecipeEntry fromJson(final JSONObject recipeJson) throws JSONException{
        int id = recipeJson.getInt(OWN_POSITION);
        String videoId = recipeJson.getString(OWN_VIDEOID);
        String title = recipeJson.getString(OWN_TITLE);
        String description = recipeJson.getString(OWN_DESCRIPTION).replace("\\n","\n");
        return new RecipeEntry(id, videoId, title, description);
    }

    @NonNull
    static RecipesResponse parse(final String jsonString) throws JSONException{
        JSONArray recipesJson = new JSONArray(jsonString);
        RecipeEntry[] recipeEntries = fromJson(recipesJson);
        return new RecipesResponse(recipeEntries);

    }
}
