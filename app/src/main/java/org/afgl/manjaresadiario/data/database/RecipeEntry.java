package org.afgl.manjaresadiario.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by arturo on 14/02/2018.
 * Entidad Receta, almacena una receta
 */
@Entity(tableName = "recipes", indices = {@Index(value= {"videoId"}, unique = true)})
public class RecipeEntry {

    @PrimaryKey
    private int id;

    private String videoId;
    private String title;
    private String description;

    public RecipeEntry(int id, String videoId, String title, String description) {
        this.id = id;
        this.videoId = videoId;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
