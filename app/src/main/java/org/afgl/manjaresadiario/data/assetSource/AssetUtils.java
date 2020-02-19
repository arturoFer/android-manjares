package org.afgl.manjaresadiario.data.assetSource;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by arturo on 15/02/2018.
 * Funciones staticas para acceder al archivo json de la carpeta assets
 */

final class AssetUtils {
    static String getJSONString(Context context){
        String json;
        try{
            InputStream is = context.getAssets().open("manjares.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            int readedLen = is.read(buffer);
            is.close();
            if(readedLen <= 0) return null;
            json = new String(buffer,"UTF-8");
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return json;
    }
}
