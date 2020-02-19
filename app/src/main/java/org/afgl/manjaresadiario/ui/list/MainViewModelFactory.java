package org.afgl.manjaresadiario.ui.list;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.afgl.manjaresadiario.data.ManjaresRepository;

/**
 * Created by arturo on 15/02/2018.
 * Para que el constructor de MainActivityViewModel pueda tener parametros
 */

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final ManjaresRepository mRepository;

    public MainViewModelFactory(ManjaresRepository repository) {
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainActivityViewModel(mRepository);
    }
}
