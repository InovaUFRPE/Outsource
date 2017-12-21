package com.outsource.inovaufrpe.usuario.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class FirebaseAux {
    private static FirebaseAux instancia;
    private final FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference usuarioReference;

    /**
     * Gets instancia.
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
        usuarioReference = databaseReference.child("usuario");
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

    public DatabaseReference getUsuarioReference() {
        return usuarioReference;
    }

    public void setUsuarioReference(DatabaseReference usuarioReference) {
        this.usuarioReference = usuarioReference;
    }


    public FirebaseUser getUser() {
        return firebaseAuth.getCurrentUser();
    }
}
