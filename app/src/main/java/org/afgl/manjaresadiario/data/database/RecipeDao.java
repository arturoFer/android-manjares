package org.afgl.manjaresadiario.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by arturo on 14/02/2018.
 * Objeto de acceso a datos DAO. Aqui ponemos todas las consultas que necesitamos hacer a la base de
 * datos
 */
@Dao
public interface RecipeDao {

    @Query("SELECT id, videoId, title FROM recipes ORDER BY id DESC")
    LiveData<List<ListRecipeEntry>> getAllRecipes();

    @Query("SELECT videoId, title, description FROM recipes WHERE videoId = :videoId")
    LiveData<VideoRecipeEntry> getRecipeByVideoId(String videoId);

    @Query("SELECT COUNT(id) FROM recipes")
    int countAllEntries();

    @Insert
    void bulkInsert(RecipeEntry... recipe);

}
