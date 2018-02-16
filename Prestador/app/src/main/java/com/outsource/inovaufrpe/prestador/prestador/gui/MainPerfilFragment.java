package com.outsource.inovaufrpe.prestador.prestador.gui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.prestador.dominio.Prestador;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainPerfilFragment extends Fragment {
    ImageButton btnConfig;
    TextView nomeUsuario;
    TextView emailUsuario;
    TextView telefoneUsuario;
    RatingBar avaliarPerfil;
    TextView numServicosAtendidos;
    CircleImageView foto_perfil;
    ProgressBar pbFoto;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 2;

    TextView numAvaliacoes;


    public MainPerfilFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        nomeUsuario = view.findViewById(R.id.tvNomePerfil);
        emailUsuario = view.findViewById(R.id.tvEmailPerfil);
        telefoneUsuario = view.findViewById(R.id.tvTelefonePerfil);
        avaliarPerfil = view.findViewById(R.id.rbAvaliarPerfil);
        numServicosAtendidos = view.findViewById(R.id.num_servicos_atendidos);
        numAvaliacoes = view.findViewById(R.id.num_avaliacoes);
        foto_perfil = view.findViewById(R.id.profile_image);
        pbFoto = view.findViewById(R.id.indeterminateBar);
        mStorage = FirebaseStorage.getInstance().getReference();
        pbFoto.setVisibility(View.GONE);

        foto_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarFoto();
            }
        });

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference usuarioReference = databaseReference.child("prestador").child(usuarioAtual.getUid());
        usuarioReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Prestador prestador = dataSnapshot.getValue(Prestador.class);
                        String nomeCompleto = prestador.getNome()+" "+prestador.getSobrenome();
                        nomeUsuario.setText(nomeCompleto);
                        emailUsuario.setText(prestador.getEmail());
                        if(prestador.getFoto() != null && !prestador.getFoto().isEmpty()) {
                            Picasso.with(getActivity()).load(Uri.parse(prestador.getFoto())).fit().centerCrop().into(foto_perfil);
                        }
                        telefoneUsuario.setText(prestador.getTelefone());
                        numAvaliacoes.setText(String.valueOf(prestador.getPesoNota()));
                        databaseReference.child("servico").orderByChild("idPrestador").equalTo(prestador.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                numServicosAtendidos.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        if(prestador.getPesoNota() == 0){
                            avaliarPerfil.setRating(0);
                        }else{
                            avaliarPerfil.setRating((float)prestador.getNota()/prestador.getPesoNota());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );


        return view;
    }

    private void selecionarFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    @Override
    public void onResume() {
        super.onResume();
        btnConfig = getActivity().findViewById(R.id.imageButton);
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditarPerfilActivity.class));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == GALLERY_INTENT ){
            pbFoto.setVisibility(View.VISIBLE);
            pbFoto.setIndeterminate(true);
            Uri uri = data.getData();

            StorageReference childRef = mStorage.child("foto").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            childRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUri = taskSnapshot.getDownloadUrl();
                    FirebaseDatabase.getInstance().getReference().child("prestador").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("foto").setValue(downloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Picasso.with(getActivity()).load(downloadUri).fit().centerCrop().into(foto_perfil);
                            Toast.makeText(getActivity(), "Imagem salva com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Imagem não pode ser salva, tente novamente!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    pbFoto.setIndeterminate(false);
                    pbFoto.setVisibility(View.GONE);
                }
            });
        }
    }
}
