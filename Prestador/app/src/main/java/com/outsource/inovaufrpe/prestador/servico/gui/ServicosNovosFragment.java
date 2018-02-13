package com.outsource.inovaufrpe.prestador.servico.gui;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.servico.adapter.ServicoDistanciaAdapter;
import com.outsource.inovaufrpe.prestador.servico.dominio.ServicoView;
import com.outsource.inovaufrpe.prestador.utils.Sessao;
import com.outsource.inovaufrpe.prestador.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ServicosNovosFragment extends Fragment implements ServicoDistanciaAdapter.OnItemClicked {
    private RecyclerView mRecyclerView;
    private FusedLocationProviderClient mFusedLocationClient;
    private ValueEventListener valueEventListener;
    private Location locationUsuario;
    private List<ServicoView> servicos;
    private SeekBar sbDistancia;
    private EditText etFiltro;
    private CheckBox cbUrgente;
    private TextView tvDistancia;
    private final int MY_PERMISSIONS_REQUEST = 0;
    private FloatingActionButton filtroBtn;
    private Sessao sessao;
    private TextView tvNenhumServico;


    DatabaseReference databaseReference;


    public ServicosNovosFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) throws NullPointerException {
        View layout = inflater.inflate(R.layout.fragment_servicos_novos, container, false);
        servicos = new ArrayList<ServicoView>();
        mRecyclerView = layout.findViewById(R.id.recycleID);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference("visualizacao").child("aberto");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        tvNenhumServico = layout.findViewById(R.id.nenhum_servico);
        String s = getContext().getString(R.string.nenhum_servico) + " novo";
        tvNenhumServico.setText(s);
        tvNenhumServico.setVisibility(View.GONE);
        filtroBtn = layout.findViewById(R.id.filtrarBtn);

        sessao = Sessao.getInstancia(getContext());

        final String stringUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        filtroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFiltroServico();
            }
        });

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servicos.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    ServicoView servico = dados.getValue(ServicoView.class);
                    float[] result = new float[2];
                    if (!servico.getIdCriador().equals(stringUserId)) {
                        Location.distanceBetween(servico.getLatitude(), servico.getLongitude(), locationUsuario.getLatitude(), locationUsuario.getLongitude(), result);
                        if (result[0] < sessao.getRange() * 1000) {
                            if(sessao.getFiltro().isEmpty()) {
                                servicos.add(servico);
                            } else {
                                StringBuilder sb = new StringBuilder();
                                sb.append(servico.getDescricao()).append(servico.getNome());
                                if(sb.toString().toUpperCase().contains(sessao.getFiltro().toUpperCase())) {
                                    servicos.add(servico);
                                }
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
                Utils.criarToast(getActivity(), "Erro ao carregar os dados");
            }
        };

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST);

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
                            if (sessao.isUrgencia()){
                                databaseReference.orderByChild("ordemRef").addValueEventListener(valueEventListener);
                            }else{
                                databaseReference.orderByChild("ordemRef").startAt("1").addValueEventListener(valueEventListener);
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
        ServicoView servico = servicos.get(position);
        it.putExtra("servicoID", servico.getId());
        it.putExtra("estado", servico.getEstado());
        it.putExtra("nomeServico", servico.getNome());
        startActivity(it);
    }

    private void dialogFiltroServico(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View v1 = getLayoutInflater().inflate(R.layout.dialog_filtro_servicos, null);
        sbDistancia = v1.findViewById(R.id.sbDistanciaID);
        cbUrgente = v1.findViewById(R.id.cbUrgenteID);
        etFiltro = v1.findViewById(R.id.etFiltroID);
        tvDistancia = v1.findViewById(R.id.tvDistanciaID);

        //sessao
        sbDistancia.setProgress(sessao.getRange());
        cbUrgente.setChecked(sessao.isUrgencia());
        etFiltro.setText(sessao.getFiltro());

        tvDistancia.setText("Distância: " + sbDistancia.getProgress() + " Km");
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
        mBuilder.setPositiveButton(R.string.aceitar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        adaptador();
                        sessao.setUrgencia(cbUrgente.isChecked());
                        sessao.setRange(sbDistancia.getProgress());
                        sessao.setFiltro(etFiltro.getText().toString());
                        sessao.salvarSessao();
                        dialog.cancel();
                        Utils.esconderTeclado(getActivity());
                    }
                });
        mBuilder.setView(v1);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }


}
