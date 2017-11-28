package com.outsource.inovaufrpe.prestador.utils;

import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.outsource.inovaufrpe.prestador.servico.dominio.EstadoServico;

/**
 * Created by Pichau on 26/11/2017.
 */

public class FirebaseUtil {


    public void moverServico(final DatabaseReference fromPath, final DatabaseReference toPath, final EstadoServico estadoServico) throws DatabaseException{
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            throw new DatabaseException(firebaseError.getMessage());
                        } else {
                            toPath.child("estado").setValue(estadoServico.getValue());
                            fromPath.removeValue();
                            System.out.println("Success");

                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
