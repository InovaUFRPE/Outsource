package com.outsource.inovaufrpe.usuario.servico.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.servico.dominio.EstadoServico;
import com.outsource.inovaufrpe.usuario.servico.dominio.Servico;
import com.outsource.inovaufrpe.usuario.solicitante.gui.MainActivity;

import java.sql.Timestamp;
import java.util.Date;

public class CadastroServicoActivity extends AppCompatActivity {
    EditText etNomeServicoID;
    EditText etDescricaoServicoID;
    EditText etPrecoServicoID;
    TextView tvLocalSelecionadoID;
    TextView tvLocalID;
    Button btVoltarID;
    Button btConfirmarID;
    Button placePickerID;
    LatLng latLng = null;
    final int PLACE_PICKER_REQUEST = 1;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_servico);

        tvLocalSelecionadoID = findViewById(R.id.tvLocalSelecionadoID);
        tvLocalID = findViewById(R.id.tvLocalID);
        etNomeServicoID = findViewById(R.id.etNomeServicoID);
        etDescricaoServicoID = findViewById(R.id.etDescricaoServicoID);
        etPrecoServicoID = findViewById(R.id.etPrecoServicoID);
        btVoltarID = findViewById(R.id.btVoltarID);
        btConfirmarID = findViewById(R.id.btConfirmarID);
        placePickerID = findViewById(R.id.placePickerID);

        tvLocalSelecionadoID.setVisibility(View.INVISIBLE);
        tvLocalID.setVisibility(View.INVISIBLE);

        btVoltarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltar();
            }
        });

        btConfirmarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmar();
            }
        });

        if (firebaseDatabase == null) {
            inicializarFirebase();
        }


        placePickerID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(CadastroServicoActivity.this),PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==PLACE_PICKER_REQUEST){
            if(resultCode==RESULT_OK){
                Place place = PlacePicker.getPlace(data,this);
                latLng = place.getLatLng();
                tvLocalID.setText(place.getAddress());
                placePickerID.setText("Alterar Local");
                tvLocalSelecionadoID.setVisibility(View.VISIBLE);
                tvLocalID.setVisibility(View.VISIBLE);
            }
        }
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(CadastroServicoActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void voltar() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void confirmar() {
        if(latLng == null){
            Toast.makeText(this, "Selecione um local para o serviço!", Toast.LENGTH_SHORT).show();
        }else {
            Servico servico = criaServico();
            adicionaServicoUsuario(servico);
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void adicionaServicoUsuario(Servico servico) {
        databaseReference.child("usuario").child(firebaseAuth.getCurrentUser().getUid()).child("servicos").child(servico.getId()).setValue(servico.getEstado());

    }

    private Servico criaServico() {
        String servicoId = databaseReference.child("servico").child("aberto").push().getKey();
        Date data = new Date();
        Servico servico = new Servico();
        servico.setId(servicoId);
        servico.setNome(etNomeServicoID.getText().toString().trim());
        servico.setDescricao(etDescricaoServicoID.getText().toString().trim());
        servico.setPreco(Double.valueOf(etPrecoServicoID.getText().toString().trim()));
        servico.setOferta(Double.valueOf(etPrecoServicoID.getText().toString().trim()));
        servico.setData(new Timestamp(data.getTime()).toString());
        servico.setEstado(EstadoServico.ABERTA.getValue());
        servico.setIdCriador(firebaseAuth.getCurrentUser().getUid());
        servico.setLatitude(latLng.latitude);
        servico.setLongitude(latLng.longitude);
        databaseReference.child("servico").child("aberto").child(servicoId).setValue(servico);
        databaseReference.child("servico").child("aberto").child(servicoId).child("ordem-ref").setValue(new Timestamp(-1 * data.getTime()).toString());
        return servico;
    }
}
