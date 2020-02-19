package org.afgl.manjaresadiario.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.afgl.manjaresadiario.data.ManjaresRepository;
import org.afgl.manjaresadiario.data.database.ListRecipeEntry;

import java.util.List;

/**
 * Created by arturo on 14/02/2018.
 * ViewModel para lista de recetas
 */

class MainActivityViewModel extends ViewModel {

    private final LiveData<List<ListRecipeEntry>> mListaRecetas;

    MainActivityViewModel(ManjaresRepository repository) {
        mListaRecetas = repository.getAllRecipes();
    }

    LiveData<List<ListRecipeEntry>> getListaRecetas(){
        return mListaRecetas;
    }
}
