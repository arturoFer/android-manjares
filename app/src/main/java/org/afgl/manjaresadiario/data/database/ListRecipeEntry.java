package org.afgl.manjaresadiario.data.database;

/**
 * Created by arturo on 15/02/2018.
 * Receta con solo los campos que necesitamos en RecyclerView
 */

public class ListRecipeEntry {

    private int id;
    private String videoId;
    private String title;

    ListRecipeEntry(int id, String videoId, String title) {
        this.id = id;
        this.videoId = videoId;
        this.title = title;
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
}
