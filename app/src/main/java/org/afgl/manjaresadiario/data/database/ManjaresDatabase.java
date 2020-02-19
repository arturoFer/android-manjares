package org.afgl.manjaresadiario.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by arturo on 14/02/2018.
 * Objeto database
 */
@Database(entities = {RecipeEntry.class}, version = 1, exportSchema = false)
public abstract class ManjaresDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "manjares";

    // Para el singleton
    private static final Object LOCK = new Object();
    private static ManjaresDatabase sInstance;

    public static ManjaresDatabase getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        ManjaresDatabase.class,DATABASE_NAME).build();

            }
        }
        return sInstance;
    }

    // El DAO asociado a la base de datos
    public abstract RecipeDao recipeDao();
}
