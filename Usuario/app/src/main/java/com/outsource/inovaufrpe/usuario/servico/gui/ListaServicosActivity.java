package com.outsource.inovaufrpe.usuario.servico.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.servico.dominio.Servico;

import java.util.ArrayList;
import java.util.List;

public class ListaServicosActivity extends AppCompatActivity {
    EditText etAtualizaNomeServicoID;
    EditText etAtualizaDescricaoServicoID;
    EditText etAtualizaPrecoServicoID;
    ListView lServicos;
    Button btDeletarID;
    Button btConfirmarID;

    private List<Servico> listServico = new ArrayList<Servico>();
    private ArrayAdapter<Servico> arrayAdapterServico;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    Servico servicoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_servicos);
        etAtualizaNomeServicoID = (EditText)findViewById(R.id.etAtualizaNomeServicoID);
        etAtualizaDescricaoServicoID =(EditText)findViewById(R.id.etAtualizaDescricaoServicoID);
        etAtualizaPrecoServicoID = (EditText)findViewById(R.id.etAtualizaPrecoServicoID);
        btConfirmarID = (Button)findViewById(R.id.btConfirmarID);
        btDeletarID = (Button)findViewById(R.id.btDeletarID);
        lServicos = (ListView)findViewById(R.id.lServicos);

        inicializarFirebase();
        eventoDatabase();

        lServicos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                servicoSelecionado = (Servico)parent.getItemAtPosition(position);
                etAtualizaNomeServicoID.setText(servicoSelecionado.getNome());
                etAtualizaDescricaoServicoID.setText(servicoSelecionado.getDescricao());
                etAtualizaPrecoServicoID.setText(servicoSelecionado.getPreco());
            }
        });
        btConfirmarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Servico servico = new Servico();
                servico.setId(servicoSelecionado.getId());
                servico.setNome(etAtualizaNomeServicoID.getText().toString().trim());
                servico.setDescricao(etAtualizaDescricaoServicoID.getText().toString().trim());
                servico.setPreco(etAtualizaPrecoServicoID.getText().toString().trim());
                databaseReference.child("servico").child(servico.getId()).setValue(servico);
            }
        });
        btDeletarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("servico").child(servicoSelecionado.getId()).removeValue();
            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(ListaServicosActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();

    }

    private void eventoDatabase() {
        databaseReference.child("servico").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listServico.clear();
                for (DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                    Servico servico = objSnapshot.getValue(Servico.class);
                    listServico.add(servico);
                }
                arrayAdapterServico = new ArrayAdapter<Servico>(ListaServicosActivity.this,android.R.layout.simple_list_item_1,listServico);
                lServicos.setAdapter(arrayAdapterServico);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
