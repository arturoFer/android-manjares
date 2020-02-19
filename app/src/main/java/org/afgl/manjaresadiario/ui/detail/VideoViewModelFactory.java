package org.afgl.manjaresadiario.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.afgl.manjaresadiario.data.ManjaresRepository;

/**
 * Created by arturo on 19/02/2018.
 * Para usar dos parametros en el constructor del VideoActivityViewModel
 */

public class VideoViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final ManjaresRepository repository;
    private final String videoId;

    public VideoViewModelFactory(ManjaresRepository repository, String videoId){
        this.repository = repository;
        this.videoId = videoId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new VideoActivityViewModel(repository, videoId);
    }
}
