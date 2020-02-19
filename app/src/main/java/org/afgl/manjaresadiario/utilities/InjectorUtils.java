package org.afgl.manjaresadiario.utilities;

import android.content.Context;

import com.bumptech.glide.Glide;

import org.afgl.manjaresadiario.AppExecutors;
import org.afgl.manjaresadiario.data.ManjaresRepository;
import org.afgl.manjaresadiario.data.assetSource.RecipeAssetDataSource;
import org.afgl.manjaresadiario.data.database.ManjaresDatabase;
import org.afgl.manjaresadiario.lib.GlideImageLoader;
import org.afgl.manjaresadiario.lib.ImageLoader;
import org.afgl.manjaresadiario.ui.detail.VideoViewModelFactory;
import org.afgl.manjaresadiario.ui.list.MainViewModelFactory;
import org.afgl.manjaresadiario.ui.list.RecipeListAdapter;
import org.afgl.manjaresadiario.ui.list.RecipeListAdapterOnItemClickHandler;

/**
 * Created by arturo on 15/02/2018.
 * Inyeccion de dependencias
 */

public class InjectorUtils {

    private static ManjaresRepository provideRepository(Context context){
        ManjaresDatabase database = ManjaresDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        RecipeAssetDataSource assetDataSource = RecipeAssetDataSource.getInstance(context.getApplicationContext(), executors);
        return ManjaresRepository.getInstance(database.recipeDao(),assetDataSource,executors);
    }

    public static RecipeAssetDataSource provideAssetDataSource(Context context){
        AppExecutors executors = AppExecutors.getInstance();
        return RecipeAssetDataSource.getInstance(context.getApplicationContext(), executors);
    }


    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context){
        ManjaresRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }

    public static VideoViewModelFactory provideVideoViewModelFactory(Context context, String videoId){
        ManjaresRepository repository = provideRepository(context.getApplicationContext());
        return new VideoViewModelFactory(repository, videoId);
    }

    public static ImageLoader provideImageLoader(Context context){
        return new GlideImageLoader(Glide.with(context));
    }

    public static RecipeListAdapter provideRecipeListAdapter(RecipeListAdapterOnItemClickHandler listener, Context context){
        ImageLoader loader = provideImageLoader(context);
        return new RecipeListAdapter(listener, loader);
    }
}
