package org.afgl.manjaresadiario.ui.list;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.afgl.manjaresadiario.R;
import org.afgl.manjaresadiario.data.database.ListRecipeEntry;
import org.afgl.manjaresadiario.lib.ImageLoader;

import java.util.List;

/**
 * Created by arturo on 15/02/2018.
 * Adapter para recycler view
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListAdapterViewHolder> {

    private static final String URL_IMAGE_START = "https://i.ytimg.com/vi/";
    private static final String URL_IMAGE_END = "/hqdefault.jpg";

    private final RecipeListAdapterOnItemClickHandler mClickHandler;
    private List<ListRecipeEntry> mRecipeList;
    private final ImageLoader imageLoader;

    public RecipeListAdapter(RecipeListAdapterOnItemClickHandler clickHandler, ImageLoader loader) {
        this.mClickHandler = clickHandler;
        this.imageLoader = loader;
    }

    @NonNull
    @Override
    public RecipeListAdapter.RecipeListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        view.setFocusable(true);
        return new RecipeListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapter.RecipeListAdapterViewHolder holder, int position) {
        ListRecipeEntry recipe = mRecipeList.get(position);
        String url = getUrl(recipe.getVideoId());

        holder.titleView.setText(recipe.getTitle());
        imageLoader.load(holder.imageView, url, R.drawable.no_thumbnail);
    }

    private String getUrl(String videoId) {
        return URL_IMAGE_START + videoId + URL_IMAGE_END;
    }

    void swapRecipeList(List<ListRecipeEntry> recipeList){
        mRecipeList = recipeList;
        notifyDataSetChanged();
    }

    ImageLoader getImageLoader(){
        return imageLoader;
    }

    @Override
    public int getItemCount() {
        if(mRecipeList == null) return 0;
        else return mRecipeList.size();
    }

    class RecipeListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView titleView;
        final ImageView imageView;

        RecipeListAdapterViewHolder(View view) {
            super(view);

            titleView = view.findViewById(R.id.recipe_title);
            imageView = view.findViewById(R.id.recipe_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ListRecipeEntry recipe = mRecipeList.get(position);
            mClickHandler.onItemClick(recipe.getVideoId());
        }
    }
}
