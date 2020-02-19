package org.afgl.manjaresadiario.data.database;

/**
 * Created by arturo on 19/02/2018.
 * Datos necesarios para la actividad video
 */

public class VideoRecipeEntry {

    private String videoId;
    private String title;
    private String description;

    VideoRecipeEntry(String videoId, String title, String description) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
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
