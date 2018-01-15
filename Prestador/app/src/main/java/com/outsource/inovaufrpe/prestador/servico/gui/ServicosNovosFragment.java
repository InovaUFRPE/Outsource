package com.outsource.inovaufrpe.prestador.servico.gui;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.outsource.inovaufrpe.prestador.utils.CardFormat;
import com.outsource.inovaufrpe.prestador.utils.ServicoDistanciaAdapter;

import java.util.ArrayList;
import java.util.List;

public class ServicosNovosFragment extends Fragment implements ServicoDistanciaAdapter.OnItemClicked {
    private RecyclerView mRecyclerView;
    private FusedLocationProviderClient mFusedLocationClient;
    private ValueEventListener valueEventListener;
    private Location locationUsuario;
    private List<Servico> servicos;

    private TextView tvNenhumServico;


    DatabaseReference databaseReference;

    CardFormat cardFormat = new CardFormat();

    public ServicosNovosFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) throws NullPointerException {
        View layout = inflater.inflate(R.layout.fragment_servicos_novos, container, false);
        servicos = new ArrayList<Servico>();
        mRecyclerView = layout.findViewById(R.id.recycleID);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference("servico").child("aberto");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

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
                    if (result[0] < 1000) {
                        servicos.add(servico);
                    }
                }
                if (servicos.isEmpty()) {
                    tvNenhumServico.setVisibility(View.VISIBLE);
                } else {
                    tvNenhumServico.setVisibility(View.GONE);
                    adaptador();
                    mRecyclerView.setAdapter(new ServicoDistanciaAdapter(servicos, getContext(), ServicosNovosFragment.this));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                tvNenhumServico.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Erro ao carregar dados", Toast.LENGTH_SHORT).show();
            }
        };

        return layout;
    }

    private void adaptador() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            locationUsuario = location;
                            databaseReference.orderByChild("ordem-ref").addValueEventListener(valueEventListener);
                        }
                    }
                });


    }

    @Override
    public void onItemClick(int position) {
        Intent it = new Intent(getActivity(), VisualizarServicoActivity.class);
        Servico servico = servicos.get(position);
        it.putExtra("servicoID", servico.getId());
        it.putExtra("estado", servico.getEstado());
        startActivity(it);
    }


}
