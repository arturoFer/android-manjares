package org.afgl.manjaresadiario.lib;

import android.widget.ImageView;

/**
 * Created by arturo on 16/02/2018.
 * Interfaz para el wrapper de Glide
 */

public interface ImageLoader {
    void load(ImageView imageView, String url, int resError);
    void load(ImageView imageView, int res);
    void loadCircle(ImageView imageView, int res);
}
