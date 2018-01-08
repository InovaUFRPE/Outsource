package com.outsource.inovaufrpe.prestador.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Heitor on 08/01/2018.
 */

public class NotaMedia {
    private float media;

    public float getMedia() {
        return media;
    }

    public NotaMedia() {
        FirebaseDatabase.getInstance().getReference().child("usuario").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                media = dataSnapshot.child("nota").getValue(Float.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public float mediaNotas(int nota){
        media = (nota + media)/2;
        return media;
    }
}
