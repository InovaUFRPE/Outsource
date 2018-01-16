package com.outsource.inovaufrpe.usuario.utils;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Heitor on 08/01/2018.
 */

public class NotaMedia {
    private int notaMedia;
    private int peso;
    private String prestadorID;



    public void adicionarNota(final String prestadorId, final int nota) {
        prestadorID = prestadorId;
        FirebaseDatabase.getInstance().getReference().child("prestador").child(prestadorId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notaMedia = dataSnapshot.child("nota").getValue(int.class);
                peso = dataSnapshot.child("pesoNota").getValue(int.class);
                FirebaseDatabase.getInstance().getReference().child("prestador").child(prestadorId).child("nota").setValue(notaMedia+nota);
                FirebaseDatabase.getInstance().getReference().child("prestador").child(prestadorId).child("pesoNota").setValue(peso+1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
