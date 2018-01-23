package com.outsource.inovaufrpe.prestador.servico.gui;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.servico.dominio.Servico;
import com.outsource.inovaufrpe.prestador.utils.ServicoDistanciaAdapter;

import java.util.ArrayList;
import java.util.List;

public class ServicosNovosFragment extends Fragment implements ServicoDistanciaAdapter.OnItemClicked {
    private RecyclerView mRecyclerView;
    private FusedLocationProviderClient mFusedLocationClient;
    private ValueEventListener valueEventListener;
    private Location locationUsuario;
    private List<Servico> servicos;
    private SeekBar sbDistancia;
    private EditText etFiltro;
    private CheckBox cbUrgente;
    private Button btFiltro;
    private TextView tvDistancia;
    private Button btShowHide;
    private final int MY_PERMISSIONS_REQUEST = 0;

    private TextView tvNenhumServico;


    DatabaseReference databaseReference;


    public ServicosNovosFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) throws NullPointerException {
        View layout = inflater.inflate(R.layout.fragment_servicos_novos, container, false);
        servicos = new ArrayList<Servico>();
        mRecyclerView = layout.findViewById(R.id.recycleID);
        sbDistancia = layout.findViewById(R.id.sbDistanciaID);
        cbUrgente = layout.findViewById(R.id.cbUrgenteID);
        etFiltro = layout.findViewById(R.id.etFiltroID);
        btFiltro = layout.findViewById(R.id.btFiltroID);
        btShowHide = layout.findViewById(R.id.btShowHide);
        final CardView cvFiltro = layout.findViewById(R.id.cvFiltro);
        cvFiltro.setVisibility(View.GONE);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference("servico").child("aberto");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        tvDistancia = layout.findViewById(R.id.tvDistanciaID);
        tvDistancia.setText("Distância: " + sbDistancia.getProgress() + " Km");
        tvNenhumServico = layout.findViewById(R.id.nenhum_servico);
        String s = getContext().getString(R.string.nenhum_servico) + " novo";
        tvNenhumServico.setText(s);
        tvNenhumServico.setVisibility(View.GONE);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servicos.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Servico servico = dados.getValue(Servico.class);
                    float[] result = new float[2];
                    Location.distanceBetween(servico.getLatitude(), servico.getLongitude(), locationUsuario.getLatitude(), locationUsuario.getLongitude(), result);
                    if (result[0] < sbDistancia.getProgress() * 1000) {
                        if(etFiltro.getText().toString().isEmpty()){
                            servicos.add(servico);
                        }else{
                            StringBuilder sb = new StringBuilder();
                            sb.append(servico.getDescricao()).append(servico.getNome());
                            if(sb.toString().toUpperCase().contains(etFiltro.getText().toString().toUpperCase())){
                                servicos.add(servico);
                            }
                        }
                    }
                }
                if (servicos.isEmpty()) {
                    tvNenhumServico.setVisibility(View.VISIBLE);
                } else {
                    tvNenhumServico.setVisibility(View.GONE);
                    mRecyclerView.setAdapter(new ServicoDistanciaAdapter(servicos, getContext(), ServicosNovosFragment.this));
                }
                mRecyclerView.setAdapter(new ServicoDistanciaAdapter(servicos, getContext(), ServicosNovosFragment.this));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                tvNenhumServico.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Erro ao carregar dados", Toast.LENGTH_SHORT).show();
            }
        };

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST);

        btShowHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cvFiltro.isShown()){
                    cvFiltro.setVisibility(View.GONE);
                }else{
                    cvFiltro.setVisibility(View.VISIBLE);
                }
            }
        });

        btFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adaptador();
            }
        });

        sbDistancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvDistancia.setText("Distância: " + sbDistancia.getProgress() + " Km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        adaptador();
        return layout;
    }


    private void adaptador() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            locationUsuario = location;
                            if (cbUrgente.isChecked()){
                                databaseReference.orderByChild("ordem-ref").addValueEventListener(valueEventListener);
                            }else{
                                databaseReference.orderByChild("ordem-ref").startAt("1").addValueEventListener(valueEventListener);
                            }

                        }
                    }
                });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(requestCode==MY_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                adaptador();

            } else {
                //GABRIEL FAAZER DIALOGO AVISANDO QUE PRECISA DA LOCALIZAÇÃO

            }
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent it = new Intent(getActivity(), VisualizarServicoActivity.class);
        Servico servico = servicos.get(position);
        it.putExtra("servicoID", servico.getId());
        it.putExtra("estado", servico.getEstado());
        it.putExtra("nomeServico", servico.getNome());
        startActivity(it);
    }


}
