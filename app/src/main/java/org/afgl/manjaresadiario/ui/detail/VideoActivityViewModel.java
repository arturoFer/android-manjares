package org.afgl.manjaresadiario.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.afgl.manjaresadiario.data.ManjaresRepository;
import org.afgl.manjaresadiario.data.database.VideoRecipeEntry;

/**
 * Created by arturo on 19/02/2018.
 * ViewModel para VideoActivity
 */

class VideoActivityViewModel extends ViewModel {

    private final LiveData<VideoRecipeEntry> recipe;

    VideoActivityViewModel(ManjaresRepository repository, String videoId){
        recipe = repository.getRecipeByVideoId(videoId);
    }

    LiveData<VideoRecipeEntry> getRecipe(){
        return recipe;
    }
}
