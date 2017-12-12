package com.outsource.inovaufrpe.prestador.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Nicolas on 27/11/2017.
 */

public final class FirebaseAux {
    private static FirebaseAux instancia;
    private String codigo;
    private final FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference prestadorReference;

    /**
     * Gets instancia.
     *
     *
     * @return the instancia
     */
    public static synchronized FirebaseAux getInstancia() {
        if (instancia == null) {
            instancia = new FirebaseAux();
        }
        return instancia;
    }

    private FirebaseAux() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        prestadorReference = databaseReference.child("prestador");
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public DatabaseReference getPrestadorReference() {
        return prestadorReference;
    }

    public void setPrestadorReference(DatabaseReference usuarioReference) {
        this.prestadorReference = usuarioReference;
    }


    public FirebaseUser getUser() {
        return firebaseAuth.getCurrentUser();
    }
}
