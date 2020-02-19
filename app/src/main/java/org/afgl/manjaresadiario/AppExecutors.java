package org.afgl.manjaresadiario;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by arturo on 15/02/2018.
 * Global executor pools para toda la aplicacion
 * Agrupando las tareas de esta forma se evita "starvation" (la lectura de disco
 * no espera las peticiones web
 */

public class AppExecutors {

    // Para el singleton
    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;

    private final Executor diskIO;
    //private final Executor mainThread;
    private final Executor networkIO;

    /*private AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }*/

    private AppExecutors(Executor diskIO, Executor networkIO){
        this.diskIO = diskIO;
        this.networkIO = networkIO;
    }

    public static AppExecutors getInstance(){
        if(sInstance == null){
            synchronized (LOCK){
                /*sInstance = new AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());*/
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3));

            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }

    /*public Executor mainThread() {
        return mainThread;
    }*/

    public Executor networkIO() {
        return networkIO;
    }

    /*private static class MainThreadExecutor implements Executor{
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }*/
}
