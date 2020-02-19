package org.afgl.manjaresadiario.lib;

import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by arturo on 16/02/2018.
 * Clase que encapsula Glide
 */

public class GlideImageLoader implements ImageLoader{

    private RequestManager mRequestManager;

    public GlideImageLoader(RequestManager requestManager){
        this.mRequestManager = requestManager;
    }
    @Override
    public void load(ImageView imageView, String url, int resError) {
        RequestOptions cacheOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).error(resError);
        mRequestManager
                .load(url)
                .apply(cacheOptions)
                .transition(withCrossFade())
                .into(imageView);
    }

    @Override
    public void load(ImageView imageView, int res) {
        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).centerCrop();
        mRequestManager
                .load(res)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadCircle(ImageView imageView, int res) {
        RequestOptions circleOptions = RequestOptions.circleCropTransform();
        RequestOptions cacheOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        mRequestManager
                .load(res)
                .apply(cacheOptions)
                .apply(circleOptions)
                .into(imageView);
    }

}
