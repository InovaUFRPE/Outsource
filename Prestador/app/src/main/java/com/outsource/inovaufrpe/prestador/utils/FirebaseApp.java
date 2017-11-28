package com.outsource.inovaufrpe.prestador.utils;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Pichau on 21/11/2017.
 */

public class FirebaseApp extends android.app.Application{

        @Override
        public void onCreate() {
            super.onCreate();
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
}
