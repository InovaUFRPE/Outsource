package com.outsource.inovaufrpe.usuario.servico.gui;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.servico.dominio.Servico;
import com.outsource.inovaufrpe.usuario.solicitante.gui.MainActivity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Heitor on 14/01/2018.
 */

public class EditarServicoActivity extends AppCompatActivity {
    EditText etNomeServicoID;
    EditText etDescricaoServicoID;
    EditText etPrecoServicoID;
    TextView tvData;
    RelativeLayout servicoBar;
    Switch switchTipoServico;
    String servicoId;
    String estadoId;
    LatLng latLngInicial;

    Button placePickerID;
    LatLng latLng = null;
    final int PLACE_PICKER_REQUEST = 1;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceServico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.editar_servico));
        setContentView(R.layout.activity_cadastro_servico);

        Intent intent = getIntent();
        servicoId = intent.getStringExtra("servicoID");
        estadoId = intent.getStringExtra("estado");
        servicoBar = findViewById(R.id.tipo_servico_bar);
        etNomeServicoID = findViewById(R.id.etNomeServicoID);
        etDescricaoServicoID = findViewById(R.id.etDescricaoServicoID);
        etPrecoServicoID = findViewById(R.id.etPrecoServicoID);
        placePickerID = findViewById(R.id.placePickerID);
        final RelativeLayout wellServicoUrgencia = findViewById(R.id.info_servico_urgencia);
        final TextView tvCifrao = findViewById(R.id.tvCifrao);

        switchTipoServico = findViewById(R.id.switchTipoServico);

        databaseReferenceServico = FirebaseDatabase.getInstance().getReference().child("vizualizacao");
        databaseReferenceServico.child(estadoId).child(servicoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Servico servico = dataSnapshot.getValue(Servico.class);
                etNomeServicoID.setText(servico.getNome());
                etDescricaoServicoID.setText(servico.getDescricao());
                etPrecoServicoID.setText(servico.getPreco().toString());
                if(servico.isUrgente()){
                    switchTipoServico.setChecked(true);
                }
                latLngInicial = new LatLng(servico.getLatitude(), servico.getLongitude());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        switchTipoServico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                ObjectAnimator colorFade;
                if(isChecked) {
                    colorFade = ObjectAnimator.ofObject(servicoBar, "backgroundColor", new ArgbEvaluator(), getResources().getColor(R.color.colorGreen), getResources().getColor(R.color.colorDanger));
                    wellServicoUrgencia.setVisibility(View.VISIBLE);
                } else {
                    colorFade = ObjectAnimator.ofObject(servicoBar, "backgroundColor", new ArgbEvaluator(), getResources().getColor(R.color.colorDanger), getResources().getColor(R.color.colorGreen));
                    wellServicoUrgencia.setVisibility(View.GONE);
                }
                colorFade.setDuration(400);
                colorFade.start();
            }
        });

        etPrecoServicoID.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0) {
                    tvCifrao.setTextColor(getResources().getColor(R.color.colorBlack));
                    tvCifrao.setAlpha(0.5f);
                } else if(s.length() == 6) {
                    etPrecoServicoID.setError("O valor do serviço não pode ultrapassar 5 casas");
                } else {
                    tvCifrao.setTextColor(getResources().getColor(R.color.colorGreen));
                    tvCifrao.setAlpha(1.0f);
                }
            }
        });

        //TODO: pegar dados de uma api;
        tvData = findViewById(R.id.tDataServicoID);
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        tvData.setText(sdf.format(currentTime));
        if (firebaseDatabase == null) {
            inicializarFirebase();
        }


        placePickerID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                builder.setLatLngBounds(new LatLngBounds(latLngInicial,latLngInicial));
                try {
                    startActivityForResult(builder.build(EditarServicoActivity.this),PLACE_PICKER_REQUEST);
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
                placePickerID.setText(place.getAddress());
            }
        }
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(EditarServicoActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void confirmar() {
        if(latLng == null){
            Toast.makeText(this, "Selecione um local para o serviço!", Toast.LENGTH_SHORT).show();
        }else {
            atualizaServico();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void atualizaServico() {
        databaseReferenceServico.child(estadoId).child(servicoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String gambi;
                Date data = new Date();
                Servico servico = dataSnapshot.getValue(Servico.class);
                servico.setNome(etNomeServicoID.getText().toString().trim());
                servico.setDescricao(etDescricaoServicoID.getText().toString().trim());
                servico.setPreco(Double.valueOf(etPrecoServicoID.getText().toString().trim()));
                servico.setData(servico.getData());
                servico.setEstado(estadoId);
                servico.setIdCriador(firebaseAuth.getCurrentUser().getUid());
                servico.setLatitude(latLng.latitude);
                servico.setLongitude(latLng.longitude);

                if (switchTipoServico.isChecked()) {
                    gambi = "0";
                    servico.setUrgente(true);
                } else {
                    gambi = "1";
                    servico.setUrgente(false);
                }

                databaseReference.child("servico").child(estadoId).child(servicoId).setValue(servico);
                databaseReference.child("servico").child(estadoId).child(servicoId).child("ordem-ref").setValue(gambi + new Timestamp(-1 * data.getTime()).toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.salvar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.salvarBtn) {
            confirmar();
        }
        return super.onOptionsItemSelected(item);
    }
}
