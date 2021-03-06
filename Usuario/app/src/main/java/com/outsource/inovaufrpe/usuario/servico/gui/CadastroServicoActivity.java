package com.outsource.inovaufrpe.usuario.servico.gui;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.servico.dominio.EstadoServico;
import com.outsource.inovaufrpe.usuario.servico.dominio.Servico;
import com.outsource.inovaufrpe.usuario.servico.dominio.ServicoView;
import com.outsource.inovaufrpe.usuario.solicitante.gui.MainActivity;
import com.outsource.inovaufrpe.usuario.utils.Utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CadastroServicoActivity extends AppCompatActivity {
    private EditText etNomeServicoID;
    private EditText etDescricaoServicoID;
    private EditText etPrecoServicoID;
    private TextView tvData;
    private RelativeLayout servicoBar;
    private Switch switchTipoServico;
    private CheckBox ckbCombinarPreco;

    Button placePickerID;
    LatLng latLng = null;
    final int PLACE_PICKER_REQUEST = 1;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Criar novo serviço");
        setContentView(R.layout.activity_cadastro_servico);

        servicoBar = findViewById(R.id.tipo_servico_bar);
        etNomeServicoID = findViewById(R.id.etNomeServicoID);
        etDescricaoServicoID = findViewById(R.id.etDescricaoServicoID);
        etPrecoServicoID = findViewById(R.id.etPrecoServicoID);
        placePickerID = findViewById(R.id.placePickerID);
        final RelativeLayout wellServicoUrgencia = findViewById(R.id.info_servico_urgencia);
        final TextView tvCifrao = findViewById(R.id.tvCifrao);

        switchTipoServico = findViewById(R.id.switchTipoServico);

        switchTipoServico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                ObjectAnimator colorFade;
                Utils.esconderTeclado(CadastroServicoActivity.this);
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

        ckbCombinarPreco = findViewById(R.id.ckbCombinarPreco);

        ckbCombinarPreco.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Utils.esconderTeclado(CadastroServicoActivity.this);
                if (isChecked) {
                    tvCifrao.setVisibility(View.INVISIBLE);
                    etPrecoServicoID.setText("0");
                    etPrecoServicoID.setVisibility(View.INVISIBLE);
                } else {
                    tvCifrao.setVisibility(View.VISIBLE);
                    etPrecoServicoID.setText("");
                    etPrecoServicoID.setVisibility(View.VISIBLE);
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
                placePickerID.setText(place.getAddress());
            }
        }
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(CadastroServicoActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void confirmar() {
        if(latLng == null){
            Utils.criarToast(this, "Selecione um local para o serviço!");
            return;
        }if (etNomeServicoID.getText().toString().trim().isEmpty()) {
            Utils.criarToast(this,"Dê um nome para este serviço.");
            return;
        }if (etPrecoServicoID.getText().toString().trim().isEmpty()) {
            Utils.criarToast(this,"Dê um preço para este serviço.");
            return;
        }
        criaServico();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    private Servico criaServico() {
        String gambi;
        String servicoId = databaseReference.child("servico").child("aberto").push().getKey();
        Date data = new Date();
        Servico servico = new Servico();
        servico.setId(servicoId);
        servico.setNome(etNomeServicoID.getText().toString().trim());
        servico.setDescricao(etDescricaoServicoID.getText().toString().trim());
        servico.setPreco(Double.valueOf(etPrecoServicoID.getText().toString().trim()));
        servico.setData(new Timestamp(data.getTime()).toString());
        servico.setEstado(EstadoServico.ABERTA.getValue());
        servico.setIdCriador(firebaseAuth.getCurrentUser().getUid());
        servico.setLatitude(latLng.latitude);
        servico.setLongitude(latLng.longitude);
        servico.setEndereco(placePickerID.getText().toString());


        ServicoView servicoView = new ServicoView();
        if (switchTipoServico.isChecked()) {
            gambi = "0";
            servico.setUrgente(true);
            servicoView.setUrgente(true);
        } else {
            gambi = "1";
            servico.setUrgente(false);
            servicoView.setUrgente(false);
        }

        servicoView.setId(servico.getId());
        servicoView.setNome(servico.getNome());
        servicoView.setDescricao(servico.getDescricao());
        servicoView.setEstado(servico.getEstado());
        servicoView.setPreco(servico.getPreco());
        servicoView.setData(servico.getData());
        servicoView.setIdCriador(servico.getIdCriador());
        servicoView.setLatitude(servico.getLatitude());
        servicoView.setLongitude(servico.getLongitude());
        servicoView.setEndereco(placePickerID.getText().toString());
        servicoView.setOrdemRef(gambi + new Timestamp(-1 * data.getTime()).toString());


        databaseReference.child("servico").child(servicoId).setValue(servico).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Utils.criarToast(CadastroServicoActivity.this, task.getException().getMessage());
                }
            }
        });

        databaseReference.child("visualizacao").child("aberto").child(servicoId).setValue(servicoView).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Utils.criarToast(CadastroServicoActivity.this, task.getException().getMessage());
                }
            }
        });

        return servico;
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
