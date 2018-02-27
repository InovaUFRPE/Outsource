package com.outsource.inovaufrpe.usuario.solicitante.gui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.solicitante.dominio.Usuario;
import com.outsource.inovaufrpe.usuario.utils.FirebaseAux;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class MainPerfilFragment extends Fragment {
    ImageButton btnConfig;
    TextView nomeUsuario;
    TextView emailUsuario;
    TextView telefoneUsuario;
    RatingBar avaliarPerfil;
    TextView numServicosAtendidos;
    TextView numAvaliacoes;
    CircleImageView foto_perfil;
    ProgressBar pbFoto;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 2;

    public MainPerfilFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        nomeUsuario = view.findViewById(R.id.tvNomePerfil);
        emailUsuario = view.findViewById(R.id.tvEmailPerfil);
        telefoneUsuario = view.findViewById(R.id.tvTelefonePerfil);
        avaliarPerfil = view.findViewById(R.id.rbAvaliarPerfil);
        numServicosAtendidos = view.findViewById(R.id.num_servicos_concluidos);
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
        FirebaseAux firebase = FirebaseAux.getInstancia();
        DatabaseReference usuarioReference = firebase.getUsuarioReference().child(firebase.getUser().getUid());
        usuarioReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuario user = dataSnapshot.getValue(Usuario.class);
                        String nomeCompleto = user.getNome() + " " + user.getSobrenome();
                        if(user.getFoto() != null && !user.getFoto().isEmpty()) {
                            pbFoto.setVisibility(View.VISIBLE);
                            pbFoto.setIndeterminate(true);
                            Picasso.with(getActivity()).load(Uri.parse(user.getFoto())).fit().centerCrop().into(foto_perfil, new com.squareup.picasso.Callback(){
                                @Override
                                public void onSuccess() {
                                    pbFoto.setIndeterminate(false);
                                    pbFoto.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    pbFoto.setIndeterminate(false);
                                    pbFoto.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "Falha ao Carregar foto!", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        nomeUsuario.setText(nomeCompleto);
                        emailUsuario.setText(user.getEmail());
                        telefoneUsuario.setText(user.getTelefone());
                        numAvaliacoes.setText(String.valueOf(user.getPesoNota()));
                        databaseReference.child("servico").orderByChild("concluido").equalTo(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                numServicosAtendidos.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        if (user.getPesoNota() == 0){
                            avaliarPerfil.setRating(0);
                        }
                        else {
                            avaliarPerfil.setRating((float)user.getNota()/user.getPesoNota());
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        nomeUsuario.setText(firebase.getUser().getDisplayName());

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
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            pbFoto.setVisibility(View.VISIBLE);
            pbFoto.setIndeterminate(true);
            Uri uri = data.getData();

            StorageReference childRef = mStorage.child("foto").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            childRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUri = taskSnapshot.getDownloadUrl();
                    FirebaseDatabase.getInstance().getReference().child("usuario").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("foto").setValue(downloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
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
